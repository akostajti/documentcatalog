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
package net.docca.backend.search.lucene;

import java.io.File;
import java.io.IOException;

import net.docca.backend.Config;
import net.docca.backend.search.AbstractSearchProxy;
import net.docca.backend.search.ProxyTypes;
import net.docca.backend.search.SearchExpression;
import net.docca.backend.search.SearchResult;

import org.apache.log4j.Logger;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.SearcherFactory;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;


/**
 * The <code>SearchProxy</code> implementation for Lucene (local lucene process).
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
public class LuceneProxy extends AbstractSearchProxy {
	/**
	 * the logger of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(LuceneProxy.class);

	/**
	 * the searcher manager used for managing the lucene <code>IndexSearcher</code> objects.
	 */
	private static SearcherManager searcherManager;
	static {
		File indexDir = new File(Config.getInstance().getIndexLocation());
		Directory directory;
		try {
			directory = FSDirectory.open(indexDir);
			searcherManager = new SearcherManager(directory, new SearcherFactory());
		} catch (IOException e) {
			LOGGER.error("couldn't create the searcher manager", e);
		}
	}

	@Override
	public final SearchResult find(final SearchExpression expression) {
		if (searcherManager == null) {
			LOGGER.error("searcher manager was null couldn't complete search " + expression);
			return null;
		}
		IndexSearcher searcher = searcherManager.acquire();
		// TODO: implement the rest
		throw new UnsupportedOperationException();
	}

	@Override
	public final ProxyTypes getType() {
		return ProxyTypes.lucene;
	}

}

