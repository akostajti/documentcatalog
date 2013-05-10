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

import java.nio.file.Path;
import java.util.List;

import net.docca.backend.persistence.entities.Document;

/**
 * contains a {@code Path} to an image and a {@code Document} representing the pdf
 * parsed from it.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
public class FileDocumentPair {
	/**
	 * the list of images that are merged together to create a final pdf.
	 */
	private final List<Path> paths;

	/**
	 * the document.
	 */
	private final Document document;

	/**
	 * creates the immutable instance.
	 * @param paths the path
	 * @param document the document
	 */
	public FileDocumentPair(final List<Path> paths, final Document document) {
		super();
		this.paths = paths;
		this.document = document;
	}

	/**
	 * getter for path.
	 * @return the path
	 */
	public final List<Path> getPaths() {
		return paths;
	}

	/**
	 * getter for document.
	 * @return the document
	 */
	public final Document getDocument() {
		return document;
	}
}

