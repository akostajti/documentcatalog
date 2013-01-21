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

/**
 * stores a property of an indexable object. defines the attributes of the property: how it is stored in the ndex,
 * its type etc. its instances are immutable.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
public class IndexedProperty {

	/**
	 * defines the storing options for indexed properties.
	 * @author Akos Tajti <akos.tajti@gmail.com>
	 *
	 */
	public enum Stored {
		/**
		 * the property is stored in the index.
		 */
		Stored,
		/**
		 * the property is not stored.
		 */
		NotStored
	};

	/**
	 * the type of the property. default is string.
	 */
	private final Class<?> type;

	/**
	 * the value of the property.
	 */
	private final Object value;

	/**
	 * whether the property is stored in the index.
	 */
	private final Stored stored;

	/**
	 * the language of the property. lowercase two-letter ISO-639 code.
	 */
	private final String language;

	/**
	 * creates an instance using the default values.
	 * @param value the value of the property.
	 */
	public IndexedProperty(final Object value) {
		this(value, String.class);
	}

	/**
	 * constructor with the two parameters.
	 *
	 * @param value the value
	 * @param type type of the property
	 */
	public IndexedProperty(final Object value, final Class<?> type) {
		this(value, type, Stored.NotStored);
	}

	/**
	 * constructor with three parameters.
	 * @param value
	 * @param type
	 * @param stored
	 */
	public IndexedProperty(final Object value, final Class<?> type, final Stored stored) {
		this(value, type, stored, "en");
	}

	/**
	 * constructor with three parameters.
	 *
	 * @param value
	 * @param type
	 * @param stored
	 * @param language
	 */
	public IndexedProperty(final Object value, final Class<?> type, final Stored stored, final String language) {
		super();
		this.type = type;
		this.value = value;
		this.stored = stored;
		this.language = language;
	}

	public Class<?> getType() {
		return type;
	}

	public Object getValue() {
		return value;
	}

	public Stored getStored() {
		return stored;
	}

	public String getLanguage() {
		return language;
	}

	@Override
	public String toString() {
		return "IndexedProperty [type=" + type + ", value=" + value
				+ ", stored=" + stored + ", language=" + language + "]";
	}
}

