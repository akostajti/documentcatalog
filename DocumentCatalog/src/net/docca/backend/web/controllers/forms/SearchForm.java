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

import javax.validation.constraints.NotNull;

/**
 * form class for {@code SearchController}.
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
public class SearchForm {
	/**
	 * the keyword to search for.
	 */
	@NotNull
	private String keyword;

	/**
	 * getter for keyword.
	 * @return the keyword
	 */
	public final String getKeyword() {
		return keyword;
	}

	/**
	 * setter for keyword.
	 * @param keyword the keyword to set
	 */
	public final void setKeyword(final String keyword) {
		this.keyword = keyword;
	}
}
