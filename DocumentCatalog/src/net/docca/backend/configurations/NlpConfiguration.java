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
	 * the name of the binary model files used by opennlp for finding named entities.
	 */
	private static final String[] nameFinderModelNames = {
		"nlp/models/en-ner-location.bin",
		"nlp/models/en-ner-person.bin",
		"nlp/models/en-ner-date.bin",
		"nlp/models/en-ner-time.bin",
		"nlp/models/en-ner-organization.bin"
	};

	/**
	 * creates the {@code TokenizerModel} bean used by opennlp. Uses a previously trained model file
	 * from {@literal resources/nlp/models}.
	 * @return the initialized model
	 * @throws IOException thrown when the model file was not found or it is invalid
	 */
	@Bean
	public TokenizerModel tokenizerModel() throws IOException {
		InputStream input = this.getClass().getClassLoader().getResourceAsStream("nlp/models/en-token.bin");
		TokenizerModel model = new TokenizerModel(input);
		return model;
	}

	/**
	 * creates the {@code TokenNameFinderModel} beans used by opennlp. Uses a previously trained model file
	 * from {@literal resources/nlp/models}.
	 * @return the initialized model
	 * @throws IOException thrown when the model file was not found or it is invalid
	 */
	@Bean
	public TokenNameFinderModel personModel() throws IOException {
		InputStream input = loadModelFile("nlp/models/en-ner-person.bin");
		TokenNameFinderModel model = new TokenNameFinderModel(input);

		return model;
	}

	@Bean
	public TokenNameFinderModel locationModel() throws IOException {
		InputStream input = loadModelFile("nlp/models/en-ner-location.bin");
		TokenNameFinderModel model = new TokenNameFinderModel(input);

		return model;
	}

	@Bean
	public TokenNameFinderModel organizationModel() throws IOException {
		InputStream input = loadModelFile("nlp/models/en-ner-organization.bin");
		TokenNameFinderModel model = new TokenNameFinderModel(input);

		return model;
	}

	@Bean
	public TokenNameFinderModel dateModel() throws IOException {
		InputStream input = loadModelFile("nlp/models/en-ner-date.bin");
		TokenNameFinderModel model = new TokenNameFinderModel(input);

		return model;
	}

	@Bean
	public TokenNameFinderModel timeModel() throws IOException {
		InputStream input = loadModelFile("nlp/models/en-ner-time.bin");
		TokenNameFinderModel model = new TokenNameFinderModel(input);

		return model;
	}

	/**
	 * loads a model file.
	 * @param fileName
	 * @return
	 */
	private InputStream loadModelFile(final String fileName) {
		InputStream input = this.getClass().getClassLoader().getResourceAsStream(fileName);
		return input;
	}
}
