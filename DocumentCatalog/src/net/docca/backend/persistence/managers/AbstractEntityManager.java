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

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

/**
 * abstract superclass for all entity managers. contains all methods that are useful for
 * the subclasses.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 * @param <T> the <code>IndentifiableEntity</code> subclass that is manager by the manager
 */
public abstract class AbstractEntityManager<T extends IdentifiableEntity> {
	/**
	 * the hibernate session factory object for running queries.
	 */
	private static SessionFactory sessions = configureSessionFactory();

	/**
	 * an operation that must be run in one hibernate transaction.
	 * @author Akos Tajti <akos.tajti@gmail.com>
	 * @param <U> the type of the object returned by execute
	 */
	interface AtomicOperation<U> {
		/**
		 * will be run in one transaction.
		 * @param session the <code>Session</code> in which the transaction was started
		 * (using <code>beginTransaction</code>)
		 * @return the result of the operation
		 */
		U execute(final Session session);
	}

	/**
	 * stores an entity to the database or updates an existing one.
	 *
	 * @param entity the entity to store.
	 */
	public final void save(final T entity) {
		executeInTransaction(new AtomicOperation<T>() {
			@Override
			public T execute(final Session session) {
				if (entity == null) {
					return null;
				}
				session.saveOrUpdate(entity);
				return null;
			}
		});
	}

	/**
	 * finds an object by id.
	 *
	 * @param id the id
	 * @return the document with the id
	 */
	public final T find(final Long id) {
		return executeInTransaction(new AtomicOperation<T>() {

			@Override
			public T execute(final Session session) {
				Query query = session.createQuery("from " + getManagedClass()
						.getSimpleName() + " where id = :id").setParameter("id", id);
				List result = query.list();
				if (result.isEmpty()) {
					return null;
				}
				return (T) result.get(0);
			}
		});
	}

	/**
	 * deletes an entity with the given id.
	 *
	 * @param id the id of the document to delete.
	 */
	public final void delete(final Long id) {
		executeInTransaction(new AtomicOperation<Integer>() {

			@Override
			public Integer execute(final Session session) {
				Query query = session.createQuery("delete from " + getManagedClass()
						.getSimpleName() + " where id = :id");
				query = query.setParameter("id", id);
				int count = query.executeUpdate();
				return Integer.valueOf(count);
			}
		});
	}

	/**
	 * executes an atomic operation in one transaction.
	 *
	 * @param operation the operation to run in one transacton
	 * @param <U> the type of objects returned by the execute method of the operation
	 * @return the result of the operation
	 */
	public final <U> U executeInTransaction(final AtomicOperation<U> operation) {
		Session session = sessions.openSession();
		U result = null;
		try {
			session.beginTransaction();
			result = operation.execute(session);
			session.getTransaction().commit();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return result;
	}

	/**
	 * returns the type that is managed by this manager.
	 *
	 * @return the class object for <code>T</code>
	 */
	protected abstract Class<T> getManagedClass();

	/**
	 * creates a session factory object based on the default configuration.
	 * @return the session factory object
	 */
	private static SessionFactory configureSessionFactory() {
		Configuration configuration = new Configuration();
		configuration.configure();
		ServiceRegistryBuilder builder = new ServiceRegistryBuilder();
		ServiceRegistry serviceRegistry = builder.applySettings(
				configuration.getProperties()).buildServiceRegistry();
		SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		return sessionFactory;
	}
}

