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

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 * abstract base class of all tesseract application tests.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
public abstract class AbstractTesseractApplicationTest {

	/**
	 * check if the md5 hash of the generated hocr file matches the expected one.
	 *
	 * @param hocr the hocr file
	 * @param expectedHash the expected hash
	 * @throws IOException thrown on any error with the file
	 * @throws NoSuchAlgorithmException thrown when the md5 algorithm is not found
	 */
	protected final void checkHocrFileHash(final File hocr, final byte[] expectedHash)
			throws IOException, NoSuchAlgorithmException {
		String output = FileUtils.readFileToString(hocr);
		// replace the image path to nothing because it will always change and break the hash
		output = output.replaceAll("title='image.*;", "");
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		byte[] hash = md5.digest(output.getBytes());
		assertEquals(hash, expectedHash);
	}

	/**
	 * sets up the aplications (arguments, etc.).
	 *
	 * @param application the application to set.
	 * @param imagePath the path to the image.
	 * @throws IOException on io exception
	 */
	protected final void setupAplication(final OcrApplication application, final String imagePath)
			throws IOException {
		InputStream imageStream = this.getClass().getClassLoader()
				.getResourceAsStream(imagePath);
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
	}
}
