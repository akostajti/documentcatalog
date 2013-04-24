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

/**
 * creates a short summary froma  long text. the summary should contain the relevant
 * sentences.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
public interface TextSummarizer {
	/**
	 * creates a short summary for {@code text}.
	 * @param text the text to summarize
	 * @return the summary
	 */
	String summarize(final String text);

	/**
	 * creates a short summary for {@code text} containing at most {@code sentences} sentences.
	 * @param text the text to summarize
	 * @param sentences the maximum number of sentences in the summary
	 * @return the summary
	 */
	String summarize(final String text, final int sentences);
}

