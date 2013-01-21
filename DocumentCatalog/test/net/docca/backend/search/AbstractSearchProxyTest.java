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


import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests if <code>AbsractSearchProxy</code> works properly.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Test(groups = {"mustrun", "search" })
public class AbstractSearchProxyTest {
	/**
	 * Tests if <code>getSearchProxyForType()</code> returns the correct search proxy.
	 */
	public final void testGetSearchProxyForType() {
		for (ProxyTypes type: ProxyTypes.values()) {
			SearchProxy proxy = AbstractSearchProxy.getSearchProxyForType(type);
			Assert.assertNotNull(proxy);
			Assert.assertEquals(proxy.getType(), type);
		}

		try {
			AbstractSearchProxy.getSearchProxyForType(null);
			Assert.fail("An exception must be throw when an invalid argment is passed to the method");
		} catch (IllegalArgumentException ex) {
			System.err.println(ex);
		}
	}

	/**
	 * invokes some methods on the <code>ProxyTypes</code> enum to get a better coverage.
	 */
	public final void testProxyTypes() {
		for (ProxyTypes type: ProxyTypes.values()) {
			Assert.assertEquals(type, ProxyTypes.valueOf(type.name()));
		}
	}
}

