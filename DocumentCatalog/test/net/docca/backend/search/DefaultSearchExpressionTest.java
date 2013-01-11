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
 * Tests for <code>DefaultSearchExpression</code>.
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Test(groups = {"mustrun", "search" })
public class DefaultSearchExpressionTest {
	public void testGetRawRepresentation() {
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
}

