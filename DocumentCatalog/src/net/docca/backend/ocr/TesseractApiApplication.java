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

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

import net.sourceforge.tess4j.Tesseract;

import org.apache.commons.io.IOUtils;

/**
 * <p>
 * <code>OcrApplication</code> implementation that wraps the tesseract api (dll or so).
 * the libraries can be found in the root of the application.
 * </p>
 * <p>
 * doesn't support windows 64 bit. on this platform use <code>TesseractExecutableApplication</code>.
 * </p>
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
public class TesseractApiApplication extends OcrApplication {

	@Override
	public final File run() throws Exception {
		Map<String, String> arguments = getArguments();
		String imagePath = arguments.get(OcrApplication.IMAGE_PATH);
		String outputDirectory = arguments.get(OcrApplication.OUTPUT_DIRECTORY);
		String language = arguments.get(OcrApplication.LANGUAGE);

		File directory = new File(outputDirectory);
		if (!directory.exists()) {
			directory.mkdirs();
		}
		File result = File.createTempFile("hocr", ".html", directory);

		File image = new File(imagePath);
		Tesseract instance = Tesseract.getInstance();
		if (language != null) {
			instance.setLanguage(language);
		}
		// set the output to hocr
		instance.setHocr(true);
		String output = instance.doOCR(image);
		IOUtils.write(output, new FileOutputStream(result));

		return result;
	}
}

