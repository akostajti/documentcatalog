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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import net.docca.backend.ocr.ObservablePriorityQueue;
import net.docca.backend.ocr.Prioritized;
import net.docca.backend.persistence.entities.Document;
import net.docca.backend.persistence.entities.Document.DocumentType;
import net.docca.backend.persistence.managers.repositories.DocumentRepository;
import net.docca.backend.web.controllers.forms.UploadImageForm;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

/**
 * controller responsible for uploading and processing image documents.
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Controller
@RequestMapping("/upload")
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
	 * the ocr queue.
	 */
	@Autowired
	private ObservablePriorityQueue<Prioritized<FileDocumentPair>> queue;

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
	 * @param form the form
	 * @param model the model
	 * @return the name of the result view
	 * @throws FileNotFoundException if the file was not found
	 * @throws IOException on any io error
	 */
	@RequestMapping(method = RequestMethod.POST)
	public String process(@ModelAttribute("uploadForm") final UploadImageForm form, final Model model) throws FileNotFoundException, IOException {
		List<String> fileNames = new ArrayList<>();

		logger.debug("processing image uploads");

		if (form.getFiles() != null) {
			for (MultipartFile file: form.getFiles()) {
				if (file != null) {
					File temporary = File.createTempFile(file.getOriginalFilename(), "");
					IOUtils.copy(file.getInputStream(), new FileOutputStream(temporary));
					Document persisted = new Document();
					persisted.setCreatedAt(new Timestamp(System.currentTimeMillis()));
					persisted.setSource(file.getOriginalFilename());
					persisted.setType(DocumentType.PDF);
					repository.save(persisted);
					queue.add(new Prioritized<FileDocumentPair>(new FileDocumentPair(temporary.toPath(), persisted), 0));
					logger.debug("added [" + file.getOriginalFilename() + "] to the ocr queue");
				}
				fileNames.add(file.getOriginalFilename());
			}
		}
		model.addAttribute("names", fileNames);
		return "result";
	}
}

