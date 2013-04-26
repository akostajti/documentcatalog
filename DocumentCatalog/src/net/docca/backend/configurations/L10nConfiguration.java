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

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/**
 * the configuration for spring internationalization.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Configuration
public class L10nConfiguration extends WebMvcConfigurerAdapter {
	/**
	 * the default locale (us english).
	 */
	public static final Locale DEFAULT_LOCALE = new Locale("en", "US");

	/**
	 * the name of the http parameter that defines the locale.
	 */
	public static final String  LOCALE_PARAMETER = "lang";

	/**
	 * the message resource. loads the resources from the classpath.
	 * @return the bean
	 */
	@Bean
	public ReloadableResourceBundleMessageSource messageSource() {
		ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
		source.setBasenames("classpath:messages", "classpath:labels");
		return source;
	}

	/**
	 * this interceptor will notices when the local changes.
	 * @return the bean
	 */
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName(LOCALE_PARAMETER);
		return localeChangeInterceptor;
	}

	/**
	 * resolver for finding out the locale.
	 * @return the bean
	 */
	@Bean
	public CookieLocaleResolver localeResolver() {
		CookieLocaleResolver localeResolver = new CookieLocaleResolver();
		localeResolver.setDefaultLocale(DEFAULT_LOCALE);
		return localeResolver;
	}

	/**
	 * adds the {@code localeChangeInterceptor} bean to {@code registry}.
	 */
	@Override
	public void addInterceptors(final InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}
}
