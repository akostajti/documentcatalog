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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * contains the results of a search.
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
public interface SearchResult {

	/**
	 * class for storing the elements of a search result (rows in the search result table).
	 *
	 * @author Akos Tajti <akos.tajti@gmail.com>
	 *
	 */
	public class Item {
		/**
		 * a map containing all properties of this row of the search result.
		 */
		private Map<String, String> properties = new HashMap<String, String>();

		/**
		 * @return the properties
		 */
		public final Map<String, String> getProperties() {
			return properties;
		}

		/**
		 * @param properties the properties to set
		 */
		public final void setProperties(final Map<String, String> properties) {
			this.properties = properties;
		}

		/**
		 * adds a property to the map.
		 * @param name
		 * @param value
		 */
		public final void addProperty(final String name, final String value) {
			this.properties.put(name, value);
		}
	}

	/**
	 * returns the list of search result items that matched the search expression.
	 * @return the matched items or an empty list (return value is never <code>null</code>).
	 */
	List<Item> getItems();

	/**
	 * returns the search expression that generated this result.
	 * @return a <code>SearchExpression</code> object
	 */
	SearchExpression getSearchExpression();
}

