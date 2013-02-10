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

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

/**
 * a manager for <code>Document</code> entities.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
public final class DocumentManager {
	/**
	 * the hibernate session factory object for running queries.
	 */
	private static final SessionFactory SESSIONS = new AnnotationConfiguration().configure().buildSessionFactory();

	/**
	 * stores a document to the database.
	 * @param document the document to store.
	 */
	public void save(final Document document) {
		if (document == null) {
			return;
		}
		Session session = SESSIONS.openSession();
		try {
			session.beginTransaction();
			if (document.getId() == null) {
				session.save(document);
			} else {
				session.update(document);
			}
			session.getTransaction().commit();
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * finds an object by id.
	 * @param id the id
	 * @return the document with the id
	 */
	public Document find(final Long id) {
		Session session = SESSIONS.openSession();
		session.beginTransaction();
		Query query = session.createQuery("from Document where id = :id").setParameter("id", id);
		List result = query.list();
		if (result.isEmpty()) {
			return null;
		}
		session.getTransaction().commit();
		session.close();
		return (Document) result.get(0);
	}

	/**
	 * deletes a document with the given id.
	 * @param id the id of the document to delete.
	 */
	public void delete(final Long id) {
		Session session = SESSIONS.openSession();
		try {
			session.beginTransaction();
			Document document = new Document();
			document.setId(id);
			session.delete(document);
			session.getTransaction().commit();
		} finally {
			session.close();
		}
	}

}

