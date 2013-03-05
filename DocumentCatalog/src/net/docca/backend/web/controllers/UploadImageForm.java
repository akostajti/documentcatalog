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

import org.springframework.web.multipart.MultipartFile;

/**
 * form class for <code>UploadImageController</code>.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
public class UploadImageForm {
	/**
	 * a comment added by the user.
	 */
	private String description;

	/**
	 * the list of uploaded files.
	 */
	private List<MultipartFile> files;

	/**
	 * returns the description.
	 * @return the decription
	 */
	public final String getDescription() {
		return description;
	}

	/**
	 * sets the description.
	 * @param description the description
	 */
	public final void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * returns the list of the uploaded files.
	 * @return the files
	 */
	public final List<MultipartFile> getFiles() {
		return files;
	}

	/**
	 * sets the file list.
	 * @param files the files
	 */
	public final void setFiles(final List<MultipartFile> files) {
		this.files = files;
	}
}

