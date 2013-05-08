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

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.docca.backend.persistence.entities.Document;
import net.docca.backend.persistence.entities.User;
import net.docca.backend.persistence.managers.repositories.DocumentRepository;
import net.docca.backend.web.helpers.UserManager;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * the controller generating the content of the user home.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Controller
public class HomePageController {
	/**
	 * the logger for this class.
	 */
	private static Logger logger = Logger.getLogger(HomePageController.class);

	/**
	 * the injected user manager instance.
	 */
	@Autowired
	private UserManager userManager;

	/**
	 * the repository for accessing documents.
	 */
	@Autowired
	private DocumentRepository documentRepository;

	/**
	 * generates the user home page.
	 * @param request the request
	 * @param model the model
	 * @return the name of the jsp page to use
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public final String show(final HttpServletRequest request, final Model model) {
		User user = userManager.getCurrentUser(request);

		logger.info("generating home page for [" + user + "]");

		List<Document> documents = documentRepository.findByUploader(user);

		model.addAttribute("documents", documents);
		model.addAttribute("user", user);

		return "home";
	}
}

