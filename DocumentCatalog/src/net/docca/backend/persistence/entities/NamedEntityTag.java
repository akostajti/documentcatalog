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
package net.docca.backend.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.apache.log4j.Logger;

/**
 * entity class or named entities parsed from the texts (places, locations, organizations etc.).
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Entity
public class NamedEntityTag extends IdentifiableEntity {
	/**
	 * lists the possible named entity types.
	 * @author Akos Tajti <akos.tajti@gmail.com>
	 *
	 */
	public enum Type {
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
	private static Logger logger = Logger.getLogger(NamedEntityTag.class);

	/**
	 * the name of the entity.
	 */
	@Column(unique = true)
	private String name;

	/**
	 * the type of the named entity.
	 */
	private Type type;

	/**
	 * the default contructor.
	 */
	public NamedEntityTag() {

	}

	/**
	 * creates an instance.
	 * @param name the name.
	 * @param type the type.
	 */
	public NamedEntityTag(final String name, final Type type) {
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

	/**
	 * setter for name.
	 * @param name the name to set
	 */
	public final void setName(final String name) {
		this.name = name;
	}

	/**
	 * setter for type.
	 * @param type the type to set
	 */
	public final void setType(final Type type) {
		this.type = type;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		NamedEntityTag other = (NamedEntityTag) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (type != other.type) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NamedEntityTag [name=");
		builder.append(name);
		builder.append(", type=");
		builder.append(type);
		builder.append("]");
		return builder.toString();
	}
}

