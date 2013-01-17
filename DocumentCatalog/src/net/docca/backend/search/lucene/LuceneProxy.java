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
 * <
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
public class LuceneProxy extends AbstractSearchProxy {
	/**
	 * the maximum number of matches returned by the searches.
	 */
	public static final int MAXIMUM_MATCHES = 1000;

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
		Directory directory;
		try {
			directory = FSDirectory.open(indexDir);
			searcherManager = new SearcherManager(directory, new SearcherFactory());
			// start the reopene job
			triggerReopenJob();
		} catch (IOException e) {
			LOGGER.error("couldn't create the searcher manager", e);
		}
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

	@Override
	public final SearchResult find(final SearchExpression expression) throws SearchException {
		if (searcherManager == null) {
			LOGGER.error("searcher manager was null couldn't complete search " + expression);
			return null;
		}

		IndexSearcher searcher = null;
		try {
			List<Item> resultItems = new ArrayList<Item>(); // the items of the result
			QueryParser parser = new QueryParser(Version.LUCENE_40, "content",
					new StandardAnalyzer(Version.LUCENE_40));
			//TODO: use a stopword list and an oter default field name
			Query query = parser.parse(expression.getRawExpression());
			searcher = searcherManager.acquire();

			TopDocs matches = searcher.search(query, MAXIMUM_MATCHES);
			for (ScoreDoc scoreDoc: matches.scoreDocs) {
				Item item = new Item();
				Document document = searcher.doc(scoreDoc.doc);
				// visit all fields and add them to the result
				List<IndexableField> fields = document.getFields();
				for (IndexableField field: fields) {
					item.addProperty(field.name(), field.stringValue());
				}
				resultItems.add(item);
			}

			// construct the result
			DefaultSearchResult result = new DefaultSearchResult();
			result.setItems(resultItems);
			result.setSearchExpression(expression);
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
	 * refreshes the searcher manager if necessary. blocking method.
	 */
	final void refreshSearcherManager() {
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
	protected final void closeSearcherManager() {
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
	static SearcherManager getSearcherManager() {
		return searcherManager;
	}

	/**
	 * closes the searcher manager.
	 */
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		closeSearcherManager();
	}

	@Override
	public final ProxyTypes getType() {
		return ProxyTypes.lucene;
	}
}

