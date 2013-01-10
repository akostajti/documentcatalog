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
package net.docca.backend.search.indexers;

import net.docca.backend.search.Indexable;

/**
 * Instances of this interface index a specifc type of objects.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
public interface Indexer {

	/**
	 * used for transforming indexed property names to a common format.
	 *
	 * @author Akos Tajti <akos.tajti@gmail.com>
	 *
	 */
	public class NameCanonizer {
		/**
		 * the default implementation simply trims the whitespaces and converts the
		 * <code>subject</code> to all lower case.
		 * @param subject the string to convert.
		 * @return the string converted to the canonical format.
		 */
		public String canonize(final String subject) {
			if (subject == null) {
				return null;
			}
			return subject.trim()
					.toLowerCase();
		}
	}

	/**
	 * Attempts to index <code>indexable</code>.
	 *
	 * @param indexable the object to be indexed.
	 * @return <code>true</code> if the indexing was successful.
	 */
	boolean index(final Indexable indexable);
}

