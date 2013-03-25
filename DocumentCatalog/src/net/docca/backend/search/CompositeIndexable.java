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

import java.util.HashMap;
import java.util.Map;

/**
 * composite class for merging the properties from multiple {@code Indexable} objects.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
public class CompositeIndexable implements Indexable {
	/**
	 * the properties merged from all indexable objects.
	 */
	private final Map<String, IndexedProperty> properties = new HashMap<String, IndexedProperty>();

	/**
	 * the id of this indexable object.
	 */
	private final Integer id;

	/**
	 * creates an instance based on the properties of all objects in {@code indexables}
	 * @param id the id
	 * @param indexables the indexables
	 */
	public CompositeIndexable(final Integer id, final Indexable... indexables) {
		this.id = id;
		for (Indexable indexable: indexables) {
			Map<String, IndexedProperty> props = indexable.getProperties();
			if (props != null) {
				this.properties.putAll(props);
			}
		}
	}

	/**
	 * adds a property to the property map.
	 * @param name the name of the property
	 * @param property the value of the property
	 */
	public final void addProperty(final String name, final IndexedProperty property) {
		properties.put(name, property);
	}

	@Override
	public final Map<String, IndexedProperty> getProperties() {
		return properties;
	}

	@Override
	public final Integer getId() {
		return id;
	}

}

