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

import net.docca.backend.search.indexers.IndexingException;


/**
 * A proxy for a search service. This can be any webservice or local process.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
public interface SearchProxy {
	/**
	 * Finds an object that matches the criteria defined by <code>expression</code>.
	 * @param expression the search criteria
	 * @return a <code>SearchResult</code> object.
	 */
	SearchResult find(final SearchExpression expression);

	/**
	 * Indexes an indexable object.
	 *
	 * @param indexable the indexable object
	 * @return <code>true</code> if the indexing was successful
	 */
	boolean index(final Indexable indexable) throws IndexingException;

	/**
	 * Returns the type of the service for which this proxy was implemented.
	 *
	 * @return the type of the proxy.
	 */
	ProxyTypes getType();
}

