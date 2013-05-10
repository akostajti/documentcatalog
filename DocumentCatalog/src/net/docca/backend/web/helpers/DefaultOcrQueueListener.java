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
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import net.docca.backend.Config;
import net.docca.backend.convert.hocr.HocrDocument;
import net.docca.backend.convert.hocr.HocrParser;
import net.docca.backend.convert.hocr.HocrToPdfConverter;
import net.docca.backend.convert.hocr.elements.Page;
import net.docca.backend.nlp.LanguageDetector;
import net.docca.backend.nlp.NamedEntity;
import net.docca.backend.nlp.NamedEntityRecognizer;
import net.docca.backend.nlp.TextSummarizer;
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
import net.docca.backend.search.indexers.IndexingException;
import net.docca.backend.web.controllers.FileDocumentPair;

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
	 * the size of the text chunk used for language detection.
	 */
	private static final int SAMPLE_SIZE = 300;

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
	 * the txt summarizer component.
	 */
	@Autowired
	private TextSummarizer textSummarizer;

	/**
	 * contains the arguments that are the same for every run of the ocr application.
	 */
	private final Map<String, String> commonArguments = new HashMap<>();

	/**
	 * constructor for initializing the listener.
	 */
	public DefaultOcrQueueListener() {
	}

	/**
	 * sets up the basic properties after the component was created.
	 */
	@PostConstruct
	public final void initialize() {
		commonArguments.put(OcrApplication.LANGUAGE, "hun");
		commonArguments.put(OcrApplication.OUTPUT_DIRECTORY,
				environment.getProperty("ocr.convert.pdf.directory"));
	}

	@Override
	public final void notify(final Prioritized<FileDocumentPair> pair) {
		taskExecutor.execute(new Runnable() {

			@Override
			public void run() {
				OcrApplication application;
				try {
					application = ocrApplicationManager.findOcrApplication();

					HocrDocument mergedDocument = null;
					for (Path path: pair.getSubject().getPaths()) {
						Map<String, String> arguments = new HashMap<>(commonArguments);
						arguments.put(OcrApplication.IMAGE_PATH, path.toString());
						application.setArguments(arguments);
						File hocr = application.run();
						HocrParser parser = new HocrParser(hocr);
						HocrDocument document = parser.parse();
						if (mergedDocument == null) {
							mergedDocument = document;
						} else {
							// if there are multiple images merge the parse results in a single hocr document
							mergedDocument.merge(document);
						}
					}


					// convert to pdf if the output directory is defined
					String pdfDirectory = environment.getProperty("ocr.convert.pdf.directory",
							Config.DEFAULT_PDF_DIRECTORY);
					if (pdfDirectory != null) {
						File pdf = File.createTempFile("generated",
								".pdf", new File(pdfDirectory));

						Document persisted = pair.getSubject().getDocument();
						HocrToPdfConverter converter = new HocrToPdfConverter();
						converter.convertToPdf(mergedDocument, new FileOutputStream(pdf));

						StringBuilder builder = new StringBuilder();
						for (Page page: mergedDocument.getPages()) {
							builder.append(page.getTextContent()).append(" ");
						}
						// find out the language of the text
						String language = languageDetector.getLanguage(StringUtils.abbreviate(
								builder.toString(), SAMPLE_SIZE));

						// find the named entities
						Set<NamedEntity> entities = findNamedEntities(builder.toString());

						// compute the summary
						String summary = textSummarizer.summarize(builder.toString());
						updateDocument(persisted, pdf, language, entities,
								summary);

						indexDocument(mergedDocument, persisted);
					}
				} catch (Exception e) {
					logger.error("couldn't add path to the ocr queue ["
							+ pair.getSubject() + "]", e);
				}
			}
		});
	}

	/**
	 * find tha named entities in {@code text}.
	 *
	 * @param text the text
	 * @return the set of {@code NamedEntity} objects
	 */
	private Set<NamedEntity> findNamedEntities(final String text) {
		Set<NamedEntity> entities = new HashSet<>();
		entities.addAll(namedEntityRecognizer.recognize(text));
		return entities;
	}

	/**
	 * checks if the elements in {@code entities} exists. if yes, then the already existing tag
	 * is put in the result set. otherwise a new tag is created.
	 * @param entities the entities
	 * @return the same entites and in the input
	 */
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

	/** creates a {@code CompositeIndexable} from a {@code Document} and a {@code HocrDocument}.
	 * @param document the hocr document
	 * @param persisted the document persisted to the database
	 * @return the composite indexable object
	 */
	private CompositeIndexable compose(final HocrDocument document, final Document persisted) {
		CompositeIndexable composite = new CompositeIndexable(
				persisted.getId().intValue(), document);
		composite.addProperty("description", new IndexedProperty(
				persisted.getDescription(),
				String.class, Stored.Stored));
		composite.addProperty("comment", new IndexedProperty(
				persisted.getComment(),
				String.class, Stored.Stored));
		return composite;
	}

	/**
	 * sets some fields of the document and stores it to the database.
	 * @param pdf the file from which this document was parsed
	 * @param document the document itself
	 * @param language the language of the document (guessed from the parsed text)
	 * @param entities the named entities found in the parsed text
	 * @param summary the generated summary
	 */
	private void updateDocument(final Document document, final File pdf,
			final String language, final Set<NamedEntity> entities, final String summary) {
		document.setGeneratedSummary(StringUtils.abbreviate(summary,
				Document.MAXIMUM_SUMMARY_LENGTH));
		// store the path of the pdf to the dto
		document.setPath(pdf.getAbsolutePath());
		document.setLanguage(language);
		document.setNamedEntities(getNamedEntities(entities));
		documentService.save(document);
	}

	/** indexes the document.
	 * @param document the hocr document
	 * @param persisted the persisted entity created based on the hocr document
	 * @throws IndexingException thrown on any ndexing error
	 */
	private void indexDocument(final HocrDocument document, final Document persisted)
			throws IndexingException {
		CompositeIndexable composite = compose(document,
				persisted);
		SearchProxy proxy = AbstractSearchProxy
				.getSearchProxyForType(ProxyTypes.lucene);
		document.setId(persisted.getId().intValue());
		proxy.index(composite);
	}
}

