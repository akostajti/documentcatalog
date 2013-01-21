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
package net.docca.backend.search.lucene;

import net.docca.backend.search.AbstractSearchProxy;
import net.docca.backend.search.ProxyTypes;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * <p>
 * a quartz job that periodically reopens the lucene index searcher if neccessary.
 * see the documentation of <code>SearcherManager</code> for details.
 * </p>
 * <p>
 * this is an interruptible job. execute stores the currently executing thread and <code>interrupt()</code>
 * interrupts this thread.
 * </p>
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
public class ReopenSearcherJob implements Job {
	/**
	 * the name of the job.
	 */
	public static final String JOB_NAME = "refreshIndexJob";
	/**
	 * group of the job.
	 */
	public static final String GROUP_NAME = "search";

	/**
	 * public constructor needed by quartz.
	 */
	public ReopenSearcherJob() {

	}

	/**
	 * refreshes the searcher manager if necessary.
	 */
	private void refreshSearcherManager() {
		LuceneProxy proxy = (LuceneProxy) AbstractSearchProxy.getSearchProxyForType(ProxyTypes.lucene);
		if (proxy != null) {
			proxy.refreshSearcherManager();
		}
	}

	/**
	 * reopens the index searcher manager.
	 * @param context the job context
	 * @throws JobExecutionException any kind of quartz exception triggers this
	 */
	@Override
	public final void execute(final JobExecutionContext context)
			throws JobExecutionException {
		refreshSearcherManager();
	}
}

