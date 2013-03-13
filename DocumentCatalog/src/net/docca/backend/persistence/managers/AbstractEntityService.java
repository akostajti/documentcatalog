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

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * abstract base class for all {@code EntityService} implementations.
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 * @param <T>
 */
public abstract class AbstractEntityService<T extends IdentifiableEntity> implements EntityService<T> {

	@Override
	public final T save(final T entity) {
		T modifiedEntity = beforeSave(entity);
		return getRepository().save(modifiedEntity);
	}

	@Override
	public final void delete(final Long id) {
		getRepository().delete(id);
	}

	@Override
	public final T find(final Long id) {
		return getRepository().findOne(id);
	}

	@Override
	public final Iterable<T> findAll(final Iterable<Long> ids) {
		return getRepository().findAll(ids);
	}

	@Override
	public final List<T> findAll() {
		return getRepository().findAll();
	}

	/**
	 * returns a {@link JpaRepository} implementation for accessing the entities of the
	 * given type. this method is invoked on every database operation som implement it
	 * efficiently.
	 *
	 * @return the repository implementation.
	 */
	public abstract JpaRepository<T, Long> getRepository();

	/**
	 * this method gets called immediately before saving the entity. the default implementation
	 * doesn't do antyhing.
	 * @param entity the entity to be saved
	 * @return the modified entity. might be a different object
	 */
	public T beforeSave(final T entity) {
		return entity;
	}
}

