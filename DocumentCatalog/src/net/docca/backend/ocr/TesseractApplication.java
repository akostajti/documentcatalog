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
import java.util.Map;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.Executor;
import org.apache.commons.lang.StringUtils;

/**
 * <code>OcrApplication</code> implementation that wraps tesseract.
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
public class TesseractApplication extends OcrApplication {
	/**
	 * the name of the configuration property storing the location of the tesseract executable.
	 */
	public static final String TESSERACT_LOCATION_PROPERTY = "ocr.tesseract.location";

	/**
	 * the name of the onfiguation property storing the location of the tesseract config file.
	 */
	public static final String TESSERACT_CONFIG_FILE = "ocr.tesseract.config";

	@Override
	public final File run() throws Exception {

		Map<String, String> arguments = getArguments();
		String imagePath = arguments.get("imagePath");
		String outputDirectory = arguments.get("outputDirectory");
		String language = arguments.get("language");

		File directory = new File(outputDirectory);
		if (!directory.exists()) {
			directory.mkdirs();
		}
		File result = File.createTempFile("hocr", ".html", directory);

		String executable = getConfig().getProperty(TESSERACT_LOCATION_PROPERTY);

		CommandLine command = buildCommand(executable, imagePath, language,
				result);

		Executor executor = new DefaultExecutor();
		int exitCode = executor.execute(command);
		if (exitCode != 0) {
			return null;
		}

		return result;
	}

	/**
	 * builds the tesseract command.
	 *
	 * @param executable the path to tesseract.
	 * @param imagePath the path to the image to be processed.
	 * @param language the language of the text
	 * @param result the output file
	 * @return returns the <code>CommandLine</code> object built from the arguments.
	 */
	private CommandLine buildCommand(final String executable, final String imagePath,
			final String language, final File result) {
		// build the command line
		CommandLine command = new CommandLine(executable);
		command.addArgument(imagePath, true); // set the image
		// set the outputbase
		command.addArgument(StringUtils.substringBefore(result.getAbsolutePath(), ".html"), true);
		// set the language if specified
		if (language != null) {
			command.addArgument("-l");
			command.addArgument(language);
		}

		// set the config file
		String configFileLocation = getConfig().getProperty(TESSERACT_CONFIG_FILE);
		if (configFileLocation != null) {
			command.addArgument(configFileLocation);
		}
		return command;
	}
}


