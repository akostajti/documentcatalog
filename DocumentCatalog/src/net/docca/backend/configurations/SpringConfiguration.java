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
package net.docca.backend.configurations;

import net.docca.backend.ocr.ObservablePriorityQueue;
import net.docca.backend.ocr.OcrApplicationManager;
import net.docca.backend.ocr.OcrQueueFactory;
import net.docca.backend.ocr.Prioritized;
import net.docca.backend.web.controllers.FileDocumentPair;
import net.docca.backend.web.helpers.DefaultOcrQueueListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * the general spring configuration class.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Configuration
@ComponentScan("net.docca.backend")
@Import({JpaConfiguration.class, SecurityConfiguration.class, SearchConfiguration.class })
@ImportResource("/WEB-INF/spring-security.xml")
public class SpringConfiguration {

	/**
	 * the default ocr queue listener. this is always added to the queue.
	 */
	@Autowired
	private DefaultOcrQueueListener ocrQueueListener;

	/**
	 * creates the ocr application manager as a bean.
	 * @return the {@code OcrApplcationManager} instance
	 */
	@Bean
	public OcrApplicationManager ocrApplicationManager() {
		return OcrApplicationManager.getInstance();
	}

	/**
	 * registers the ocr queue bean.
	 * @return the queue object
	 */
	@Bean(name = "ocrQueue")
	public ObservablePriorityQueue<Prioritized<FileDocumentPair>> ocrQueue() {
		ObservablePriorityQueue<Prioritized<FileDocumentPair>> queue = (ObservablePriorityQueue<Prioritized<FileDocumentPair>>)
				OcrQueueFactory.getQueue();
		queue.addListener(ocrQueueListener);
		return queue;
	}

	/**
	 * the executor for asynchronous tasks.
	 * @return the executor
	 */
	@Bean
	public TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setMaxPoolSize(10);
		executor.setQueueCapacity(100);
		executor.setWaitForTasksToCompleteOnShutdown(true);
		executor.setCorePoolSize(5);
		return executor;
	}
}

