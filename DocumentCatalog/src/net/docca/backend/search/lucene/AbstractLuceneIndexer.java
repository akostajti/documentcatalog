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
import net.docca.backend.search.Indexer;

/**
 * Abstract base class for all lucene based indexers.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 * @param <T>
 */
public abstract class AbstractLuceneIndexer<T extends Indexable> implements Indexer<T> {

}

