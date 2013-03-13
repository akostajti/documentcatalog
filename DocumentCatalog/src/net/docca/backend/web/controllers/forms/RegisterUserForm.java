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
package net.docca.backend.web.controllers.forms;

import javax.validation.constraints.NotNull;

/**
 * encapsulates all information shown on the user registration form.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
public class RegisterUserForm {
	/**
	 * the name of the user.
	 */
	@NotNull
	private String username;

	/**
	 * the password.
	 */
	@NotNull
	private String password;

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
}

