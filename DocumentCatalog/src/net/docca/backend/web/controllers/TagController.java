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

import java.util.ArrayList;
import java.util.List;

import net.docca.backend.persistence.entities.Document;
import net.docca.backend.persistence.entities.Tag;
import net.docca.backend.persistence.managers.repositories.DocumentRepository;
import net.docca.backend.persistence.managers.repositories.TagRepository;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * controller for handling tag related operations.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Controller
public class TagController {
	/**
	 * the logger for this class.
	 */
	private static Logger logger = Logger.getLogger(TagController.class);

	/**
	 * repository for accessing tags.
	 */
	@Autowired
	private TagRepository tagRepository;

	/**
	 * repository for accessing documents.
	 */
	@Autowired
	private DocumentRepository documentRepository;

	/**
	 * lists all documents tagged with {@code tagName}.
	 * @param tagName the name of the tag
	 * @param model the model
	 * @return the name of the view to render
	 */
	@RequestMapping(value = "/tags/{tagName}", method = RequestMethod.GET)
	public String listDocuments(@PathVariable final String tagName, final Model model) {
		logger.info("listing documents with tag [" + tagName + "]");
		List<Document> documents = new ArrayList<>();

		Tag tag = tagRepository.findByName(tagName);
		if (tag != null) {
			logger.debug("tag found [" + tag + "]");
			documents = documentRepository.findByTag(tag);
			model.addAttribute(tag);
		}
		model.addAttribute("documents", documents);

		return "documents/tags";
	}
}

