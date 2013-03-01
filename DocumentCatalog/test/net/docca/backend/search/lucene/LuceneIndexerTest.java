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
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import net.docca.backend.Config;
import net.docca.backend.search.DefaultSearchExpression;
import net.docca.backend.search.IndexedProperty;
import net.docca.backend.search.IndexedProperty.Stored;
import net.docca.backend.search.MockIndexable;
import net.docca.backend.search.SearchException;
import net.docca.backend.search.SearchExpression;
import net.docca.backend.search.SearchResult;
import net.docca.backend.search.indexers.Indexer;
import net.docca.backend.search.indexers.IndexingException;
import net.docca.test.AbstractTestNGTest;

import org.apache.commons.io.FileUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.KeyMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests for lucene indexer.
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Test(groups = {"mustrun", "search" })
public class LuceneIndexerTest extends AbstractTestNGTest {
	/**
	 * the autowired proxy object.
	 */
	@Autowired
	private LuceneProxy proxy;

	/**
	 * removes the lucene index.
	 * @throws IOException when couldn't delete the index
	 */
	@BeforeMethod
	public final void setup() throws IOException {
		// delete the index directory
		File file = new File(Config.getInstance().getIndexLocation());
		FileUtils.deleteDirectory(file);
	}

	/** closes the searcherManager.
	 *
	 * @throws Exception on any error
	 */
	@AfterMethod
	public final void cleanupAfter() throws Exception {
		// interrupt the job first; if it is running it's not possible to delete the index directory
		Scheduler scheduler = new StdSchedulerFactory().getScheduler();
		scheduler.interrupt(new JobKey(ReopenSearcherJob.JOB_NAME, ReopenSearcherJob.GROUP_NAME));

		if (LuceneProxy.getInstance().getSearcherManager() != null) {
			LuceneProxy.getInstance().getSearcherManager().close();
		}
	}

	/**
	 * tests if the indexer can index any kind of <code>Indexable</code> objects.
	 * @throws Exception all exceptios are rethrown
	 */
	public final void testIndexMock() throws Exception {
		MockIndexable subject = new MockIndexable();

		// indexable is null, it is not indexed
		Assert.assertFalse(proxy.index(null));

		// properties is null, it is not indexed
		Assert.assertFalse(proxy.index(subject));

		// add an empty map; still no properties expect false return value
		subject.setProperties(new HashMap<String, IndexedProperty>());
		Assert.assertFalse(proxy.index(subject));

		// add some properties
		Map<String, IndexedProperty> properties = new HashMap<String, IndexedProperty>();
		properties.put("title", new IndexedProperty("the loneliness of the long distance runner"));
		properties.put("writer", new IndexedProperty("sillitoe", String.class, Stored.Stored));
		properties.put("year", new IndexedProperty(Integer.valueOf(1960), Integer.class, Stored.Stored));
		properties.put("nullproperty", new IndexedProperty(null));
		subject.setProperties(properties);

		SearchExpression expression = new DefaultSearchExpression("title:loneliness");
		Assert.assertTrue(proxy.index(subject));

		// refresh the manager
		// searcer manager must be reinitialized because the cleanup method closes it
		LuceneProxy.setupSearcherManager();
		proxy.refreshSearcherManager();

		SearchResult result = proxy.find(expression);
		Assert.assertEquals(result.getItems().size(), 1);
		Assert.assertEquals(result.getSearchExpression(), expression);
		Map<String, String> resultProperties = result.getItems().get(0).getProperties();
		for (Entry<String, IndexedProperty> property: properties.entrySet()) {
			String res = new Indexer.NameCanonizer().canonize(resultProperties.get(property.getKey()));
			if (property.getValue().getStored() == Stored.Stored) {
				Assert.assertEquals(res, property.getValue().getValue().toString());
			} else {
				// not stored fields are not returned
				Assert.assertNull(res);
			}
		}
		Assert.assertEquals(resultProperties.get("id"), subject.getId().toString());

		// delete the index directory then try again; an exception must be thrown
		proxy.closeSearcherManager();
		File indexDir = new File(Config.getInstance().getIndexLocation());
		FileUtils.deleteDirectory(indexDir);
		try {
			proxy.index(subject);
		} catch (IndexingException ex) {
			Assert.fail("the index directory must have been created");
		}

		// search with invalid expression
		expression = new DefaultSearchExpression("title*%:loneliness");
		try {
			result = proxy.find(expression);
			Assert.fail("invalid search expression should cause an exception");
		} catch (SearchException ex) {

		}

		// test what happens when the searcher manager couldn't be initialized
		Field field = LuceneProxy.class.getDeclaredField("searcherManager");
		field.setAccessible(true);
		field.set(proxy, null);
		Assert.assertNull(proxy.find(new DefaultSearchExpression("title:loneliness")));
	}

