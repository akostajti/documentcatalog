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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.Queue;

import net.docca.backend.persistence.entities.Document;
import net.docca.backend.web.controllers.FileDocumentPair;

import org.mockito.Mockito;
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
		Queue<Prioritized<FileDocumentPair>> queue = OcrQueueFactory.getQueue();
		assertNotNull(queue);
		assertEquals(queue.size(), 0);

		Prioritized<FileDocumentPair> p =
				new Prioritized<FileDocumentPair>(new FileDocumentPair(null, new Document()), 10);
		queue.add(p);

		queue = OcrQueueFactory.getQueue();
		assertEquals(queue.size(), 1);
	}

	/**
	 * checks if the listeners are always notified.
	 */
	@SuppressWarnings("unchecked")
	public final void testListeners() {
		Queue<Prioritized<FileDocumentPair>> queue = OcrQueueFactory.getQueue();
		net.docca.backend.ocr.QueueListener<Prioritized<FileDocumentPair>> listener = mock(QueueListener.class);
		OcrQueueFactory.addQueueListener(listener);

		Prioritized<FileDocumentPair> p1 =
				new Prioritized<FileDocumentPair>(new FileDocumentPair(null, new Document()), 10);
		Prioritized<FileDocumentPair> p2 =
				new Prioritized<FileDocumentPair>(new FileDocumentPair(null, new Document()), 20);

		queue.add(p1);
		queue.add(p2);

		verify(listener).notify(p1);
		verify(listener).notify(p2);

		OcrQueueFactory.removeQueueListener(listener);
		reset(listener);

		queue.remove();
		queue.remove();
		queue.add(p1);
		queue.add(p2);

		verifyNoMoreInteractions(listener);

		// verify that all listeners are notified
		QueueListener<Prioritized<FileDocumentPair>> listener2 = Mockito.mock(QueueListener.class);
		OcrQueueFactory.addQueueListener(listener);
		OcrQueueFactory.addQueueListener(listener2);

		queue.remove();
		queue.remove();
		queue.add(p1);
		verify(listener).notify(p1);

		// test if adding and removing a null listener causes exeption
		OcrQueueFactory.addQueueListener(null);
		queue.add(p1);
		OcrQueueFactory.removeQueueListener(null);
	}
}

