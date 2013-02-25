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

import net.docca.backend.persistence.entities.Document;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * the spring repository interface for <code>Document</code> entities.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
public interface Documentrepository extends JpaRepository<Document, Long> {

}

