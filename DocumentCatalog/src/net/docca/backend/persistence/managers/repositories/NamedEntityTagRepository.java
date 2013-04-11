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

import net.docca.backend.persistence.entities.NamedEntityTag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * repository for {@code NamedEntityTag} entities.
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
public interface NamedEntityTagRepository extends JpaRepository<NamedEntityTag, Long> {
	/**
	 * finds a tag by its name.
	 * @param name the tagname to find
	 * @return the tag found
	 */
	NamedEntityTag findByName(final String name);

	/**
	 * finds all tags with the given names.
	 * @param names the tagnames to find
	 * @return the tags found
	 */
	@Query("select t from NamedEntityTag t where t.name in ?1")
	List<NamedEntityTag> findByNames(final List<String> names);
}
