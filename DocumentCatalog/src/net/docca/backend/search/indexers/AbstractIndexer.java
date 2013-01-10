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
package net.docca.backend.search.indexers;

import java.util.HashMap;
import java.util.Map;

import net.docca.backend.search.ProxyTypes;


/**
 * abstract base class of all indexers. it is also a factory for indexers.
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
public abstract class AbstractIndexer implements Indexer {

	/**
	 * contains the proxy implementations for the types.
	 */
	private static final Map<ProxyTypes, Indexer> INDEXERS =
			new HashMap<ProxyTypes, Indexer>();

	// TODO: make this configurable
	static {
		INDEXERS.put(ProxyTypes.lucene, new LuceneIndexer());
	}

	/**
	 * returns an indexer for a given search service type. factory method.
	 *
	 * @param type the type of the search service.
	 * @return the indexer. if no indexer was found for the type then an <code>IllegalArgumentException</code>
	 * is thrown.
	 */
	public static Indexer getIndexerForType(final ProxyTypes type) {
		Indexer indexer = INDEXERS.get(type);
		if (indexer == null) {
			throw new IllegalArgumentException("No indexer found for type " + type);
		}

		return indexer;
	}
}

