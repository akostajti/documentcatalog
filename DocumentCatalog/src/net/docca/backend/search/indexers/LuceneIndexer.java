/*
 * Copyright by Akos Tajti (akos.tajti@gmail.com)
 *
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Akos Tajti. ("Confidential Information"). You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Akos Tajti.
 */
package net.docca.backend.search.indexers;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import net.docca.backend.Config;
import net.docca.backend.search.Indexable;
import net.docca.backend.search.ProxyTypes;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * A lucene indexer class indexing all types of <code>Indexable</code> objects. Uses the
 * <code>Indexer.getProperties()</code> method to find all relevant data to index.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
public class LuceneIndexer extends AbstractLuceneIndexer {
	/**
	 * the name of the index field storing the id.
	 */
	private static final String ID_FIELD = "id";

	/**
	 * the logger for this class.
	 */
	public static final Logger LOGGER = Logger.getLogger(LuceneIndexer.class);

	/**
	 * constructor with protected visibility. this ensures that the class can be instantiated only
	 * by the factory.
	 */
	LuceneIndexer() { }

	/**
	 * returns an <code>IndexWiter</code> that uses the index in the directory returned by
	 * <code>Config.getIndexLocation()</code>. Either a new index is created or an existing one
	 * is opened for writing - it depends on the value of <code>overwrite</code>.
	 *
	 * @param overwrite specifies if the existing index must be used ar a new one must be created.
	 * If true then the already existing index in the index directory is overwritten.
	 * @return the index writer. <code>null</code> if it cannot be created.
	 * @throws IOException throw when the index cannot be opened for some reason
	 */
	public final IndexWriter getIndexWriter(final boolean overwrite) throws IOException {
		String indexLocation = Config.getInstance().getIndexLocation();
		if (indexLocation == null) {
			LOGGER.error("index cannot be created because index location was not set correctly");
			return null;
		}

		File indexDir = new File(indexLocation);
		LOGGER.debug("using index directory " + indexDir);

		// set up the objects needed for opening the index
		Directory directory = FSDirectory.open(indexDir);
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, analyzer);

		if (overwrite) {
			config.setOpenMode(OpenMode.CREATE);
		} else {
			config.setOpenMode(OpenMode.CREATE_OR_APPEND);
		}

		IndexWriter writer = new IndexWriter(directory, config);
		LOGGER.debug("successfully opened indexwriter");

		return writer;
	}

	/**
	 * creates a lucene document from an indexable. converts each properties of
	 * <code>indexable</code> to string and adds them to the document.
	 *
	 * @param indexable the object to create the document for.
	 * @return the document created or <code>null</code> if <code>indexable.getProperties()</code>
	 * returned <code>null</code>.
	 */
	private Document createDocument(final Indexable indexable) {
		Map<String, Object> properties = indexable.getProperties();
		if (properties == null || properties.isEmpty()) {
			LOGGER.debug("no properties found in indexable " + indexable);
			return null;
		}

		Document result = new Document();

		// visit all entries in the map, convert them to strings and add them to the document
		for (Entry<String, Object> property: properties.entrySet()) {
			String value = "";
			if (property.getValue() != null) {
				value = property.getValue().toString();
			}
			TextField field = new TextField(property.getKey(), value, Field.Store.YES); // TODO: revise this (storage and field type)
			result.add(field);
		}

		// also add the id
		IntField field = new IntField(ID_FIELD, indexable.getId(), Field.Store.YES);
		result.add(field);

		return result;
	}

	/**
	 * closes the index writer.
	 * @param writer the writer to close
	 * @throws IOException
	 */
	public final void closeIndexWriter(final IndexWriter writer) throws IOException {
		if (writer == null) {
			return;
		}

		try {
			writer.commit();
		} finally {
			writer.close();
		}
		LOGGER.debug("closed the index writer");
	}

	/**
	 * Indexes <code>indexable</code> using the properties returned by <code>indexable.getProperties()</code>.
	 * All properties are stored in a separate column of the lucene index.
	 *
	 * @param indexable the object to index
	 * @return <code>true</code> if the indexing was successful.
	 * @throws IndexingException on any indexing error.
	 */
	@Override
	public final boolean index(final Indexable indexable) throws IndexingException {
		if (indexable == null || indexable.getProperties() == null) {
			return false;
		}
		IndexWriter writer = null;
		try {
			writer = getIndexWriter(false);
			if (writer == null) {
				return false;
			}

			Document document = createDocument(indexable);

			if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
				writer.addDocument(document);
				LOGGER.debug("added the doccument to the index");
			} else {
				// update
				writer.updateDocument(new Term("id", indexable.getId().toString()), document);
				LOGGER.debug("updated the document with id " + indexable.getId());

			}
		} catch (Exception ex) {
			throw new IndexingException(ex);
		} finally {
			// always try to close the index writer
			if (writer != null) {
				try {
					closeIndexWriter(writer);
				} catch (IOException ex) {
					LOGGER.debug("couldn't close the index writer", ex);
				}
			}
		}

		return true;
	}

	@Override
	public final ProxyTypes getType() {
		return ProxyTypes.lucene;
	}
}

