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

import net.docca.backend.persistence.entities.User;
import net.docca.backend.persistence.managers.repositories.UserRepository;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * handles the user profile releted urls.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Controller
public class UserProfileController {
	/**
	 * the logger for this class.
	 */
	private static Logger logger = Logger
			.getLogger(UserProfileController.class);

	/**
	 * the repsitory for accessing user objects.
	 */
	@Autowired
	private UserRepository userRepository;

	/**
	 * renders the profile page of the user.
	 *
	 * @param username the name of the user
	 * @param model the model
	 * @return the name of the jsp page
	 */
	@RequestMapping(value = "/profile/{username}", method = RequestMethod.GET)
	public final String showProfile(@PathVariable final String username, final Model model) {
		User user = userRepository.findByUsername(username);
		model.addAttribute("user", user);

		logger.debug("rendering the profile page for [" + user + "]");

		return "profile";
	}
}
