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
	private static final ObservablePriorityQueue<Prioritized<Path>> QUEUE = new ObservablePriorityQueue<>();

	/**
	 * returns the queue instance.
	 * @return the shared queue.
	 */
	public static Queue<Prioritized<Path>> getQueue() {
		return QUEUE;
	}

	/**
	 * adds a listener to the queue.
	 * @param listener will be notified whenever the <code>offer()</code> or <code>add()</code>
	 * method was invoked on the queue.
	 */
	public static void addQueueListener(final QueueListener<Prioritized<Path>> listener) {
		QUEUE.addListener(listener);
	}

	/**
	 * removes a listener.
	 * @param listener the listener to remove
	 */
	public static void removeQueueListener(final QueueListener<Prioritized<Path>> listener) {
		QUEUE.removeListener(listener);
	}

	/**
	 * a listener for queue add opertations.
	 * @author Akos Tajti <akos.tajti@gmail.com>
	 *
	 * @param <T>
	 */
	public interface QueueListener<T> {
		/**
		 * called when a new item was added to the observed queue.
		 * @param subject the added item.
		 */
		void notify(final T subject);
	}
}

