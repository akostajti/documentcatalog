package net.docca.backend.search;

/**
 * Interface for marking classes as indexable.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
public interface Indexable {

	/**
	 * indexes the object.
	 * @return <code>true</code> if the indexing was successful.
	 */
	boolean index();
}

