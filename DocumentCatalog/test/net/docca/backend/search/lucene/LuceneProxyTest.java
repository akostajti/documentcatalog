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

import org.apache.lucene.store.AlreadyClosedException;
import org.testng.Assert;

/**
 * tests for lucene proxy. there are other test for this class.
 * @see LuceneIndexerTest
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
public class LuceneProxyTest {
	/**
	 * tests if finalize closes the searcher manager.
	 * @throws Throwable
	 */
	public final void testFinalize() throws Throwable {
		LuceneProxy proxy = (LuceneProxy) AbstractSearchProxy.getSearchProxyForType(ProxyTypes.lucene);
		Assert.assertNotNull(proxy.getSearcherManager());
		proxy.finalize();
		try {
			proxy.getSearcherManager().close();
			Assert.fail("an exception must have been thrown since the manager is already closed");
		} catch (AlreadyClosedException ex) {

		}
	}
}
