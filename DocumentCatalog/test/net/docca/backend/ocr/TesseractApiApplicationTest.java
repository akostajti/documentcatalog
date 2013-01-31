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
import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import net.docca.test.util.MockUtils;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.testng.annotations.Test;

/**
 * tests the tessetract api application.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Test(groups = {"tesseract", "mustrun", "ocr" })
public class TesseractApiApplicationTest {

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
		arguments.put(OcrApplication.LANGUAGE, "hun");
		application.setArguments(arguments);

		// run and check the result
		File hocr = application.run();

		String output = FileUtils.readFileToString(hocr);
		// replace the image path to nothing because it will always change and break the hash
		output = output.replaceAll("title='image.*;", "");
		MessageDigest md5 = MessageDigest.getInstance("MD5");

		byte[] hash = md5.digest(output.getBytes());
		assertEquals(hash, EXPECTED_HASH);
	}

}

