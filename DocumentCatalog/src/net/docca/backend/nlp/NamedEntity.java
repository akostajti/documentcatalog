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
package net.docca.backend.nlp;

import org.apache.log4j.Logger;

/**
 * represents a named entity. it has a name and a type.
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
public final class NamedEntity {
	/**
	 * lists the possible named entity types.
	 * @author Akos Tajti <akos.tajti@gmail.com>
	 *
	 */
	enum Type {
		/**
		 * Person type of entity.
		 */
		Person,
		/**
		 * date type of entity.
		 */
		Date,
		/**
		 * time type of entity.
		 */
		Time,
		/**
		 * organization type of entity.
		 */
		Organization,
		/**
		 * location.
		 */
		Location
	}

	/**
	 * the logger for this class.
	 */
	private static Logger logger = Logger.getLogger(NamedEntity.class);

	/**
	 * the name of the entity.
	 */
	private final String name;

	/**
	 * the type of this entity.
	 */
	private final Type type;

	/**
	 * creates a named entity.
	 * @param name the name of the entity
	 * @param type the type of the entity
	 */
	public NamedEntity(final String name, final Type type) {
		this.name = name;
		this.type = type;
	}

	/**
	 * getter for name.
	 * @return the name
	 */
	public final String getName() {
		return name;
	}

	/**
	 * getter for type.
	 * @return the type
	 */
	public final Type getType() {
		return type;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NamedEntity [name=");
		builder.append(name);
		builder.append(", type=");
		builder.append(type);
		builder.append("]");
		return builder.toString();
	}
}

