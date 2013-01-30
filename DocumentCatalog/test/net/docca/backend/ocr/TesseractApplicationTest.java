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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.fail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import net.docca.backend.Config;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.testng.annotations.Test;

/**
 * tests for <code>TesseractApplication</code>.
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Test(groups = {"ocr", "mustrun", "tesseract" })
public class TesseractApplicationTest {
	/**
	 * the expected hash after processing the test image.
	 */
	private static final byte[] EXPECTED_HASH =
			new byte[]{9, 39, -121, 15, 81, 70, 40, -14, 125, -105, -74, -85, -26, -61, 23, 121};

	/**
	 * tests if the application is created successfully and all properties are set correctly.
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 */
	public final void testSetProperties() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		// test if the properties are set correctly
		TesseractApplication application = new TesseractApplication();
		assertNull(application.getArguments());
		assertNull(application.getConfig());

		application.setConfig(Config.getInstance());
		assertEquals(application.getConfig(), Config.getInstance());

		Map<String, String> arguments = new HashMap<String, String>();
		arguments.put(OcrApplication.IMAGE_PATH, "/tmp/scanned.jpg");
		application.setArguments(arguments);
		assertEquals(application.getArguments(), arguments);

		// check if the application instance returned by the manager is set up correctly
		application = (TesseractApplication) OcrApplicationManager.getInstance().findOcrApplication();
		assertEquals(application.getConfig(), Config.getInstance());
	}

	/**
	 * tests if the application can be run and produces the correct results.
	 * @throws Exception
	 */
	public final void testRun() throws Exception {
		// set the configuration
		Configuration configuration = mock(PropertiesConfiguration.class);
		Field field = Config.class.getDeclaredField("configuration");
		field.setAccessible(true);
		field.set(Config.getInstance(), configuration);
		when(configuration.getString("ocr.application")).thenReturn(TesseractApplication.class.getName());

		TesseractApplication application = (TesseractApplication) OcrApplicationManager
				.getInstance().findOcrApplication();
		InputStream imageStream = this.getClass().getClassLoader()
				.getResourceAsStream("hocr/advertisement.jpg");
		File image = File.createTempFile("test", ".jpg");
		IOUtils.copy(imageStream, new FileOutputStream(image));
		File outputDir = File.createTempFile("test", "dir");
		outputDir.delete();
		outputDir.mkdirs();

		Map<String, String> arguments = new HashMap<String, String>();
		arguments.put(OcrApplication.IMAGE_PATH, image.getAbsolutePath());
		arguments.put(OcrApplication.OUTPUT_DIRECTORY, outputDir.getAbsolutePath());
		arguments.put(OcrApplication.LANGUAGE, "eng");
		application.setArguments(arguments);

		File result = application.run();
		String output = FileUtils.readFileToString(result);
		MessageDigest md5 = MessageDigest.getInstance("MD5");

		byte[] hash = md5.digest(output.getBytes());
		assertEquals(hash, EXPECTED_HASH);
	}

	/**
	 * tests if the application can handle invalid arguments.
	 * @throws Exception on any exception
	 */
	public final void testInvalidArguments() throws Exception {
		Configuration configuration = mock(PropertiesConfiguration.class);
		Field field = Config.class.getDeclaredField("configuration");
		field.setAccessible(true);
		field.set(Config.getInstance(), configuration);
		// change the ocr application property to invalid class
		when(configuration.getString("ocr.application")).thenReturn("in.valid.class");
		try {
			OcrApplicationManager.getInstance().findOcrApplication();
			fail("a class not found exception must have been thrown since an invalid classname was set");
		} catch (ClassNotFoundException ex) {

		}

		// set the correct value
		when(configuration.getString("ocr.application")).thenReturn(TesseractApplication.class.getName());
		OcrApplication application = OcrApplicationManager.getInstance().findOcrApplication();
		assertNotNull(application);
		assertEquals(application.getClass(), TesseractApplication.class);

		// set the config to invalid tesseract path
		when(configuration.getString(TesseractApplication
				.TESSERACT_LOCATION_PROPERTY)).thenReturn("/in/valid/path");

		try {
			application.run();
			fail("File not found exception must have been thrown");
		} catch (Exception ex) {

		}

		// test with ivalid image path
		application.getArguments().put(OcrApplication.IMAGE_PATH, "invalid.image.path");
		try {
			application.run();
			fail("File not found exception must have been thrown");
		} catch (Exception ex) {

		}
	}
}

