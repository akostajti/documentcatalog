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
package net.docca.backend.persistence.managers;

import net.docca.backend.persistence.entities.User;
import net.docca.backend.persistence.managers.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Service
public class UserService extends AbstractEntityService<User> {
	/**
	 * the repository used for accessing the users.
	 */
	@Autowired
	private UserRepository repository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * encodes the password of the user with the default password encoder.
	 */
	@Override
	public final User beforeSave(final User entity) {
		User user = super.beforeSave(entity);
		if (user.getPassword() != null) {
			// generate a salt and encode a password
			String encoded = passwordEncoder.encode(user.getPassword());
			user.setPassword(encoded);
		}
		return user;
	}

	/**
	 * finds a user by username.
	 * @param username the name
	 * @return the user with that name (if exists)
	 */
	public final User findByUsername(final String username) {
		return repository.findByUsername(username);
	}

	@Override
	public final JpaRepository<User, Long> getRepository() {
		return repository;
	}
}

