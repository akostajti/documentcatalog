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

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * entity class for tags.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Entity
public class Tag extends IdentifiableEntity {

	/**
	 * the name of the tag.
	 */
	@Column(unique = true, length = 30)
	private String name;

	/**
	 * getter for name.
	 * @return the name
	 */
	public final String getName() {
		return name;
	}

	/**
	 * setter for name.
	 * @param name the name to set
	 */
	public final void setName(final String name) {
		this.name = name;
	}
}

