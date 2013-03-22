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
	 * the path.
	 */
	private final Path path;

	/**
	 * the document.
	 */
	private final Document document;

	/**
	 * creates the immutable instance.
	 * @param path the path
	 * @param document the document
	 */
	public FileDocumentPair(final Path path, final Document document) {
		super();
		this.path = path;
		this.document = document;
	}

	/**
	 * getter for path.
	 * @return the path
	 */
	public final Path getPath() {
		return path;
	}

	/**
	 * getter for document.
	 * @return the document
	 */
	public final Document getDocument() {
		return document;
	}
}
