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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.docca.backend.persistence.entities.Document;
import net.docca.backend.persistence.managers.DocumentService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * controller for handling all type of operations regarding documents.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Controller
public class DocumentController {
	/**
	 * logger for this class.
	 */
	private static Logger logger = Logger.getLogger(DocumentController.class);

	/**
	 * used for finding and altering the documents.
	 */
	@Autowired
	private DocumentService documentService;

	/**
	 * shows the details of a document.
	 * @param documentId the id of the document
	 * @param model the model
	 * @return the name of the view to render
	 */
	@RequestMapping(value = "/document/{documentId}", method = RequestMethod.GET)
	public String showDocument(@PathVariable final Long documentId, final Model model) {
		Document document = documentService.find(documentId);
		model.addAttribute(document);

		logger.info("showing document [" + document + "]");

		return "documents/document";
	}

	/**
	 * downloads a document.
	 *
	 * @param documentId the id of the document to download
	 * @param request the request
	 * @param response the response
	 * @throws IOException on any io error during the copying of the file to the output stream of the response
	 */
	@RequestMapping(value = "/document/{documentId}/download", method = RequestMethod.GET)
	public void download(@PathVariable final Long documentId, final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {
		Document document = documentService.find(documentId);
		logger.info("downloading document [" + document + "]");

		ServletContext context = request.getSession().getServletContext();

		// the path to the file to download
		Path path = Paths.get(document.getPath());
		logger.debug("downloading [" + path + "]");

		// try to compute the mime type
		String mimeType = context.getMimeType(path.toString());
		if (mimeType == null) {
			mimeType = "application/octet-stream";
		}

		logger.debug("using mime type [" + mimeType + "]");

		response.setContentType(mimeType);
		response.setContentLength((int) path.toFile().length());

		// set the two headers
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", path.getFileName().toString());
		response.setHeader(headerKey, headerValue);

		Files.copy(path, response.getOutputStream());
	}
}

