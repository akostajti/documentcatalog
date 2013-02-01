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
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import net.docca.backend.Config;
import net.docca.test.util.MockUtils;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * tests for <code>TesseractApplication</code>.
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
// TODO: activate this test when shipping the tesseract executable with the class is solved
//@Test(groups = {"ocr", "64bit", "tesseract" })
public class TesseractExecutableApplicationTest extends AbstractTesseractApplicationTest {
	/**
	 * the expected hash after processing the test image.
	 */
	private static final byte[] EXPECTED_HASH =
			new byte[]{-50, 15, -97, 1, -51, 69, 35, -99, -94, -93, 117, 96, -113, -65, -25, -102};

	/**
	 * tests if the application is created successfully and all properties are set correctly.
	 * @throws Exception on any exception
	 */
	public final void testSetProperties() throws Exception {
		// test if the properties are set correctly
		TesseractExecutableApplication application = new TesseractExecutableApplication();
		assertNotNull(application.getArguments());
		assertTrue(application.getArguments().isEmpty());
		assertNull(application.getConfig());

		application.setConfig(Config.getInstance());
		assertEquals(application.getConfig(), Config.getInstance());

		Map<String, String> arguments = new HashMap<String, String>();
		arguments.put(OcrApplication.IMAGE_PATH, "/tmp/scanned.jpg");
		application.setArguments(arguments);
		assertEquals(application.getArguments(), arguments);

		// check if the application instance returned by the manager is set up correctly
		Configuration configuration = MockUtils.getMockedConfiguration();
		when(configuration.getString("ocr.application"))
		.thenReturn(TesseractExecutableApplication.class.getName());
		application = (TesseractExecutableApplication) OcrApplicationManager.getInstance().findOcrApplication();
		assertEquals(application.getConfig(), Config.getInstance());
	}

	/**
	 * tests if the application can be run and produces the correct results.
	 * @throws Exception on any exception
	 */
	public final void testRun() throws Exception {
		// set the configuration
		Configuration configuration = MockUtils.getMockedConfiguration();
		when(configuration.getString("ocr.application"))
		.thenReturn(TesseractExecutableApplication.class.getName());
		// set the tesseract location
		when(configuration.getString(TesseractExecutableApplication.TESSERACT_LOCATION_PROPERTY)) // TODO: ship tesseract with the application
		.thenReturn("C:\\Program Files (x86)\\Tesseract-OCR\\tesseract.exe");
		when(configuration.getString(TesseractExecutableApplication.TESSERACT_CONFIG_FILE))
		.thenReturn("C:\\Program Files (x86)\\Tesseract-OCR\\config.txt");

		TesseractExecutableApplication application = (TesseractExecutableApplication) OcrApplicationManager
				.getInstance().findOcrApplication();
		setupAplication(application, "hocr/advertisement.jpg");

		File result = application.run();

		checkHocrFileHash(result, EXPECTED_HASH);
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
		when(configuration.getString("ocr.application"))
		.thenReturn(TesseractExecutableApplication.class.getName());
		OcrApplication application = OcrApplicationManager.getInstance().findOcrApplication();
		assertNotNull(application);
		assertEquals(application.getClass(), TesseractExecutableApplication.class);

		// set the config to invalid tesseract path
		when(configuration.getString(TesseractExecutableApplication
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

