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

import net.docca.backend.Config;

/**
 * <p>
 * a singleton class responsible to finding an <code>OcrApplication</code> implementation to use. the
 * implementation depends on the configuration. it can be any implementation of <code>OcrApplication</code>
 * that is accessible i the classpath.
 * </p>
 * <p>
 * this manager sets the properties (config) of the object returned so they can access the
 * default configuration.
 * </p>
 * <p>
 * 	<strong>all implementing classes must have a visible default constructor</strong>.
 * </p>
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
public final class OcrApplicationManager {

	/**
	 * the default configuration.
	 */
	private final Config config;

	/**
	 * the only instance of this class.
	 */
	private static final OcrApplicationManager INSTANCE = new OcrApplicationManager();

	/**
	 * returns the only instance of this class.
	 * @return INSTANCE
	 */
	public static OcrApplicationManager getInstance() {
		return INSTANCE;
	}

	/**
	 * hidden default constructor.
	 */
	private OcrApplicationManager() {
		config = Config.getInstance();
	}

	/**
	 * find the ocr applicatio implementation based on the config and sets its properties.
	 * @return the ocr implementation found. never <code>null</code>.
	 * @throws ClassNotFoundException if the class specified in the config with key ocr.application then a
	 * ClassNotFoundException is thrown
	 * @throws IllegalAccessException thrown when the class specified in the config couldn't be instantiated
	 * @throws InstantiationException thrown when the class specified in the config couldn't be instantiated
	 */
	public OcrApplication findOcrApplication() throws ClassNotFoundException,
	InstantiationException, IllegalAccessException {
		String applicationClassName = config.getOcrApplication();
		Class<?> applicationClass = Class.forName(applicationClassName);
		if (!OcrApplication.class.isAssignableFrom(applicationClass)) {
			throw new IllegalStateException("class ["
					+ applicationClassName + "] is not an implementation of OcrApplication");
		}

		return (OcrApplication) applicationClass.newInstance();
	}
}

