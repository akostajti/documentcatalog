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
package net.docca.backend.persistence.entities;

import javax.persistence.Entity;

/**
 * an entity class representing the sources from which the ocr engine can create text.
 * these are typically images.
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Entity
public class Source extends IdentifiableEntity {
	/**
	 * the path of this source.
	 */
	private String path;

	/**
	 * the default constructor.
	 */
	public Source() {

	}

	/**
	 * creates a source object.
	 * @param path the path
	 */
	public Source(final String path) {
		this.path = path;
	}

	/**
	 * getter for path.
	 * @return the path
	 */
	public final String getPath() {
		return path;
	}

	/**
	 * setter for path.
	 * @param path the path to set
	 */
	public final void setPath(final String path) {
		this.path = path;
	}
}

