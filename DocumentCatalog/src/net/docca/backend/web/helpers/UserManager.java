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
package net.docca.backend.web.helpers;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import net.docca.backend.persistence.entities.Role;
import net.docca.backend.persistence.entities.Role.RoleNames;
import net.docca.backend.persistence.entities.User;
import net.docca.backend.persistence.managers.UserService;
import net.docca.backend.persistence.managers.repositories.RoleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * service for managing users (registration/deletion/etc.).
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Service
public class UserManager {
	/**
	 * {@code UserService} for accessing users in the database.
	 */
	@Autowired
	private UserService userService;

	/**
	 * {@code RoleRepository} for accessing roles in the database.
	 */
	@Autowired
	private RoleRepository roleRepository;

	/**
	 * creates a new user.
	 * @param username the name of the user.
	 * @param password the password.
	 * @return the dto object created after registering the user.
	 */
	public final User register(final String username, final String password) {
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user = userService.save(user);
		// set the default role for the user
		addToDefaultRole(user);
		return user;
	}

	/**
	 * adds {@code user} to the default role.
	 * @param user the user
	 */
	private void addToDefaultRole(final User user) {
		Role defaultRole = new Role();
		defaultRole.setRole(RoleNames.ROLE_USER.ordinal());
		defaultRole.setUser(user);
		roleRepository.save(defaultRole);
		user.setRole(defaultRole);
	}

	/**
	 * checks if a user with the given {@code username} already exists.
	 * @param username the username
	 * @return trivial
	 */
	public final boolean userExists(final String username) {
		User user = userService.findByUsername(username);
		return user != null;
	}

	/**
	 * returns the currently logged in user.
	 * @param request the request
	 * @return the currently logged user or null (should not happen)
	 */
	public final User getCurrentUser(final HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		if (principal != null) {
			String name = principal.getName();
			return userService.findByUsername(name);
		}

		return null;
	}
}

