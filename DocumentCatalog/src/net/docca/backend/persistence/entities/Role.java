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
import javax.persistence.OneToOne;

/**
 * represents a role.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Entity
public class Role extends IdentifiableEntity {
	/**
	 * the ordinal of the role.
	 */
	private Integer role;

	/**
	 * a reference to the user in this role.
	 */
	@OneToOne
	private User user;

	/**
	 * getter for role.
	 * @return the role
	 */
	public final Integer getRole() {
		return role;
	}

	/**
	 * setter for role.
	 * @param role the role to set
	 */
	public final void setRole(final Integer role) {
		this.role = role;
	}

	/**
	 * getter for user.
	 * @return the user
	 */
	public final User getUser() {
		return user;
	}

	/**
	 * setter for user.
	 * @param user the user to set
	 */
	public final void setUser(final User user) {
		this.user = user;
	}
}

