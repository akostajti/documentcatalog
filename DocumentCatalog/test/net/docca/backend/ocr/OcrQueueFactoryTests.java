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

import java.nio.file.Path;
import java.util.Queue;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * tests for <code>OcrQueueFactory</code>.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Test(groups = {"ocr", "mustrun" })
public class OcrQueueFactoryTests {
	/**
	 * checks if the queue is always returned (and always the same instance).
	 */
	public final void testGetQueue() {
		Queue<Prioritized<Path>> queue = OcrQueueFactory.getQueue();
		Assert.assertNotNull(queue);
		Assert.assertEquals(queue.size(), 0);

		Prioritized<Path> p = new Prioritized<Path>(null, 10);
		queue.add(p);

		queue = OcrQueueFactory.getQueue();
		Assert.assertEquals(queue.size(), 1);
	}
}

