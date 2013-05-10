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
package net.docca.backend.web.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.docca.backend.Config;
import net.docca.backend.ocr.ObservablePriorityQueue;
import net.docca.backend.ocr.Prioritized;
import net.docca.backend.persistence.entities.Document;
import net.docca.backend.persistence.entities.Document.DocumentType;
import net.docca.backend.persistence.entities.Tag;
import net.docca.backend.persistence.managers.repositories.DocumentRepository;
import net.docca.backend.persistence.managers.repositories.TagRepository;
import net.docca.backend.web.controllers.forms.UploadImageForm;
import net.docca.backend.web.helpers.UserManager;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import edu.emory.mathcs.backport.java.util.Arrays;
import edu.emory.mathcs.backport.java.util.Collections;

/**
 * controller responsible for uploading and processing image documents.
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Controller
@RequestMapping("/upload")
@PropertySource(Config.DEFAULT_CONFIGURATION)
public class UploadImageController {
	/**
	 * the logger for this class.
	 */
	private static Logger logger = Logger.getLogger(UploadImageController.class);

	/**
	 * the repository used for accessing documents in the database.
	 */
	@Autowired
	private DocumentRepository repository;

	/**
	 * repository for accessing the tags.
	 */
	@Autowired
	private TagRepository tagRepository;

	/**
	 * the ocr queue.
	 */
	@Autowired
	private ObservablePriorityQueue<Prioritized<FileDocumentPair>> queue;

	/**
	 * the environment object injected by spring. used for accessing the configuration variables.
	 */
	@Autowired
	private Environment environment;

	@Autowired
	private UserManager userManager;

	/**
	 * creates a new form object.
	 * @return the new form object
	 */
	@ModelAttribute("uploadForm")
	public final UploadImageForm getForm() {
		return new UploadImageForm();
	}

	/**
	 * shows the upload form.
	 * @return the name of the upload view
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String show() {
		return "upload";
	}

	/**
	 * handles image uploads. basically just copies the uploaded files to a temporary location then adds them to
	 * a queue.
	 *
	 * @param request the request
	 * @param form the form
	 * @param model the model
	 * @return the name of the result view
	 * @throws IOException on any io error
	 */
	@RequestMapping(method = RequestMethod.POST)
	public String process(final HttpServletRequest request,
			@ModelAttribute("uploadForm") final UploadImageForm form, final Model model)
					throws IOException {
		List<String> fileNames = new ArrayList<>();

		logger.debug("processing image uploads");

		if (form.getFiles() != null) {
			File imageDirectory = new File(
					environment.getProperty("permanent.image.directory",
							Config.DEFAULT_IMAGE_DIRECTORY));
			if (!imageDirectory.exists()) {
				imageDirectory.mkdirs();
			}
			if (form.isMerge()) {
				Document persisted = new Document();
				persisted.setTags(parseTags(form.getTags()));
				persisted.setCreatedAt(new Timestamp(System.currentTimeMillis()));
				persisted.setType(DocumentType.PDF);
				persisted.setDescription(form.getDescription());
				persisted.setComment(form.getComment());
				persisted.setUploader(userManager.getCurrentUser(request));
				repository.save(persisted);
				List<Path> paths = new ArrayList<Path>();
				for (MultipartFile file: form.getFiles()) {
					if (file != null) {
						String extension = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
						File permanent = new File(imageDirectory,
								System.currentTimeMillis() + "" + file.getOriginalFilename().hashCode() + "." + extension);
						IOUtils.copy(file.getInputStream(), new FileOutputStream(permanent));
						paths.add(permanent.toPath());
						logger.debug("added [" + file.getOriginalFilename() + "] to the ocr queue");
					}
				}
				queue.add(new Prioritized<FileDocumentPair>(
						new FileDocumentPair(paths, persisted), 0));
			} else {
				for (MultipartFile file: form.getFiles()) {
					if (file != null) {
						String extension = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
						File permanent = new File(imageDirectory,
								System.currentTimeMillis() + "" + file.getOriginalFilename().hashCode() + "." + extension);
						IOUtils.copy(file.getInputStream(), new FileOutputStream(permanent));
						Document persisted = new Document();
						persisted.setTags(parseTags(form.getTags()));
						persisted.setCreatedAt(new Timestamp(System.currentTimeMillis()));
						persisted.setSource(permanent.getAbsolutePath());
						persisted.setType(DocumentType.PDF);
						persisted.setDescription(form.getDescription());
						persisted.setComment(form.getComment());
						persisted.setUploader(userManager.getCurrentUser(request));
						repository.save(persisted);
						queue.add(new Prioritized<FileDocumentPair>(
								new FileDocumentPair(Collections.singletonList(permanent.toPath()), persisted), 0));
						logger.debug("added [" + file.getOriginalFilename() + "] to the ocr queue");
					}
					fileNames.add(file.getOriginalFilename());
				}
			}
		}
		model.addAttribute("names", fileNames);
		return "result";
	}

	private final List<Tag> parseTags(final String tags) {
		List<String> tagNames = Arrays.asList(StringUtils.split(StringUtils.trimToEmpty(tags), ","));
		List<Tag> result = new ArrayList<>();
		for (String tagName: tagNames) {
			String trimmed = StringUtils.trim(tagName);
			Tag tag = tagRepository.findByName(trimmed);
			if (tag == null) {
				tag = new Tag();
				tag.setName(trimmed);
				tagRepository.save(tag);
			}
			result.add(tag);
		}
		return result;
	}
}

