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

import net.docca.backend.search.AbstractSearchProxy;
import net.docca.backend.search.ProxyTypes;
import net.docca.backend.search.SearchExpression;
import net.docca.backend.search.SearchResult;

/**
 * The <code>SearchProxy</code> implementation for Lucene (local lucene process).
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
public class LuceneProxy extends AbstractSearchProxy {

	@Override
	public final SearchResult find(final SearchExpression expression) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final ProxyTypes getType() {
		return ProxyTypes.lucene;
	}

}

