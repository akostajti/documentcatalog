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

import javax.validation.Valid;

import net.docca.backend.persistence.entities.User;
import net.docca.backend.web.controllers.forms.RegisterUserForm;
import net.docca.backend.web.helpers.UserManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * handles requests related to user management.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Controller
@RequestMapping("/register")
public class UserController {
	/**
	 * the {@code UserManager} used for managing the users.
	 */
	@Autowired
	private UserManager userManager;

	@ModelAttribute("registerUserForm")
	public RegisterUserForm getForm() {
		return new RegisterUserForm();
	}

	/**
	 * shows the user registration form.
	 * @return the name of the jsp to render
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String showForm() {
		return "register";
	}

	/**
	 * registers a user.
	 * @param form the form
	 * @param errors the errors object
	 * @param model the model
	 * @return the name of the view to render
	 */
	@RequestMapping(method = RequestMethod.POST)
	public String register(@Valid @ModelAttribute("registerUserForm") final RegisterUserForm form, final BindingResult errors, final Model model) {
		if (userManager.userExists(form.getUsername())) {
			errors.rejectValue("username", "create.user.name.reserved");
			return "register";
		}
		User user = userManager.register(form.getUsername(), form.getPassword());
		model.addAttribute("user", user);
		return "user";
	}
}

