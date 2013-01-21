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
package net.docca.backend.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.docca.backend.search.SearchResult.Item;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * tests the methods of <code>DefaultSearchResult</code>.
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Test(groups = {"search", "mustrun" })
public class DefaultSearchResultTest {

	/**
	 * tests if the added result items are always returned.
	 */
	public final void testAddingItem() {
		// getItems() should never return null
		DefaultSearchResult result = new DefaultSearchResult();
		result.setItems(null);
		Assert.assertNotNull(result.getItems());
		Assert.assertTrue(result.getItems().isEmpty());

		Item item = new Item();
		HashMap<String, String> properties = new HashMap<String, String>();
		properties.put("dummy", "i'm here");
		item.setProperties(properties);
		List<Item> items = new ArrayList<Item>();
		items.add(item);
		result.setItems(items);
		Assert.assertEquals(result.getItems().size(), 1);

		result.addItem(item);
		Assert.assertEquals(result.getItems().size(), 2);
	}
}
