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
 * Represents a criteria that is used for finding an object.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
public interface SearchExpression {
	/**
	 * returns the raw string representation of the expression.
	 *
	 * @return the raw representation
	 */
	String getRawExpression();
}

