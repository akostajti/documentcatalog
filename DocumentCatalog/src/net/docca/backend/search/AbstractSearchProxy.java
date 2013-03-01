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
import java.util.Map;

import net.docca.backend.search.indexers.AbstractIndexer;
import net.docca.backend.search.indexers.Indexer;
import net.docca.backend.search.indexers.IndexingException;
import net.docca.backend.search.lucene.LuceneProxy;

/**
 * Abstract implementation of <code>SearchProxy</code>.
 * Also a factory class for <code>SearchProxy</code> instances.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
public abstract class AbstractSearchProxy implements SearchProxy {
	/**
	 * contains the proxy implementations for the types.
	 */
	private static final Map<ProxyTypes, SearchProxy> PROXIES;

	// TODO: make this configurable
	static {
		PROXIES = new HashMap<ProxyTypes, SearchProxy>();
		PROXIES.put(ProxyTypes.lucene, LuceneProxy.getInstance());
	}

	/**
	 * Returns a <code>SearchProxy</code> implementation for <code>type</code>.
	 *
	 * @param type the type for which to find the search proxy implementation.
	 * @return the search proxy implementation of the type. If not found then an
	 * <code>IllegalArgumentException</code> is thrown.
	 */
	public static SearchProxy getSearchProxyForType(final ProxyTypes type) {
		SearchProxy proxy = PROXIES.get(type);
		if (proxy == null) {
			throw new IllegalArgumentException("No SearchProxy implementation found for type " + type);
		}

		return proxy;
	}

	/**
	 * default implementation of <code>index()</code>. delegates the indexing to the indexer.
	 * for the type of the proxy.
	 * @param indexable the object to index
	 * @return <code>true</code> if te indexing was successful
	 * @throws IndexingException on any indexing error
	 */
	@Override
	public final boolean index(final Indexable indexable) throws IndexingException {
		Indexer indexer = AbstractIndexer.getIndexerForType(getType());
		return indexer.index(indexable);
	}
}
