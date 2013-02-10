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

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * absract base class for all entities that has a unique id.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@MappedSuperclass
public abstract class IdentifiableEntity {
	/**
	 * the unique id.
	 */
	@Id
	@GeneratedValue
	private Long id;

	/**
	 * @return the id
	 */
	public final Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public final void setId(final Long id) {
		this.id = id;
	}
}

