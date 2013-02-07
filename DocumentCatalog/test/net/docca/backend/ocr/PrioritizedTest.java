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
package net.docca.backend.ocr;

import net.docca.test.util.EqualityAsserts;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * tests for <code>Prioritized</code>.
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Test(groups = {"ocr", "mustrun" })
public class PrioritizedTest {
	/**
	 * tests if everything is set correctly.
	 */
	public final void testConstructor() {
		Prioritized<String> p = new Prioritized<String>("test", 10);
		Assert.assertEquals(p.getSubject(), "test");
		Assert.assertEquals(p.getPriority(), 10);

		p = new Prioritized<String>(null, 10);
		Assert.assertNull(p.getSubject());

		p = new Prioritized<String>("test", -20);
		Assert.assertEquals(p.getSubject(), "test");
		Assert.assertEquals(p.getPriority(), -20);
	}

	/**
	 * tests if the equals and the compare methods work properly.
	 */
	public final void testEqualsAndCompare() {
		Prioritized<String> target = new Prioritized<String>("test", 20);
		Prioritized<String> equalObject = new Prioritized<String>("test", 20);

		Object[] notEquals = new Object[] {
				new Prioritized<String>("test", 10),
				new Prioritized<String>("almafa", 20),
				new Prioritized<String>(null, 20),
				new Prioritized<String>("almafa", 50),
				new Prioritized<Integer>(200, 50)
		};
		EqualityAsserts.assertEqualsAndHashcodeWork(target, equalObject, notEquals);
		EqualityAsserts.assertEqualsAndHashcodeWork(new Prioritized<String>(null, 120),
				new Prioritized<String>(null, 120), notEquals);

		Assert.assertEquals(target.compareTo(equalObject), 0);
		Assert.assertTrue(target.compareTo(new Prioritized<String>("test", 30)) < 0);
		Assert.assertTrue(target.compareTo(new Prioritized<String>("test", 1)) > 0);
		Assert.assertTrue(target.compareTo(null) > 0);
	}
}

