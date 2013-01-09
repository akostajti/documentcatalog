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

/**
 * Instances of this interface index a specifc type of objects.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 * @param <T>
 */
public interface Indexer<T extends Indexable> {
	/**
	 * Attempts to index <code>indexable</code>.
	 *
	 * @param indexable the object to be indexed.
	 * @return <code>true</code> if the indexing was successful.
	 */
	boolean index(final T indexable);
}

