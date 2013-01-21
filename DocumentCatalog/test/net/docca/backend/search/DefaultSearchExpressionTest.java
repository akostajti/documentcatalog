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

import net.docca.test.util.EqualityAsserts;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests for <code>DefaultSearchExpression</code>.
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Test(groups = {"mustrun", "search" })
public class DefaultSearchExpressionTest {
	/**
	 * tests if the raw representation is returned correctly.
	 */
	public final void testGetRawRepresentation() {
		String[] input = {null,
				"",
				"title: loneliness"
		};

		String[] output = input;

		for (int i = 0; i < input.length; i++) {
			DefaultSearchExpression exp = new DefaultSearchExpression(input[i]);
			Assert.assertEquals(exp.getRawExpression(), output[i]);
		}
	}

	/**
	 * tests the equals method.
	 */
	public final void testEquals() {
		DefaultSearchExpression expression1 = new DefaultSearchExpression("loneliness of");
		EqualityAsserts.assertEqualsAndHashcodeWork(expression1,
				new DefaultSearchExpression("loneliness of"), new  Object[] {
			new DefaultSearchExpression(null), null,
			new DefaultSearchExpression("loneliness")
		});
		Assert.assertEquals(new DefaultSearchExpression(null), new DefaultSearchExpression(null));
	}
}

