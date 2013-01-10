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
package net.docca.backend.search;

import java.util.Map;

/**
 * A mock indexable implementation.
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
public class MockIndexable implements Indexable {
	/**
	 * any kind of properties. these will be indexed.
	 */
	private Map<String, Object> properties;

	/**
	 * simply returns the properties previously set by <code>setProperties()</code>.
	 * @return the properties
	 */
	@Override
	public final Map<String, Object> getProperties() {
		return properties;
	}

	/**
	 * sets the properties.
	 *
	 * @param properties .
	 */
	public final void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}
}

