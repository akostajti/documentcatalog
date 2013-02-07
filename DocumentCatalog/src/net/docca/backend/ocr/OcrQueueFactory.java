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
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * returns the queue object to which the ocr images are added for processing.
 * this is a <code>PriorityQueue</code> implementation that sorts its elements
 * (<code>Prioritized</code> objects) by priority.
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
public abstract class OcrQueueFactory {
	/**
	 * the queue for storing the prioritized objects. these objects all wrap a path to an image to be processed.
	 */
	private static final Queue<Prioritized<Path>> QUEUE = new PriorityQueue<>();

	/**
	 * returns the queue instance.
	 * @return the shared queue.
	 */
	public static Queue<Prioritized<Path>> getQueue() {
		return QUEUE;
	}
}

