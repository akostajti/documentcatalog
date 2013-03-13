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

import net.docca.backend.persistence.managers.repositories.DocumentRepository;
import net.docca.backend.web.controllers.forms.UploadImageForm;

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
	 * the repository used for accessing documents in the database.
	 */
	@Autowired
	private DocumentRepository repository;

	/**
	 * shows the upload form.
	 * @return the name of the upload view
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String show() {
		return "upload";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String process(@ModelAttribute("uploadForm") UploadImageForm form, Model model) {
		List<String> fileNames = new ArrayList<>();
		if (form.getFiles() != null) {
			for (MultipartFile file: form.getFiles()) {
				fileNames.add(file.getOriginalFilename());
			}
		}
		model.addAttribute("names", fileNames);
		return "result";
	}
}

