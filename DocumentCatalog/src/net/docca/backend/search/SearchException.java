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
 * thrown on any kind of search problem.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
public class SearchException extends Exception {

	public SearchException() {
	}

	public SearchException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public SearchException(Throwable arg0) {
		super(arg0);
	}

	public SearchException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}

