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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.docca.backend.search.AbstractSearchProxy;
import net.docca.backend.search.DefaultSearchExpression;
import net.docca.backend.search.MockIndexable;
import net.docca.backend.search.ProxyTypes;
import net.docca.backend.search.SearchExpression;
import net.docca.backend.search.SearchProxy;
import net.docca.backend.search.SearchResult;
import net.docca.backend.search.indexers.Indexer;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests for lucene indexer.
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Test(groups = {"mustrun", "search" })
public class LuceneIndexerTest {
	/**
	 * tests if the indexer can index any kind of <code>Indexable</code> objects.
	 */
	public final void testIndexMock() {
		MockIndexable subject = new MockIndexable();

		SearchProxy proxy = AbstractSearchProxy.getSearchProxyForType(ProxyTypes.lucene);

		// indexable is null, it is not indexed
		Assert.assertFalse(proxy.index(subject));

		// properties is null, it is not indexed
		Assert.assertFalse(proxy.index(subject));

		// add some properties
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("title", "the loneliness of the long distance runner");
		properties.put("writer", "sillitoe");
		properties.put("year", Integer.valueOf(1960));

		SearchExpression expression = new DefaultSearchExpression("title: loneliness");
		Assert.assertTrue(proxy.index(subject));
		SearchResult result = proxy.find(expression);
		Assert.assertEquals(result.getItems().size(), 1);
		Assert.assertEquals(result.getSearchExpression(), expression);
		Map<String, String> resultProperties = result.getItems().get(0).getProperties();
		for (Entry<String, Object> property: properties.entrySet()) {
			String key = new Indexer.NameCanonizer().canonize(resultProperties.get(property.getKey()));
			Assert.assertEquals(key, property.getValue().toString());
		}
	}
}

