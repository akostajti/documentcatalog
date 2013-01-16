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
 * Interface for marking classes as indexable.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
public interface Indexable {

	/**
	 * Returns the properties of the object that needs to be indexed.
	 *
	 * @return the map of the properties to be indexed.
	 */
	Map<String, IndexedProperty> getProperties();

	/**
	 * returns a unique identifier for the indexable. this id is unique across the whole application
	 * (no two entities should have the same ids ever).
	 * @return the unique id.
	 */
	Integer getId();
}

