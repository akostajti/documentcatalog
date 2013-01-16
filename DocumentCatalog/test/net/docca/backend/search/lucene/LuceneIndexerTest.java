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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import net.docca.backend.Config;
import net.docca.backend.search.AbstractSearchProxy;
import net.docca.backend.search.DefaultSearchExpression;
import net.docca.backend.search.IndexedProperty;
import net.docca.backend.search.IndexedProperty.Stored;
import net.docca.backend.search.MockIndexable;
import net.docca.backend.search.ProxyTypes;
import net.docca.backend.search.SearchException;
import net.docca.backend.search.SearchExpression;
import net.docca.backend.search.SearchProxy;
import net.docca.backend.search.SearchResult;
import net.docca.backend.search.indexers.Indexer;
import net.docca.backend.search.indexers.IndexingException;

import org.apache.commons.io.FileUtils;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests for lucene indexer.
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Test(groups = {"mustrun", "search" })
public class LuceneIndexerTest {

	/**
	 * removes the lucene index.
	 * @throws IOException
	 */
	@BeforeMethod
	public final void cleanup() throws IOException {
		File file = new File(Config.getInstance().getIndexLocation());
		FileUtils.deleteDirectory(file);
	}

	/**
	 * tests if the indexer can index any kind of <code>Indexable</code> objects.
	 * @throws IndexingException thrown by index.
	 * @throws IOException thrown by delete directory.
	 * @throws SearchException
	 */
	public final void testIndexMock() throws IndexingException, IOException, SearchException {
		MockIndexable subject = new MockIndexable();

		SearchProxy proxy = AbstractSearchProxy.getSearchProxyForType(ProxyTypes.lucene);

		// indexable is null, it is not indexed
		Assert.assertFalse(proxy.index(null));

		// properties is null, it is not indexed
		Assert.assertFalse(proxy.index(subject));

		// add some properties
		Map<String, IndexedProperty> properties = new HashMap<String, IndexedProperty>();
		properties.put("title", new IndexedProperty("the loneliness of the long distance runner"));
		properties.put("writer", new IndexedProperty("sillitoe", String.class, Stored.Stored));
		properties.put("year", new IndexedProperty(Integer.valueOf(1960), Integer.class, Stored.Stored));
		subject.setProperties(properties);

		SearchExpression expression = new DefaultSearchExpression("title:loneliness");
		Assert.assertTrue(proxy.index(subject));

		// refresh the manager
		((LuceneProxy) proxy).refreshSearcherManager();

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
		((LuceneProxy) proxy).closeSearcherManager();
		File indexDir = new File(Config.getInstance().getIndexLocation());
		FileUtils.deleteDirectory(indexDir);
		try {
			proxy.index(subject);
		} catch (IndexingException ex) {
			Assert.fail("the index directory must have been created");
		}
	}

	/**
	 * tests if the index reopener jobb is started.
	 * @throws SchedulerException
	 * @throws InterruptedException
	 */
	public final void testReopenerJob() throws SchedulerException, InterruptedException {
		@SuppressWarnings("unused")
		SearchProxy proxy = AbstractSearchProxy.getSearchProxyForType(ProxyTypes.lucene);
		TimeUnit.SECONDS.sleep(5);
		Scheduler scheduler = new StdSchedulerFactory().getScheduler();
		List<JobExecutionContext> currentJobs = scheduler.getCurrentlyExecutingJobs();
		boolean jobIsRunning = false;
		for (JobExecutionContext ctx: currentJobs) {
			String name = ctx.getJobDetail().getKey().getName();
			String group = ctx.getJobDetail().getKey().getGroup();
			if (name.equals("refreshIndexJob")  && group.equals("search")) {
				jobIsRunning = true;
				break;
			}
		}
		Assert.assertTrue(jobIsRunning);
	}
}

