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
package net.docca.backend.web;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * handles file download. gets a filename from the local filesystem and writes it to the response.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Service
public class DownloadService {

	/**
	 * the logger for this class.
	 */
	private static Logger logger = Logger.getLogger(DownloadService.class);

	/**
	 * gets a path from the local filesystem and adds its content to the response. can recognize the
	 * mime type.
	 *
	 * @param path the file to download
	 * @param response the response
	 * @param context the servlet context
	 * @param setContentDisposition specifies if the content disposition tag must be set.
	 * this controls if the download window of the client shows the filename or not.
	 * @throws IOException thrown on any io error
	 */
	public void processDownload(final Path path, final HttpServletResponse response,
			final ServletContext context, final boolean setContentDisposition) throws IOException {
		logger.debug("downloading [" + path + "]");
		// try to compute the mime type
		String mimeType = context.getMimeType(path.toString());
		if (mimeType == null) {
			mimeType = "application/octet-stream";
		}

		logger.debug("using mime type [" + mimeType + "]");

		response.setContentType(mimeType);
		response.setContentLength((int) path.toFile().length());

		if (setContentDisposition) {
			// set the two headers
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", path.getFileName().toString());
			response.setHeader(headerKey, headerValue);
		}

		Files.copy(path, response.getOutputStream());
	}
}

