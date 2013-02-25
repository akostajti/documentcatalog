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

import net.docca.backend.persistence.entities.Document;
import net.docca.backend.persistence.managers.repositories.Documentrepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * a manager for <code>Document</code> entities.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Component
public final class DocumentService implements EntityService<Document> {
	/**
	 * the repository used to access the crud operations.
	 */
	@Autowired
	private Documentrepository repository;

	@Override
	public void save(final Document entity) {
		repository.save(entity);
	}

	@Override
	public Document find(final Long id) {
		return repository.findOne(id);
	}

	@Override
	public List<Document> findAll() {
		return repository.findAll();
	}

	@Override
	public Iterable<Document> findAll(final Iterable<Long> ids) {
		return repository.findAll(ids);
	}

	@Override
	public void delete(final Long id) {
		repository.delete(id);
	}

}

