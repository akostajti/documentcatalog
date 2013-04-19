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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;

/**
 * Detects the language of a text.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Component
public class LanguageDetector {
	/**
	 * then name of the unknown language. returned by the language-detection library
	 * when the language of a text couldn't be detected.
	 */
	public static final String UNKNOWN_LANGUAGE = "unknown";

	/**
	 * the logger for this class.
	 */
	private static Logger logger = Logger.getLogger(LanguageDetector.class);

	/**
	 * the iso names of the supported languages.
	 */
	private static final String[] SUPPORTED_LANGUAGES = {
		"af", "cs", "en", "fi", "hi", "it", "lt", "ml", "no", "ro", "so", "sw", "tl", "vi",
		"ar", "da", "es", "fr", "hr", "ja", "lv", "mr", "pa", "ru", "sq", "ta", "tr", "zh-cn",
		"bg", "de", "et", "gu", "hu", "kn", "ne", "pl", "sk", "te", "uk", "zh-tw",
		"bn", "el", "fa", "he", "id", "ko", "mk", "nl", "pt", "sl", "sv", "th", "ur"
	};

	/**
	 * the directory of the profile files. must be on the classpath.
	 */
	private static final String PROFILE_DIRECTORY = "profiles";

	/**
	 * called after the bean was constructed. initializes the {@code DetectorFactory}.
	 * @throws IOException on any io error
	 * @throws LangDetectException on any error when loading the profiles
	 */
	@PostConstruct
	public void init() throws IOException, LangDetectException {
		DetectorFactory.loadProfile(loadProfiles());
	}

	/**
	 * gets the language of a text. the language is defined by its iso code.
	 * @param text the text to analyze
	 * @return the iso code of the language or {@code null} if it couldn't be detected.
	 * @throws LangDetectException on any error when creating the detector
	 */
	public String getLanguage(final String text) throws LangDetectException {
		Detector detector = DetectorFactory.create();
		detector.append(text);
		String language;
		try {
			language = detector.detect();
			logger.debug("detected language as [" + language + "]");
			if (!UNKNOWN_LANGUAGE.equals(language)) {
				return language;
			}
		} catch (LangDetectException e) {
			logger.debug("couldn't detect language of the text", e);
		}
		return null;
	}

	/**
	 * loads the language profiles.
	 * @return returns the list of loaded language profiles.
	 * @throws IOException thrown when the profiles were not found.
	 */
	private List<String> loadProfiles() throws IOException {
		List<String> profiles = new ArrayList<>();
		for (String language: SUPPORTED_LANGUAGES) {
			String file = String.format("%s/%s", PROFILE_DIRECTORY, language);
			InputStream in = this.getClass().getClassLoader().getResourceAsStream(file);
			if (in != null) {
				profiles.add(IOUtils.toString(in));
			}
		}
		return profiles;
	}


}
