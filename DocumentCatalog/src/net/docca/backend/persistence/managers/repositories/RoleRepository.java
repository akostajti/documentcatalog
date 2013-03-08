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

import net.docca.backend.persistence.entities.Role;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * the repository interface for <code>Role</code>.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

}

