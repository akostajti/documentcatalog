package net.docca.backend.search.lucene;

import net.docca.backend.search.AbstractSearchProxy;
import net.docca.backend.search.Indexable;
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
	public final boolean index(final Indexable indexable) {
		// TODO Auto-generated method stub
		return false;
	}

}

