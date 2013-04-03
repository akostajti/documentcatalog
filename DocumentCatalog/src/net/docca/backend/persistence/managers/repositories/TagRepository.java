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

import net.docca.backend.persistence.entities.Tag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * repository class for tag entities.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
public interface TagRepository extends JpaRepository<Tag, Long> {

	/**
	 * finds a tag by its name.
	 * @param name the tagname to find
	 * @return the tag found
	 */
	Tag findByName(final String name);

	/**
	 * finds all tags with the given names.
	 * @param names the tagnames to find
	 * @return the tags found
	 */
	@Query("select t from Tag t where t.name in ?1")
	List<Tag> findByNames(final List<String> names);
}

