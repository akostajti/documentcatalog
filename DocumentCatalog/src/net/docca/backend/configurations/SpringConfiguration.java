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

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * the general spring configuration class.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Configuration
@ComponentScan("net.docca.backend")
@Import(JpaConfiguration.class)
public class SpringConfiguration {

}

