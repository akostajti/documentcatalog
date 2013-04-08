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
package net.docca.backend.web.controllers.forms;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

/**
 * form class for <code>UploadImageController</code>.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
public class UploadImageForm {
	/**
	 * a comment added by the user.
	 */
	private String description;

	/**
	 * the list of uploaded files.
	 */
	private List<MultipartFile> files;

	/**
	 * a comma separated list of tags.
	 */
	private String tags;

	/**
	 * the comment provided by the user on upload.
	 */
	private String comment;

	/**
	 * when the user uploads multiple files at the same file he can decide if he
	 * wants to merge the result of the ocr process into a single file. this field
	 * stores his decision.
	 */
	private boolean merge;

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
	 * getter for tags.
	 * @return the tags
	 */
	public final String getTags() {
		return tags;
	}

	/**
	 * setter for tags.
	 * @param tags the tags to set
	 */
	public final void setTags(final String tags) {
		this.tags = tags;
	}

	/**
	 * returns the description.
	 * @return the decription
	 */
	public final String getDescription() {
		return description;
	}

	/**
	 * sets the description.
	 * @param description the description
	 */
	public final void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * returns the list of the uploaded files.
	 * @return the files
	 */
	public final List<MultipartFile> getFiles() {
		return files;
	}

	/**
	 * sets the file list.
	 * @param files the files
	 */
	public final void setFiles(final List<MultipartFile> files) {
		this.files = files;
	}

	/**
	 * getter for merge.
	 * @return the merge
	 */
	public final boolean isMerge() {
		return merge;
	}

	/**
	 * setter for merge.
	 * @param merge the merge to set
	 */
	public final void setMerge(final boolean merge) {
		this.merge = merge;
	}
}

