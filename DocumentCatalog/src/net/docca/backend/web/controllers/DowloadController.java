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
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.docca.backend.Config;
import net.docca.backend.web.DownloadService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * controller for handling image downloads.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Controller
@PropertySource(Config.DEFAULT_CONFIGURATION)
public class DowloadController {
	/**
	 * handles the downloads.
	 */
	@Autowired
	private DownloadService downloadService;

	/**
	 * the environment object injected by spring. used for accessing the configuration variables.
	 */
	@Autowired
	private Environment environment;

	/**
	 * writes the file to the response.
	 * @param file the file to download.
	 * @param request the request
	 * @param response the response.
	 * @throws IOException on any io error
	 */
	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public void download(final HttpServletRequest request, final HttpServletResponse response, @RequestParam("file") final String file) throws IOException {
		File imageDirectory = new File(
				environment.getProperty("permanent.image.directory",
						Config.DEFAULT_IMAGE_DIRECTORY));
		if (!imageDirectory.exists()) {
			imageDirectory.mkdirs();
		}

		File subject = new File(imageDirectory, file);
		ServletContext context = request.getSession().getServletContext();
		downloadService.processDownload(subject.toPath(), response, context, false);
	}
}
