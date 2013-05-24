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
import net.docca.backend.persistence.entities.Source;
import net.docca.backend.persistence.entities.Tag;
import net.docca.backend.persistence.managers.repositories.DocumentRepository;
import net.docca.backend.persistence.managers.repositories.SourceRepository;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import edu.emory.mathcs.backport.java.util.Arrays;
import edu.emory.mathcs.backport.java.util.Collections;

/**
 * controller responsible for uploading and processing image documents.
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Controller
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

	/**
	 * the manager for accessing the users.
	 */
	@Autowired
	private UserManager userManager;

	/**
	 * the manager for accessing the document sources.
	 */
	@Autowired
	private SourceRepository sourceRepository;

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
	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public String show() {
		return "upload/upload";
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
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
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
				Document persisted = createDocumentEntity(request, form);
				repository.save(persisted);
				List<Path> paths = new ArrayList<Path>();
				for (String file: form.getFiles()) {
					Path path = new File(imageDirectory, file).toPath();
					paths.add(path);
					persisted.addSource(sourceRepository.save(new Source(path.toAbsolutePath().toString())));
					logger.debug("added [" + file + "] to the ocr queue");
				}
				queue.add(new Prioritized<FileDocumentPair>(
						new FileDocumentPair(paths, persisted), 0));
			} else {
				for (String file: form.getFiles()) {
					Path path = new File(imageDirectory, file).toPath();
					Document persisted = createDocumentEntity(request, form);
					persisted.addSource(sourceRepository.save(new Source(path.toAbsolutePath().toString())));
					repository.save(persisted);
					queue.add(new Prioritized<FileDocumentPair>(
							new FileDocumentPair(Collections.<Path>singletonList(path), persisted), 0));
					logger.debug("added [" + file + "] to the ocr queue");
					fileNames.add(file);
				}
			}
		}
		model.addAttribute("names", fileNames);
		return "upload/result";
	}

	/**
	 * handles file uploads of jquery file uploader.
	 *
	 * @param files the files
	 * @return the list of files that were uploaded
	 * @throws IOException on any io error
	 */
	@RequestMapping(value="/file", method=RequestMethod.POST)
	@ResponseBody public List<UploadedFile> upload(@RequestParam("files") final List<MultipartFile> files) throws IOException {
		logger.debug("Writing file to disk...done");
		File imageDirectory = new File(
				environment.getProperty("permanent.image.directory",
						Config.DEFAULT_IMAGE_DIRECTORY));
		if (!imageDirectory.exists()) {
			imageDirectory.mkdirs();
		}

		List<UploadedFile> uploadedFiles = new ArrayList<UploadedFile>();
		for (MultipartFile file: files) {
			if (file != null) {
				String extension = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
				String name = System.currentTimeMillis() + "" + file.getOriginalFilename().hashCode() + "." + extension;
				File permanent = new File(imageDirectory, name);
				IOUtils.copy(file.getInputStream(), new FileOutputStream(permanent));
				logger.debug("copied [" + file.getOriginalFilename() + "] to the temporary location");
				uploadedFiles.add(new UploadedFile(name, Integer.valueOf((int) permanent.length())));
			}
		}

		return uploadedFiles;
	}

	/**
	 * creates the document entity.
	 *
	 * @param request the request
	 * @param form the form
	 * @return the document entity initialized with all necessary info
	 */
	private Document createDocumentEntity(final HttpServletRequest request,
			final UploadImageForm form) {
		Document persisted = new Document();
		persisted.setTags(parseTags(form.getTags()));
		persisted.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		persisted.setType(DocumentType.PDF);
		persisted.setDescription(form.getDescription());
		persisted.setComment(form.getComment());
		persisted.setUploader(userManager.getCurrentUser(request));
		return persisted;
	}

	/**
	 * parses the tags from a comma separated list. either returns the existing tags by that name from the db
	 * or creates a new one.
	 * @param tags the comma separated list of tag names.
	 * @return the tag entities
	 */
	private List<Tag> parseTags(final String tags) {
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

