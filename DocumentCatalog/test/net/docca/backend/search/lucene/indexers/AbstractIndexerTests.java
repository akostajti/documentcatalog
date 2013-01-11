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
package net.docca.backend.search.lucene.indexers;

import net.docca.backend.search.ProxyTypes;
import net.docca.backend.search.indexers.AbstractIndexer;
import net.docca.backend.search.indexers.Indexer;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * tests for the abstract indexer.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Test(groups = {"mustrun", "search" })
public class AbstractIndexerTests {

	/**
	 * tests <code>getIndexerForType()</code>.
	 */
	public final void testGetIndexerForType() {
		try {
			AbstractIndexer.getIndexerForType(null);
			Assert.fail("an exeption must be throw for null parameter");
		} catch (IllegalArgumentException ex) {}

		for (ProxyTypes type: ProxyTypes.values()) {
			Indexer indexer = AbstractIndexer.getIndexerForType(type);
			Assert.assertEquals(indexer.getType(), type);
		}
	}
}

