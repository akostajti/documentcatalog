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
package net.docca.backend;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Method;
import java.util.Properties;

import org.apache.commons.configuration.ConfigurationException;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * tests for checking if the configuration is read and handled properly.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Test(groups = {"mustrun", "config" })
public class ConfigTests {
	/**
	 * tests the <code>loadConfiguration()</code> provate method.
	 * @throws Exception any exception
	 */
	public final void testLoadConfiguration() throws Exception {
		Config config = Config.getInstance();
		Method method = Config.class.getDeclaredMethod("loadConfiguration", String.class);
		method.setAccessible(true);

		// test with invalid arguments
		try {
			method.invoke(config, new Object[]{null });
		} catch (Exception e) {
			if (!ConfigurationException.class.isInstance(e.getCause())) {
				Assert.fail("a ConfigurationException must have been thrown ", e);
			}
		}

		try {
			method.invoke(config, "nonexistent.file");
		} catch (Exception e) {
			if (!ConfigurationException.class.isInstance(e.getCause())) {
				Assert.fail("a ConfigurationException must have been thrown");
			}
		}

		// create a valid file and check the properties
		Properties properties = new Properties();
		properties.setProperty("lucene.index.location", "/tmp/lucene");

		File tmp = File.createTempFile("test", ".properties");
		FileWriter writer = new FileWriter(tmp);
		properties.store(writer, null);

		method.invoke(config, tmp.getAbsolutePath());
		Assert.assertEquals(config.getIndexLocation(), properties.get("lucene.index.location"));
	}
}