	/**
	 * tests if the index reopener jobb is started.
	 * @throws SchedulerException
	 * @throws InterruptedException
	 */
	public final void testReopenerJob() throws SchedulerException, InterruptedException {
		// create a listener for that job
		SimpleJobListener listener = new SimpleJobListener();
		Scheduler scheduler = new StdSchedulerFactory().getScheduler();
		scheduler.getListenerManager().addJobListener(listener,
				KeyMatcher.keyEquals(JobKey.jobKey(ReopenSearcherJob.JOB_NAME,
						ReopenSearcherJob.GROUP_NAME)));

		// wait till the job gets executed
		TimeUnit.SECONDS.sleep(15);

		Assert.assertTrue(listener.isExecuted());
	}

	/**
	 * tests if the searcher manager is always initialized correctly.
	 * @throws IOException
	 */
	public final void testGetSearcherManager() throws IOException {
		//delete the index dir and check if the manager is created anyway
		FileUtils.deleteDirectory(new File(Config.getInstance().getIndexLocation()));
		// simply load the  class and check if the searcher manager is initialized
		Assert.assertNotNull(LuceneProxy.getInstance().getSearcherManager());
	}

	/**
	 * tests several type of lucene expressions.
	 * @throws SearchException on searching error
	 * @throws IndexingException on indexing error
	 */
	public final void testExpressions() throws SearchException, IndexingException {
		MockIndexable subject1 = new MockIndexable();
		Map<String, IndexedProperty> mockProperties1 = new HashMap<String, IndexedProperty>();
		mockProperties1.put(LuceneProxy.DEFAULT_INDEX_FIELD,
				new IndexedProperty("van már kenyerem, borom is van, van gyermekem és feleségem",
						String.class, Stored.Stored));
		mockProperties1.put("title", new IndexedProperty("boldog, szomorú dal"));
		mockProperties1.put("meta", new IndexedProperty("Kosztolányi Dezső"));
		subject1.setProperties(mockProperties1);

		MockIndexable subject2 = new MockIndexable();
		Map<String, IndexedProperty> mockProperties2 = new HashMap<String, IndexedProperty>();
		mockProperties2.put(LuceneProxy.DEFAULT_INDEX_FIELD,
				new IndexedProperty("beírtak engem mindenféle könyvbe, minden módon számon tartanak",
						String.class, Stored.Stored));
		mockProperties2.put("meta", new IndexedProperty("Kosztolányi Dezső, 1900"));
		subject2.setProperties(mockProperties2);

		// index the subjects
		proxy.index(subject1);
		proxy.index(subject2);

		// refresh the searcher manager
		proxy.refreshSearcherManager();

		// valid expressions
		// by deault it will search on the content field
		DefaultSearchExpression expression = new DefaultSearchExpression("borom");
		SearchResult result = proxy.find(expression);
		Assert.assertEquals(result.getItems().size(), 1);
		Assert.assertTrue(result.getItems().get(0).getProperties()
				.get(LuceneProxy.DEFAULT_INDEX_FIELD).contains("borom"));

		expression = new DefaultSearchExpression("beírtak engem");
		result = proxy.find(expression);
		Assert.assertEquals(result.getItems().size(), 1);
		Assert.assertTrue(result.getItems().get(0).getProperties()
				.get(LuceneProxy.DEFAULT_INDEX_FIELD).contains("beírtak engem"));

		expression = new DefaultSearchExpression("módon könyvbe");
		result = proxy.find(expression);
		Assert.assertEquals(result.getItems().size(), 1);

		// expressions that match both subjects
		expression = new DefaultSearchExpression("meta: kosztolányi");
		result = proxy.find(expression);
		Assert.assertEquals(result.getItems().size(), 2);

		expression = new DefaultSearchExpression("feleségem tartanak");
		result = proxy.find(expression);
		Assert.assertEquals(result.getItems().size(), 2);

		// this won't match any of the subjects
		expression = new DefaultSearchExpression("nemvoltsehol");
		result = proxy.find(expression);
		Assert.assertEquals(result.getItems().size(), 0);

		// special queries
		expression = new DefaultSearchExpression("meta: koszt*");
		result = proxy.find(expression);
		Assert.assertEquals(result.getItems().size(), 2);
	}

	/**
	 * a simple job listener that stores if the job was executed.
	 */
	class SimpleJobListener implements JobListener {
		/**
		 * true, if the job was executed.
		 */
		private boolean executed = false;

		@Override
		public void jobWasExecuted(final JobExecutionContext arg0,
				final JobExecutionException arg1) {
			executed = true;
		}

		@Override
		public void jobToBeExecuted(final JobExecutionContext arg0) {

		}

		@Override
		public void jobExecutionVetoed(final JobExecutionContext arg0) {

		}

		@Override
		public String getName() {
			return "simpleListener";
		}

		/**
		 * @return the executed
		 */
		public boolean isExecuted() {
			return executed;
		}
	}
}

