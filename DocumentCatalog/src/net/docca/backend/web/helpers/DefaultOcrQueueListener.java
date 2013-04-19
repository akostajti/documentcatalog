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
package net.docca.backend.web.helpers;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.docca.backend.Config;
import net.docca.backend.convert.hocr.HocrDocument;
import net.docca.backend.convert.hocr.HocrParser;
import net.docca.backend.convert.hocr.HocrToPdfConverter;
import net.docca.backend.convert.hocr.elements.Page;
import net.docca.backend.nlp.LanguageDetector;
import net.docca.backend.nlp.NamedEntity;
import net.docca.backend.nlp.NamedEntityRecognizer;
import net.docca.backend.ocr.OcrApplication;
import net.docca.backend.ocr.OcrApplicationManager;
import net.docca.backend.ocr.Prioritized;
import net.docca.backend.ocr.QueueListener;
import net.docca.backend.persistence.entities.Document;
import net.docca.backend.persistence.entities.NamedEntityTag;
import net.docca.backend.persistence.managers.DocumentService;
import net.docca.backend.persistence.managers.repositories.NamedEntityTagRepository;
import net.docca.backend.search.AbstractSearchProxy;
import net.docca.backend.search.CompositeIndexable;
import net.docca.backend.search.IndexedProperty;
import net.docca.backend.search.IndexedProperty.Stored;
import net.docca.backend.search.ProxyTypes;
import net.docca.backend.search.SearchProxy;
import net.docca.backend.web.controllers.FileDocumentPair;
import net.sf.classifier4J.summariser.SimpleSummariser;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

/**
 * the default listener for the ocr queue. this is always added to the queue.
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Component
@PropertySource(Config.DEFAULT_CONFIGURATION)
public class DefaultOcrQueueListener implements QueueListener<Prioritized<FileDocumentPair>> {
	/**
	 * the logger for the class.
	 */
	private static Logger logger = Logger.getLogger(DefaultOcrQueueListener.class);

	/**
	 * the ocr application manager instance for getting an {@code OcrApplication} instance.
	 */
	@Autowired
	private OcrApplicationManager ocrApplicationManager;

	/**
	 * the service used for creating new document dtos.
	 */
	@Autowired
	private DocumentService documentService;

	/**
	 * we will use this for running the notification code asynchronously.
	 */
	@Autowired
	private TaskExecutor taskExecutor;

	/**
	 * the environment object injected by spring. used for accessing the configuration variables.
	 */
	@Autowired
	private Environment environment;

	/**
	 * the service for recognizing named entities.
	 */
	@Autowired
	private NamedEntityRecognizer namedEntityRecognizer;

	/**
	 * repository for storing named entity tags parsed from the text.
	 */
	@Autowired
	private NamedEntityTagRepository namedEntityTagRepository;

	/**
	 * component used for detecting the language of texts.
	 */
	@Autowired
	private LanguageDetector languageDetector;

	/**
	 * contains the arguments that are the same for every run of the ocr application.
	 */
	private final Map<String, String> commonArguments = new HashMap<>();

	/**
	 * constructor for initializing the listener.
	 */
	public DefaultOcrQueueListener() {
		this.commonArguments.put(OcrApplication.LANGUAGE, "hun");
		this.commonArguments.put(OcrApplication.OUTPUT_DIRECTORY, "d:\\ocr-output");
	}

	@Override
	public void notify(final Prioritized<FileDocumentPair> pair) {
		taskExecutor.execute(new Runnable() {

			@Override
			public void run() {
				OcrApplication application;
				try {
					application = ocrApplicationManager.findOcrApplication();
					Map<String, String> arguments = new HashMap<>(commonArguments);
					arguments.put(OcrApplication.IMAGE_PATH,
							pair.getSubject().getPath().toString());
					application.setArguments(arguments);
					File hocr = application.run();

					// convert to pdf if the output directory is defined
					String pdfDirectory = environment.getProperty("ocr.convert.pdf.directory",
							Config.DEFAULT_PDF_DIRECTORY);
					if (pdfDirectory != null) {
						File pdf = File.createTempFile("generated",
								".pdf", new File(pdfDirectory));
						HocrParser parser = new HocrParser(hocr);
						HocrDocument document = parser.parse();
						Document persisted = pair.getSubject().getDocument();
						CompositeIndexable composite = new CompositeIndexable(
								persisted.getId().intValue(), document);
						composite.addProperty("description", new IndexedProperty(
								persisted.getDescription(),
								String.class, Stored.Stored));
						composite.addProperty("comment", new IndexedProperty(
								persisted.getComment(),
								String.class, Stored.Stored));
						HocrToPdfConverter converter = new HocrToPdfConverter();
						converter.convertToPdf(document, new FileOutputStream(pdf));
						document.setId(persisted.getId().intValue());

						// find out the language of the text
						String language = languageDetector.getLanguage(StringUtils.abbreviate(
								document.getPages().get(0).getTextContent(), 300));
						persisted.setLanguage(language);

						// store the path of the pdf to the dto
						persisted.setPath(pdf.getAbsolutePath());

						Set<NamedEntity> entities = new HashSet<>();
						StringBuilder builder = new StringBuilder();
						for (Page page: document.getPages()) {
							builder.append(page.getTextContent()).append(" ");
						}
						entities.addAll(namedEntityRecognizer.recognize(builder.toString()));
						persisted.setNamedEntities(getNamedEntities(entities));

						logger.info(entities);

						// compute the summary
						SimpleSummariser summarizer = new SimpleSummariser();
						String summary = summarizer.summarise(builder.toString(), 5);
						persisted.setGeneratedSummary(StringUtils.abbreviate(summary, 200));

						documentService.save(persisted);

						SearchProxy proxy = AbstractSearchProxy
								.getSearchProxyForType(ProxyTypes.lucene);
						proxy.index(composite);

					}
				} catch (Exception e) {
					logger.error("couldn't add path to the ocr queue ["
							+ pair.getSubject() + "]", e);
				}
			}

			private Set<NamedEntityTag> getNamedEntities(final Set<NamedEntity> entities) {
				Set<NamedEntityTag> result = new HashSet<>();

				for (NamedEntity entity: entities) {
					NamedEntityTag dto = namedEntityTagRepository.findByName(entity.getName());
					if (dto == null) {
						dto = new NamedEntityTag(entity.getName(), entity.getType());
						namedEntityTagRepository.save(dto);
					}
					result.add(dto);
				}

				return result;
			}
		});
	}
}

