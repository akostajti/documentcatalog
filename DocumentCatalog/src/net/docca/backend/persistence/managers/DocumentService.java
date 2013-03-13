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

import net.docca.backend.persistence.entities.Document;
import net.docca.backend.persistence.managers.repositories.DocumentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * a manager for <code>Document</code> entities.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Service
public final class DocumentService extends AbstractEntityService<Document> {
	/**
	 * the repository used for accessing the documents.
	 */
	@Autowired
	private DocumentRepository repository;

	@Override
	public JpaRepository<Document, Long> getRepository() {
		return repository;
	}
}

