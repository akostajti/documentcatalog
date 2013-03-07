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
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

/**
 * the spring security configuration ( both for web security and service level security).
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Configuration
public class SecurityConfiguration {

	/**
	 * creates a bean used for encoding the passwords in the database.
	 * @return the encoder bean.
	 */
	@Bean
	public StandardPasswordEncoder encoder() {
		return new StandardPasswordEncoder();
	}
}

