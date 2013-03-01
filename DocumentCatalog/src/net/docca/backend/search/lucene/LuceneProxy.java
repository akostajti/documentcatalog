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
import java.util.ArrayList;
import java.util.List;

import net.docca.backend.Config;
import net.docca.backend.search.AbstractSearchProxy;
import net.docca.backend.search.DefaultSearchResult;
import net.docca.backend.search.ProxyTypes;
import net.docca.backend.search.SearchException;
import net.docca.backend.search.SearchExpression;
import net.docca.backend.search.SearchResult;
import net.docca.backend.search.SearchResult.Item;
import net.docca.backend.search.indexers.AbstractLuceneIndexer;
import net.docca.backend.search.indexers.LuceneIndexer;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.SearcherFactory;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

/**
 * <p>
 * The <code>SearchProxy</code> implementation for Lucene (local lucene process).
 * </p>
 * <p>
 * this class is a singleton. an instance can be retrieved using <code>getInstance()</code>
 * </p>
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
public final class LuceneProxy extends AbstractSearchProxy {
	/**
	 * the maximum number of matches returned by the searches.
	 */
	public static final int MAXIMUM_MATCHES = 1000;

	/**
	 * the logger of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(LuceneProxy.class);

	/**
	 * the only instance of this class.
	 */
	private static LuceneProxy instance;

	/**
	 * creator method for this class. returns the only instance.
	 * @return <code>INSTANCE</code> the only instance
	 */
	public static LuceneProxy getInstance() {
		if (instance == null) {
			synchronized (LuceneProxy.class) {
				if (instance == null) {
					instance = new LuceneProxy();
				}
			}
		}
		return instance;
	}

	/**
	 * the searcher manager used for managing the lucene <code>IndexSearcher</code> objects.
	 */
	private static SearcherManager searcherManager;
	static {
		setupSearcherManager();
	}

	/**
	 * initializes the searchermanager.
	 */
	protected static void setupSearcherManager() {
		File indexDir = new File(Config.getInstance().getIndexLocation());
		// if the index directory doesn't exist try to create it
		if (!indexDir.exists()) {
			LOGGER.debug("trying to create index directory " + indexDir);
			indexDir.mkdirs();

			// initialize the lucene index
			LuceneIndexer indexer = (LuceneIndexer) AbstractLuceneIndexer
					.getIndexerForType(ProxyTypes.lucene);
			IndexWriter writer = null;
			try {
				writer = indexer.getIndexWriter(true);
			} catch (IOException e) {
				LOGGER.error("failed to create index", e);
			} finally {
				if (indexer != null) {
					try {
						indexer.closeIndexWriter(writer);
					} catch (IOException e) {
					}
				}
			}
		}
		try {
			createSearcherManager(indexDir);
			// start the reopene job
			triggerReopenJob();
		} catch (IOException e) {
			LOGGER.error("couldn't create the searcher manager", e);
		}
	}

	/**
	 * creates a <code>SearcherManager</code> working on <code>indexDir</code>.
	 * @param indexDir the directory where the index is stored.
	 * @throws IOException thrown when the index directory is locked (or on any error)
	 */
	private static void createSearcherManager(final File indexDir) throws IOException {
		Directory directory;
		directory = FSDirectory.open(indexDir);
		searcherManager = new SearcherManager(directory, new SearcherFactory());
	}

	/**
	 * starts the refresher job which will run in every 10 seconds forever.
	 */
	private static void triggerReopenJob() {
		Trigger trigger = TriggerBuilder
				.newTrigger()
				.withIdentity("refreshIndexTrigger", "search")
				.startNow()
				.withSchedule(SimpleScheduleBuilder.simpleSchedule()
						.withIntervalInSeconds(10)
						.repeatForever())
						.build();
		JobDetail job = JobBuilder.newJob(ReopenSearcherJob.class)
				.withIdentity(ReopenSearcherJob.JOB_NAME, ReopenSearcherJob.GROUP_NAME)
				.build();
		try {
			Scheduler scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.scheduleJob(job, trigger);
			scheduler.start();
		} catch (SchedulerException e) {
			LOGGER.error("failed to schedule the refresher job", e);
		}
	}

	/**
	 * hidden default constructor.
	 */
	private LuceneProxy() {

	}

	@Override
	public SearchResult find(final SearchExpression expression) throws SearchException {
		if (searcherManager == null) {
			LOGGER.error("searcher manager was null couldn't complete search " + expression);
			return null;
		}

		IndexSearcher searcher = null;
		try {
			Query query = createQuery(expression);
			searcher = searcherManager.acquire();

			TopDocs matches = searcher.search(query, MAXIMUM_MATCHES);
			DefaultSearchResult result = createSearchResult(expression,
					searcher, matches);
			return result;
		} catch (ParseException e) {
			throw new SearchException(e);
		} catch (IOException e) {
			throw new SearchException(e);
		} finally {
			if (searcher != null) {
				try {
					searcherManager.release(searcher);
				} catch (IOException e) { }
			}
		}
	}

	/**
	 * creates a lucene query object by parsing the search expression.
	 * @param expression the expression that will be parsed
	 * @return the lucene query object
	 * @throws ParseException throw hen the expression is invalid
	 */
	private Query createQuery(final SearchExpression expression)
			throws ParseException {
		QueryParser parser = new QueryParser(Version.LUCENE_40, DEFAULT_INDEX_FIELD,
				new StandardAnalyzer(Version.LUCENE_40));
		//TODO: use a stopword list
		Query query = parser.parse(expression.getRawExpression());
		return query;
	}

	/**
	 * created the <code>DefaultSearchResult</code> object from the results of a search.
	 * @param expression the search expression that gave these results.
	 * @param searcher the <code>IndexSearcher</code> used for searching.
	 * @param matches the matches returned by lucene.
	 * @return a result object containing all matches
	 * @throws IOException thrown on any kind of io error
	 */
	private DefaultSearchResult createSearchResult(
			final SearchExpression expression, final IndexSearcher searcher,
			final TopDocs matches) throws IOException {
		List<Item> resultItems = new ArrayList<Item>(); // the items of the result
		for (ScoreDoc scoreDoc: matches.scoreDocs) {
			Item item = createItem(searcher, scoreDoc);
			resultItems.add(item);
		}

		// construct the result
		DefaultSearchResult result = new DefaultSearchResult();
		result.setItems(resultItems);
		result.setSearchExpression(expression);
		return result;
	}

	/**
	 * creates an <code>Item</code> object from a <code>ScoreDoc</code> object.
	 * @param searcher the index searcher that was used for searching
	 * @param scoreDoc the <code>ScoreDoc</code> to convert
	 * @return an item that will contain all fields of <code>ScoreDoc</code>
	 * @throws IOException thrown on any kind of io error
	 */
	private Item createItem(final IndexSearcher searcher, final ScoreDoc scoreDoc)
			throws IOException {
		Item item = new Item();
		Document document = searcher.doc(scoreDoc.doc);
		// visit all fields and add them to the result
		List<IndexableField> fields = document.getFields();
		for (IndexableField field: fields) {
			item.addProperty(field.name(), field.stringValue());
		}
		return item;
	}

	/**
	 * refreshes the searcher manager if necessary. blocking method.
	 */
	void refreshSearcherManager() {
		if (searcherManager != null) {
			try {
				searcherManager.maybeRefreshBlocking();
			} catch (IOException e) {
				LOGGER.debug("failed to reopen index", e);
			}
		}
	}

	/**
	 * closes the searcher manager.
	 */
	protected void closeSearcherManager() {
		if (searcherManager != null) {
			try {
				searcherManager.close();
			} catch (IOException e) {
				LOGGER.error("couldn't close searcher manager", e);
			}
		}
	}

	/**
	 * returns the searcher maneger. mainly for testing.
	 * @return the searcher manager.
	 */
	SearcherManager getSearcherManager() {
		return searcherManager;
	}

	/**
	 * closes the searcher manager.
	 * @throws Throwable any exception
	 */
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		closeSearcherManager();
	}

	@Override
	public ProxyTypes getType() {
		return ProxyTypes.lucene;
	}
}

