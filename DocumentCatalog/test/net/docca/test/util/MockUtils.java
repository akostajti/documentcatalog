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
package net.docca.test.util;

import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;

import net.docca.backend.Config;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;

public class MockUtils {

	/**
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
	public static Configuration getMockedConfiguration() throws NoSuchFieldException,
	IllegalAccessException {
		Configuration configuration = mock(PropertiesConfiguration.class);
		Field field = Config.class.getDeclaredField("configuration");
		field.setAccessible(true);
		field.set(Config.getInstance(), configuration);
		return configuration;
	}
}
