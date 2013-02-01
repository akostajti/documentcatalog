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

import static org.mockito.Mockito.when;
import static org.testng.Assert.fail;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.docca.backend.Config;
import net.docca.test.util.MockUtils;

import org.apache.commons.configuration.Configuration;
import org.testng.annotations.Test;

/**
 * tests the tessetract api application.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Test(groups = {"tesseract", "mustrun", "ocr" })
public class TesseractApiApplicationTest extends AbstractTesseractApplicationTest {

	/**
	 * the expected md5 hash of the result.
	 */
	private static final byte[] EXPECTED_HASH = new byte[]
			{63, 64, -5, 2, -13, 101, 95, -19, 65, 64, -96, -107, -35, 63, -2, 49};

	/**
	 * runs the application and checks if the result is correct.
	 * @throws Exception on any exception
	 */
	public final void testRun() throws Exception {
		Configuration configuration = MockUtils.getMockedConfiguration();
		when(configuration.getString("ocr.application"))
		.thenReturn(TesseractApiApplication.class.getName());

		TesseractApiApplication application = (TesseractApiApplication) OcrApplicationManager
				.getInstance().findOcrApplication();
		String imagePath = "hocr/advertisement.jpg";
		setupAplication(application, imagePath);

		// run and check the result
		File hocr = application.run();

		checkHocrFileHash(hocr, EXPECTED_HASH);
	}

	/**
	 * tests the run method with invalid arguments.
	 * @throws Exception on any exception.
	 */
	public final void testInvalidArguments() throws Exception {
		Configuration configuration = MockUtils.getMockedConfiguration();
		when(configuration.getString("ocr.application"))
		.thenReturn(TesseractApiApplication.class.getName());

		TesseractApiApplication application = (TesseractApiApplication) OcrApplicationManager
				.getInstance().findOcrApplication();

		// run with null arguments
		try {
			application.run();
			fail("an exception must have been thrown");
		} catch (Exception ex) {}

		Map<String, String> arguments = new HashMap<String, String>();
		File outputDir = File.createTempFile("test", "dir");
		outputDir.delete();
		outputDir.mkdirs();
		arguments.put(OcrApplication.IMAGE_PATH, "invalid.image.path");
		arguments.put(OcrApplication.OUTPUT_DIRECTORY, outputDir.getAbsolutePath());
		application.setArguments(arguments);
		application.setConfig(Config.getInstance());

		try {
			application.run();
			fail("an exception must have been thrown");
		} catch (Exception ex) {}

		// test with invalid output dir
		arguments.put(OcrApplication.IMAGE_PATH, "hocr/advertisement.jpg");
		arguments.put(OcrApplication.OUTPUT_DIRECTORY, "invalid.dir");
	}
}

