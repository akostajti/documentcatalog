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
package net.docca.backend.persistence.managers;

import java.util.List;

import net.docca.backend.persistence.entities.IdentifiableEntity;

/**
 * superinterface for all entity services.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 * @param <T> the <code>IndentifiableEntity</code> subclass that is managed by the service
 */
public interface EntityService<T extends IdentifiableEntity> {
	/**
	 * stores an entity to the database or updates an existing one.
	 *
	 * @param entity the entity to store.
	 */
	void save(final T entity);

	/**
	 * finds an object by id.
	 *
	 * @param id the id
	 * @return the document with the id
	 */
	T find(final Long id);

	/**
	 * finds all entities of the given type stored in the database.
	 * @return the entities found.
	 */
	List<T> findAll();

	/**
	 * finds all entities whose id is in <code>ids</code>.
	 * @param ids the list of ids to be found
	 * @return the entities found
	 */
	Iterable<T> findAll(Iterable<Long> ids);

	/**
	 * deletes an entity with the given id.
	 *
	 * @param id the id of the document to delete.
	 */
	void delete(final Long id);
}

