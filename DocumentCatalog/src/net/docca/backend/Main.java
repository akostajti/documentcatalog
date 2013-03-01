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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import net.docca.backend.configurations.SpringConfiguration;
import net.docca.backend.convert.hocr.HocrDocument;
import net.docca.backend.convert.hocr.HocrParser;
import net.docca.backend.convert.hocr.HocrToPdfConverter;
import net.docca.backend.ocr.ObservablePriorityQueue;
import net.docca.backend.ocr.OcrApplication;
import net.docca.backend.ocr.OcrApplicationManager;
import net.docca.backend.ocr.OcrQueueFactory.QueueListener;
import net.docca.backend.ocr.Prioritized;
import net.docca.backend.persistence.managers.DocumentService;
import net.docca.backend.util.filesystem.DirectoryListener;
import net.docca.backend.util.filesystem.DirectoryWatcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

/**
 * the main class of the application (started when running on desktop).
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Component
public final class Main {
	@Autowired
	private DocumentService manager;

	@Autowired
	private ObservablePriorityQueue<Prioritized<Path>> queue;

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
		@SuppressWarnings("resource")
		ApplicationContext applicationContext = new	AnnotationConfigApplicationContext(
				SpringConfiguration.class);
		Main main = applicationContext.getBean(Main.class);
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
				queue.add(new Prioritized<Path>(path, 1));
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
					//application = OcrApplicationManager.getInstance().findOcrApplication();
					Map<String, String> arguments = new HashMap<>(commonArguments);
					Path path = FileSystems.getDefault().getPath(watchedDirectory.toString(),
							subject.getSubject().toString());
					// this is necessary on windows otherwise the new file is not found.
					TimeUnit.SECONDS.sleep(1);
					File temporaryImage = File.createTempFile("tempImage", "");
					Files.copy(path, temporaryImage.toPath(), StandardCopyOption.REPLACE_EXISTING);
					arguments.put(OcrApplication.IMAGE_PATH, temporaryImage.getAbsolutePath());
					application = OcrApplicationManager.getInstance().findOcrApplication();
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
		queue.addListener(queueListener);

		watcher.startWatching();
	}
}

