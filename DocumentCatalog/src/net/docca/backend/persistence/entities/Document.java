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

import java.sql.Timestamp;

import javax.persistence.Entity;

/**
 * represents a document stored to the database.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Entity
public class Document extends IdentifiableEntity {
	/**
	 * defines the possible types of documents stored to the database.
	 * @author Akos Tajti <akos.tajti@gmail.com>
	 *
	 */
	public enum DocumentType {
		/**
		 * pdf type of document.
		 */
		PDF,
		/**
		 * word document.
		 */
		DOC
	}

	/**
	 * the file system path of the document.
	 */
	private String path;

	/**
	 * the type of the document.
	 */
	private DocumentType type;

	/**
	 * the creation date.
	 */

	private Timestamp createdAt;

	/**
	 * stored the origin of this document. if it was parsed from an image that this property contains the
	 * path to the image.
	 */
	private String source;

	/**
	 * getter for path.
	 * @return the path
	 */
	public final String getPath() {
		return path;
	}

	/**
	 * setter for path.
	 * @param path the path to set
	 */
	public final void setPath(final String path) {
		this.path = path;
	}

	/**
	 * getter for type.
	 * @return the type
	 */
	public final DocumentType getType() {
		return type;
	}

	/**
	 * setter for type.
	 * @param type the type to set
	 */
	public final void setType(final DocumentType type) {
		this.type = type;
	}

	/**
	 * getter for createdAt.
	 * @return the createdAt
	 */
	public final Timestamp getCreatedAt() {
		return createdAt;
	}

	/**
	 * setter for createdAt.
	 * @param createdAt the createdAt to set
	 */
	public final void setCreatedAt(final Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * getter for source.
	 * @return the source
	 */
	public final String getSource() {
		return source;
	}

	/**
	 * setter for source.
	 * @param source the source to set
	 */
	public final void setSource(final String source) {
		this.source = source;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
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

		if (!(obj instanceof Document)) {
			return false;
		}
		Document other = (Document) obj;
		if (path == null) {
			if (other.path != null) {
				return false;
			}
		} else if (!path.equals(other.path)) {
			return false;
		}
		if (source == null) {
			if (other.source != null) {
				return false;
			}
		} else if (!source.equals(other.source)) {
			return false;
		}
		if (getId() == null) {
			if (other.getId() != null) {
				return false;
			}
		} else if (!getId().equals(other.getId())) {
			return false;
		}
		if (type != other.type) {
			return false;
		}
		return true;
	}

}

