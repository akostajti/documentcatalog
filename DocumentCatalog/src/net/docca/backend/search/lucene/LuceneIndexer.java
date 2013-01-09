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

import net.docca.backend.search.Indexable;

/**
 * A lucene indexer class indexing all types of <code>Indexable</code> objects. Uses the
 * <code>Indexer.getProperties()</code> method to find all relevant data to index.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
public class LuceneIndexer extends AbstractLuceneIndexer<Indexable> {

	/**
	 * Indexes <code>indexable</code> using the properties returned by <code>indexable.getProperties()</code>.
	 * All properties are stored in a separate column of the lucene index.
	 *
	 * @param indexable the object to index
	 * @return <code>true</code> if the indexing was successful.
	 */
	@Override
	public final boolean index(final Indexable indexable) {
		// TODO Auto-generated method stub
		return false;
	}
}

