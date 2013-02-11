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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.sql.Timestamp;

import net.docca.backend.persistence.entities.Document;

import org.testng.annotations.Test;

/**
 * tests for <code>DocumentManager</code>.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Test(groups = {"mustrun", "managers", "persistence" })
public class DocumentManagerTest {
	/**
	 * tests if the save method works correctly.
	 */
	public final void testSave() {
		DocumentManager manager = new DocumentManager();
		Document document = new Document();
		document.setPath("test/path" + Math.random());
		document.setCreatedAt(new Timestamp(System.currentTimeMillis()));

		manager.save(document);

		// now retrieve the document and check if it is the same
		Document found = manager.find(document.getId());
		assertEquals(found, document);

		// update an existing document; no exception
		found.setPath("an/other/path");
		manager.save(found);

		Document updated = manager.find(document.getId());
		assertEquals(updated, found);

		// null value; no exception
		manager.save(null);
	}

	/**
	 * tests the find method worth some invalid values.
	 */
	public final void testFind() {
		DocumentManager manager = new DocumentManager();
		// null id
		Document result = manager.find(null);
		assertNull(result);

		// invalid id
		result = manager.find(Long.valueOf(-2312312));
		assertNull(result);

		// tests that deleted document is not found
		Document document = new Document();
		document.setPath("test/path");
		document.setCreatedAt(new Timestamp(System.currentTimeMillis()));

		manager.save(document);
		manager.delete(document.getId());

		result = manager.find(document.getId());
		assertNull(result);
	}

	/**
	 * tests the delete method.
	 */
	public final void testDelete() {
		DocumentManager manager = new DocumentManager();

		// delete null value; no exception
		manager.delete(null);

		// delete invalid id; no exception
		manager.delete(Long.valueOf(-32123));


		Document document = new Document();
		document.setPath("test/path");
		document.setCreatedAt(new Timestamp(System.currentTimeMillis()));

		manager.save(document);
		manager.delete(document.getId());

		Document result = manager.find(document.getId());
		assertNull(result);

		// delete the already deleted; no exception
		manager.delete(document.getId());
	}
}

