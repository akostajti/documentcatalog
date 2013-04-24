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
package net.docca.backend.nlp;

import net.sf.classifier4J.summariser.SimpleSummariser;

import org.springframework.stereotype.Component;

/**
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Component
public class DefaultTextSummarizer implements TextSummarizer {
	/**
	 * the default number of sentences in the generated summary.
	 */
	public static final int DEFAULT_SUMMARY_SIZE = 5;

	/* (non-Javadoc)
	 * @see net.docca.backend.nlp.TextSummarizer#summarize(java.lang.String)
	 */
	@Override
	public String summarize(final String text) {
		return summarize(text, DEFAULT_SUMMARY_SIZE);
	}

	/* (non-Javadoc)
	 * @see net.docca.backend.nlp.TextSummarizer#summarize(java.lang.String, int)
	 */
	@Override
	public String summarize(final String text, final int sentences) {
		SimpleSummariser summarizer = new SimpleSummariser();
		String summary = summarizer.summarise(text, sentences);
		return summary;
	}
}
