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
 * the default implementation of <code>SearchExpression</code>. It wraps a simple
 * string expression.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
public class DefaultSearchExpression implements SearchExpression {
	/**
	 * the raw representation of the expression.
	 */
	private final String rawExpression;

	/**
	 * creates an instance and sets the raw representation.
	 *
	 * @param rawExpression the raw string representation of the expression.
	 */
	public DefaultSearchExpression(final String rawExpression) {
		this.rawExpression = rawExpression;
	}

	/**
	 * hidden default constructor.
	 */
	@SuppressWarnings("unused")
	private DefaultSearchExpression() {
		this(null);
	};

	@Override
	public final String getRawExpression() {
		return rawExpression;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((rawExpression == null) ? 0 : rawExpression.hashCode());
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
		DefaultSearchExpression other = (DefaultSearchExpression) obj;
		if (rawExpression == null) {
			if (other.rawExpression != null) {
				return false;
			}
		} else if (!rawExpression.equals(other.rawExpression)) {
			return false;
		}
		return true;
	}
}

