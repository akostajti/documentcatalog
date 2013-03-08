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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 * represents a user of the application.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Entity
public class User extends IdentifiableEntity {
	/**
	 * the name of the user. unique.
	 */
	@Column(unique = true)
	private String username;

	/**
	 * the password.
	 */
	private String password;

	/**
	 * the first name.
	 */
	private String firstName;

	/**
	 * the last name.
	 */
	private String lastName;

	/**
	 * the role of the user.
	 */
	@OneToOne(mappedBy = "user", cascade = {CascadeType.ALL })
	private Role role;

	/**
	 * getter for username.
	 * @return the username
	 */
	public final String getUsername() {
		return username;
	}

	/**
	 * setter for username.
	 * @param username the username to set
	 */
	public final void setUsername(final String username) {
		this.username = username;
	}

	/**
	 * getter for password.
	 * @return the password
	 */
	public final String getPassword() {
		return password;
	}

	/**
	 * setter for password.
	 * @param password the password to set
	 */
	public final void setPassword(final String password) {
		this.password = password;
	}

	/**
	 * getter for firstName.
	 * @return the firstName
	 */
	public final String getFirstName() {
		return firstName;
	}

	/**
	 * setter for firstName.
	 * @param firstName the firstName to set
	 */
	public final void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	/**
	 * getter for lastName.
	 * @return the lastName
	 */
	public final String getLastName() {
		return lastName;
	}

	/**
	 * setter for lastName.
	 * @param lastName the lastName to set
	 */
	public final void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	/**
	 * getter for role.
	 * @return the role
	 */
	public final Role getRole() {
		return role;
	}

	/**
	 * setter for role.
	 * @param role the role to set
	 */
	public final void setRole(final Role role) {
		this.role = role;
	}
}

