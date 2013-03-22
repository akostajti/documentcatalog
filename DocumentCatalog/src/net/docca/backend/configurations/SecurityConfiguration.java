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
package net.docca.backend.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * the spring security configuration (both for web security and service level security).
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Configuration
@ComponentScan("net.docca.backend")
public class SecurityConfiguration {

	/**
	 * creates a bean used for encoding the passwords in the database.
	 * @return the encoder bean.
	 */
	@Bean(name = {"encoder" })
	public PasswordEncoder encoder() { // TODO: try to use md5 or other encoder which has better performance
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}
}

