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

import net.docca.backend.search.lucene.LuceneProxy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * spring configuration class for search related beans.
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Configuration
public class SearchConfiguration {

	/**
	 * returns an instance of <code>LuceneProxy</code>.
	 * @return the single instance of <code>LuceneProxy</code>
	 */
	@Bean
	public LuceneProxy luceneProxy() {
		return LuceneProxy.getInstance();
	}
}

