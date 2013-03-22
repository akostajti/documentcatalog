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

import net.docca.backend.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * the spring mvc configuration.
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Configuration
@EnableWebMvc
@PropertySource(Config.DEFAULT_CONFIGURATION)
@ComponentScan("net.docca.backend.web")
public class MvcConfiguration {
	/**
	 * name of the configuration property storing the maximum upload size.
	 */
	private static final String WEB_MAXIMUM_UPLOAD_SIZE = "web.maximum.upload.size";

	/**
	 * the default maximum upload size. used only if an other value is not specified in the config.
	 */
	private static final Long DEFAULT_MAXIMUM_UPLOAD_SIZE = Long.valueOf(10000000);

	/**
	 * the environment object injected by spring. used for accessing the configuration variables.
	 */
	@Autowired
	private Environment environment;

	/**
	 * creates the multipart resolver bean. this will enable the application to handle
	 * multipart file uploads.
	 * @return the resolver object
	 */
	@Bean
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		resolver.setMaxUploadSize(environment.getProperty(WEB_MAXIMUM_UPLOAD_SIZE,
				Long.class, DEFAULT_MAXIMUM_UPLOAD_SIZE).longValue());
		return resolver;
	}

	/**
	 * creates the jsp resolver bean.  this will map view names to /WEB-INF/views/viewName.jsp.
	 * @return the jsp resolver bean
	 */
	@Bean
	public InternalResourceViewResolver jspResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
		return resolver;
	}
}

