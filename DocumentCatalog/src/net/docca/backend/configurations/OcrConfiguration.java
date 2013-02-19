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

import net.docca.backend.ocr.OcrApplication;
import net.docca.backend.ocr.OcrApplicationManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * the ocr configuration class. creates and registers the ocr related beans to spring.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Configuration
public class OcrConfiguration {
	/**
	 * creates and registers the <code>ocrApplication</code> bean.
	 *
	 * @return the ocr application found based on the application config
	 * @throws ClassNotFoundException if the ocr application class was not found
	 * @throws InstantiationException if the ocr application class couldn't be instantiated
	 * @throws IllegalAccessException if the ocr application class couldn't be instantiated
	 */
	@Bean
	public final OcrApplication ocrApplication() throws ClassNotFoundException,
	InstantiationException, IllegalAccessException {
		return OcrApplicationManager.getInstance().findOcrApplication();
	}
}
