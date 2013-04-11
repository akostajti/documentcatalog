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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.docca.backend.persistence.entities.NamedEntityTag.Type;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinder;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * helps finding named entities in a text.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Component
public class NamedEntityRecognizer {
	/**
	 * the logger for this class.
	 */
	private static Logger logger = Logger
			.getLogger(NamedEntityRecognizer.class);

	/**
	 * the maxent model used for tokenizing text.
	 */
	@Autowired
	private TokenizerModel tokenizerModel;

	/**
	 * the maxent model used for finding named entities.
	 */
	@Autowired
	private List<TokenNameFinderModel> tokenNameFinderModels;

	/**
	 * finds all named entities in {@code text}.
	 * @param text the text to search for the named entities
	 * @return the named entities found
	 */
	public Set<NamedEntity> recognize(final String text) {
		// first tokenize the text
		Tokenizer tokenizer = new TokenizerME(tokenizerModel);
		String[] tokens = tokenizer.tokenize(text);

		// then find the entities
		Set<NamedEntity> result = new HashSet<>();
		for (TokenNameFinderModel model: tokenNameFinderModels) {
			TokenNameFinder nameFinder = new NameFinderME(model);
			Span[] spans = nameFinder.find(tokens);
			for (Span span: spans) {
				String name = getName(span, tokens);
				result.add(new NamedEntity(name, Type.valueOf(StringUtils.capitalize(span.getType()))));
			}
		}

		return result;
	}

	private String getName(Span span, String[] tokens) {
		StringBuilder nameBuilder = new StringBuilder();
		for (int i = span.getStart(); i < span.getEnd(); i++) {
			nameBuilder.append(tokens[i]).append(" ");
		}
		return nameBuilder.toString();
	}
}
