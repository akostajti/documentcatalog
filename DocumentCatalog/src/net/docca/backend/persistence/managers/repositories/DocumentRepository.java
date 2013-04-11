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
package net.docca.backend.persistence.managers.repositories;

import java.util.List;

import net.docca.backend.persistence.entities.Document;
import net.docca.backend.persistence.entities.NamedEntityTag;
import net.docca.backend.persistence.entities.Tag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * the spring repository interface for <code>Document</code> entities.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Transactional
public interface DocumentRepository extends JpaRepository<Document, Long> {
	/**
	 * finds all documents that are tagged with {@code tag}.
	 *
	 * @param tag the tag to look for
	 * @return the documents tagged with this tag
	 */
	@Query("select d from Document d join d.tags t where ?1 = t")
	List<Document> findByTag(final Tag tag);

	/**
	 * finds all documents that are tagged with {@code tag}.
	 *
	 * @param tag the tag to look for
	 * @return the documents tagged with this tag
	 */
	@Query("select d from Document d join d.namedEntities t where ?1 = t")
	List<Document> findByNamedEntityTag(final NamedEntityTag tag);
}

