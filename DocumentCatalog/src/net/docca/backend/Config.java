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
	 * the only instance.
	 */
	private static final Config INSTANCE = new Config();

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
	}

	// TODO: move the lucene config to a separate file which is parsed by lucene
	/**
	 * returns the lucene index location.
	 * @return the path to the index directory.
	 */
	public String getIndexLocation() {
		return null; //TODO: implement
	}

}

