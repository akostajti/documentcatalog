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
import java.util.List;

/**
 * default implementation of the <code>SearchResult</code> interface.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
public class DefaultSearchResult implements SearchResult {
	/**
	 * the items matched by <code>searchExpression</code>.
	 */
	private List<Item> items = new ArrayList<Item>();

	/**
	 * the search expression that generated this result.
	 */
	private SearchExpression searchExpression;

	@Override
	public List<Item> getItems() {
		if (items == null) {
			items = new ArrayList<Item>();
		}
		return items;
	}

	/**
	 * sets the items.
	 * @param items .
	 */
	public final void setItems(final List<Item> items) {
		this.items = items;
	}

	/**
	 * adds an item to the result.
	 *
	 * @param item the result item to be added.
	 */
	public final void addItem(final Item item) {
		getItems().add(item);
	}

	@Override
	public final SearchExpression getSearchExpression() {
		return searchExpression;
	}

	/**
	 * sets the search expression that generated this result.
	 *
	 * @param searchExpression the search expression.
	 */
	public final void setSearchExpression(final SearchExpression searchExpression) {
		this.searchExpression = searchExpression;
	}
}
