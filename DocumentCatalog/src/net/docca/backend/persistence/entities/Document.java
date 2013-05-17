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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 * represents a document stored to the database.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Entity
public class Document extends IdentifiableEntity {
	/**
	 * the maximum length of the generated summary field.
	 */
	public static final int MAXIMUM_SUMMARY_LENGTH = 200;

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
	@OneToMany
	private List<Source> sources;

	/**
	 * short description of the document.
	 */
	private String description;

	/**
	 * longer description of the document.
	 */
	private String comment;

	/**
	 * the language of the document.
	 */
	private String language;

	/**
	 * the user who uploaded this file.
	 */
	@OneToOne
	private User uploader;

	/**
	 * a few sentence long summary generated from the contents of the document.
	 */
	@Column(length = MAXIMUM_SUMMARY_LENGTH)
	private String generatedSummary;

	/**
	 * the tags of this document.
	 */
	@ManyToMany(fetch = FetchType.EAGER)
	private List<Tag> tags;

	/**
	 * the named entity tags found in this document.
	 */
	@ManyToMany
	private Set<NamedEntityTag> namedEntities;

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
	public final List<Source> getSources() {
		return sources;
	}

	/**
	 * setter for source.
	 * @param source the source to set
	 */
	public final void setSources(final List<Source> source) {
		this.sources = source;
	}

	/**
	 * adds a source path.
	 * @param source the source path.
	 */
	public final void addSource(final Source source) {
		if (sources == null) {
			sources = new ArrayList<>();
		}
		sources.add(source);
	}

	/**
	 * getter for description.
	 * @return the description
	 */
	public final String getDescription() {
		return description;
	}

	/**
	 * setter for description.
	 * @param description the description to set
	 */
	public final void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * getter for tags.
	 * @return the tags
	 */
	public final List<Tag> getTags() {
		return tags;
	}

	/**
	 * setter for tags.
	 * @param tags the tags to set
	 */
	public final void setTags(final List<Tag> tags) {
		this.tags = tags;
	}

	/**
	 * getter for namedEntities.
	 * @return the namedEntities
	 */
	public final Set<NamedEntityTag> getNamedEntities() {
		return namedEntities;
	}

	/**
	 * setter for namedEntities.
	 * @param namedEntities the namedEntities to set
	 */
	public final void setNamedEntities(final Set<NamedEntityTag> namedEntities) {
		this.namedEntities = namedEntities;
	}

	/**
	 * getter for comment.
	 * @return the comment
	 */
	public final String getComment() {
		return comment;
	}

	/**
	 * setter for comment.
	 * @param comment the comment to set
	 */
	public final void setComment(final String comment) {
		this.comment = comment;
	}

	/**
	 * getter for language.
	 * @return the language
	 */
	public final String getLanguage() {
		return language;
	}

	/**
	 * setter for language.
	 * @param language the language to set
	 */
	public final void setLanguage(final String language) {
		this.language = language;
	}

	/**
	 * getter for generatedSummary.
	 * @return the generatedSummary
	 */
	public final String getGeneratedSummary() {
		return generatedSummary;
	}

	/**
	 * setter for generatedSummary.
	 * @param generatedSummary the generatedSummary to set
	 */
	public final void setGeneratedSummary(final String generatedSummary) {
		this.generatedSummary = generatedSummary;
	}

	/**
	 * getter for uploader.
	 * @return the uploader
	 */
	public final User getUploader() {
		return uploader;
	}

	/**
	 * setter for uploader.
	 * @param uploader the uploader to set
	 */
	public final void setUploader(final User uploader) {
		this.uploader = uploader;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 10000;
		result = prime * result + ((getPath() == null) ? 0 : getPath().hashCode());
		result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
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
		if (sources == null) {
			if (other.sources != null) {
				return false;
			}
		} else if (!sources.equals(other.sources)) {
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

