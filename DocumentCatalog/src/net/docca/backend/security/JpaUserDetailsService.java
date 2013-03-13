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
package net.docca.backend.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.docca.backend.persistence.entities.Role.RoleNames;
import net.docca.backend.persistence.entities.User;
import net.docca.backend.persistence.managers.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * <p>
 * a custom user details service. this will get the user information from the database
 * and update existing users through jpa.
 * </p>
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Service
public class JpaUserDetailsService implements UserDetailsService {
	/**
	 * the user repository.
	 */
	@Autowired
	private UserRepository userRepository;

	@Override
	public final UserDetails loadUserByUsername(final String username) {
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("User with name [" + username + "] not found");
		}

		org.springframework.security.core.userdetails.User result =
				new org.springframework.security.core.userdetails.User(username,
						user.getPassword(),
						getAuthorities(user.getRole().getRole()));
		return result;
	}

	/**
	 * Retrieves a collection of {@link GrantedAuthority} based on a numerical role.
	 * @param role the numerical role
	 * @return a collection of {@link GrantedAuthority
	 */
	public final Collection<? extends GrantedAuthority> getAuthorities(final Integer role) {
		List<GrantedAuthority> authList = getGrantedAuthorities(getRoles(role));
		return authList;
	}

	/**
	 * Converts a numerical role to an equivalent list of roles.
	 * @param role the numerical role
	 * @return list of roles as as a list of {@link String}
	 */
	public final List<String> getRoles(final Integer role) {
		List<String> roles = new ArrayList<String>();
		RoleNames[] roleNames = RoleNames.values();
		roles.add(roleNames[role.intValue()].name());

		return roles;
	}

	/**
	 * Wraps {@link String} roles to {@link SimpleGrantedAuthority} objects.
	 * @param roles {@link String} of roles
	 * @return list of granted authorities
	 */
	public static List<GrantedAuthority> getGrantedAuthorities(final List<String> roles) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for (String role : roles) {
			authorities.add(new SimpleGrantedAuthority(role));
		}
		return authorities;
	}
}
