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
package net.docca.backend;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

/**
 * Stores the configuration for the application. It is a singleton, The instance can be accessed using the
 * <code>getInstance()</code> method.
 *
 * The configuration is read on startup and cannot be modified after.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
public final class Config {
	/**
	 * the logger for the class.
	 */
	private static Logger logger = Logger.getLogger(Config.class);

	/**
	 * the only instance.
	 */
	private static final Config INSTANCE = new Config();

	/**
	 * the apache configuration object read from the configuration file.
	 */
	private Configuration configuration;

	/**
	 * loads the configuration from the default config file.
	 *
	 * @param file the configuration file to load. it is a properties file.
	 * @throws ConfigurationException if loading the configuration failed
	 */
	private void loadConfiguration(final String file) throws ConfigurationException {
		configuration = new PropertiesConfiguration(file);
	}

	/**
	 * returns the only instance of the class.
	 * @return the only instance
	 */
	public static Config getInstance() {
		return INSTANCE;
	}

	/**
	 * hidden constructor.
	 */
	private Config() {
		super();
		try {
			loadConfiguration("settings.properties");
		} catch (ConfigurationException e) {
			logger.error("failed to load the configuration", e);
		}
	}

	/**
	 * returns the lucene index location.
	 * @return the path to the index directory.
	 */
	public String getIndexLocation() {
		return configuration.getString("lucene.index.location");
	}

}

