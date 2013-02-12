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
import java.io.FileOutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import net.docca.backend.convert.hocr.HocrDocument;
import net.docca.backend.convert.hocr.HocrParser;
import net.docca.backend.convert.hocr.HocrToPdfConverter;
import net.docca.backend.ocr.OcrApplication;
import net.docca.backend.ocr.OcrApplicationManager;
import net.docca.backend.ocr.OcrQueueFactory;
import net.docca.backend.ocr.OcrQueueFactory.QueueListener;
import net.docca.backend.ocr.Prioritized;
import net.docca.backend.util.filesystem.DirectoryListener;
import net.docca.backend.util.filesystem.DirectoryWatcher;

import org.apache.commons.io.FileUtils;

/**
 * the main class of the application (started when running on desktop).
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
public final class Main {
	/**
	 * hidden private constructor.
	 */
	private Main() {

	}

	/**
	 * the main method.
	 * @param args arguments
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 */
	public static void main(final String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		Main main = new Main();
		main.startApplication();
	}

	/**
	 * starts the application.
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 */
	private void startApplication() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		// instantiate the ocr application
		final Map<String, String> commonArguments = new HashMap<>();
		commonArguments.put(OcrApplication.LANGUAGE, "hun");
		commonArguments.put(OcrApplication.OUTPUT_DIRECTORY, "d:\\ocr-output");

		// create a directory listener
		DirectoryListener directoryListener = new DirectoryListener() {
			@Override
			public void notify(final Path path) {
				OcrQueueFactory.getQueue().add(new Prioritized<Path>(path, 1));
			}
		};

		// find the watched directory
		String watchedDirName = Config.getInstance().getProperty("ocr.watched.directory");
		final Path watchedDirectory = FileSystems.getDefault().getPath(watchedDirName);

		// create the directory watcher
		DirectoryWatcher watcher = new DirectoryWatcher(watchedDirectory);
		watcher.addListener(directoryListener);

		// create a queue listener
		QueueListener<Prioritized<Path>> queueListener = new QueueListener<Prioritized<Path>>() {
			@Override
			public void notify(final Prioritized<Path> subject) {
				OcrApplication application;
				try {
					application = OcrApplicationManager.getInstance().findOcrApplication();
					Map<String, String> arguments = new HashMap<>(commonArguments);
					Path path = FileSystems.getDefault().getPath(watchedDirectory.toString(),
							subject.getSubject().toString());
					File temporaryImage = File.createTempFile("tempImage", "");
					FileUtils.copyFile(path.toFile(), temporaryImage);
					arguments.put(OcrApplication.IMAGE_PATH, temporaryImage.getAbsolutePath());
					application.setArguments(arguments);
					File hocr = application.run();

					File pdf = File.createTempFile("generated", ".pdf", new File("d:\\ocr-output"));
					HocrParser parser = new HocrParser(hocr);
					HocrDocument document = parser.parse();
					HocrToPdfConverter converter = new HocrToPdfConverter();
					converter.convertToPdf(document, new FileOutputStream(pdf));
				} catch (ClassNotFoundException | InstantiationException
						| IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.err.println(e);
				}
			}
		};
		OcrQueueFactory.addQueueListener(queueListener);

		watcher.startWatching();
	}
}

