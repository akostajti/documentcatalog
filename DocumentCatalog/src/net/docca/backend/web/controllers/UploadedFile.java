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

import java.io.Serializable;


/**
 * represents a fie uploaded by blueimp's jquery upload plugin.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
public class UploadedFile implements Serializable {
	/**
	 * the generates serialversionuid.
	 */
	private static final long serialVersionUID = -2772944757584223692L;

	/**
	 * the name of the file.
	 */
	private String name;

	/**
	 * the original name of the file.
	 */
	private String originalName;

	/**
	 * the size of the file.
	 */
	private Integer size;

	/**
	 * the downloadUrl of the file.
	 */
	private String downloadUrl;

	/**
	 * @param name
	 * @param originalFile
	 * @param size
	 * @param downloadUrl
	 */
	public UploadedFile(final String name, final String originalName, final Integer size, final String downloadUrl) {
		this.name = name;
		this.originalName = originalName;
		this.size = size;
		this.downloadUrl = downloadUrl;
	}

	/**
	 * getter for name.
	 * @return the name
	 */
	public final String getName() {
		return name;
	}

	/**
	 * getter for size.
	 * @return the size
	 */
	public final Integer getSize() {
		return size;
	}

	/**
	 * setter for name.
	 * @param name the name to set
	 */
	public final void setName(final String name) {
		this.name = name;
	}

	/**
	 * setter for size.
	 * @param size the size to set
	 */
	public final void setSize(final Integer size) {
		this.size = size;
	}

	/**
	 * getter for originalName.
	 * @return the originalName
	 */
	public final String getOriginalName() {
		return originalName;
	}

	/**
	 * setter for originalName.
	 * @param originalName the originalName to set
	 */
	public final void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	/**
	 * getter for downloadUrl.
	 * @return the downloadUrl
	 */
	public final String getDownloadUrl() {
		return downloadUrl;
	}

	/**
	 * setter for downloadUrl.
	 * @param downloadUrl the downloadUrl to set
	 */
	public final void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
}
