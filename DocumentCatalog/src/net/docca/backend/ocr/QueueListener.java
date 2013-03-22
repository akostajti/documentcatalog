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

