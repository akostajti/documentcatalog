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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.TokenizerModel;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * configuration class for NLP related beans.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Configuration
public class NlpConfiguration {

	/**
	 * creates the {@code TokenizerModel} bean used by opennlp. Uses a previously trained model file
	 * from {@literal resources/nlp/models}.
	 * @return the initialized model
	 * @throws IOException thrown when the model file was not found or it is invalid
	 */
	@Bean
	public TokenizerModel tokenizerModel() throws IOException {
		InputStream input = new FileInputStream("resources/nlp/models/en-token");
		TokenizerModel model = new TokenizerModel(input);
		return model;
	}

	/**
	 * creates the {@code TokenNameFinderModel} bean used by opennlp. Uses a previously trained model file
	 * from {@literal resources/nlp/models}.
	 * @return the initialized model
	 * @throws IOException thrown when the model file was not found or it is invalid
	 */
	@Bean
	public TokenNameFinderModel tokenNameFinderModel() throws IOException {
		InputStream input = new FileInputStream("resources/nlp/models/en-ner-person");
		TokenNameFinderModel model = new TokenNameFinderModel(input);
		return model;
	}
}
