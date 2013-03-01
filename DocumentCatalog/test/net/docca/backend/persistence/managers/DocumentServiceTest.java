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
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.docca.backend.persistence.entities.Document;
import net.docca.test.AbstractTestNGTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.testng.annotations.Test;

/**
 * tests for <code>DocumentManager</code>.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Test(groups = {"mustrun", "managers", "persistence" })
public class DocumentServiceTest extends AbstractTestNGTest {
	/**
	 * the service to be tested.
	 */
	@Autowired
	private DocumentService service;

	/**
	 * tests if the save method works correctly.
	 */
	@Rollback
	public final void testSave() {
		Document document = new Document();
		document.setPath("test/path" + Math.random());
		document.setCreatedAt(new Timestamp(System.currentTimeMillis()));

		service.save(document);

		// now retrieve the document and check if it is the same
		Document found = service.find(document.getId());
		assertEquals(found, document);

		// update an existing document; no exception
		found.setPath("an/other/path");
		service.save(found);

		Document updated = service.find(document.getId());
		assertEquals(updated, found);

		// null value
		try {
			service.save(null);
			fail();
		} catch (Exception ex) {

		}
	}

	/**
	 * tests the find method worth some invalid values.
	 */
	@Rollback
	public final void testFind() {
		// invalid id
		Document result = service.find(Long.valueOf(-2312312));
		assertNull(result);

		// tests that deleted document is not found
		Document document = new Document();
		document.setPath("test/path");
		document.setCreatedAt(new Timestamp(System.currentTimeMillis()));

		service.save(document);
		service.delete(document.getId());

		result = service.find(document.getId());
		assertNull(result);
	}

	/**
	 * tests the delete method.
	 */
	@Rollback
	public final void testDelete() {
		// delete null value; no exception
		try {
			service.delete(null);
			fail();
		} catch (Exception e) {

		}

		// delete invalid id; no exception
		try {
			service.delete(Long.valueOf(-32123));
			fail();
		} catch (Exception ex) {

		}


		Document document = new Document();
		document.setPath("test/path");
		document.setCreatedAt(new Timestamp(System.currentTimeMillis()));

		service.save(document);
		service.delete(document.getId());

		Document result = service.find(document.getId());
		assertNull(result);

		// delete the already deleted
		try {
			service.delete(document.getId());
			fail();
		} catch (Exception ex) {

		}
	}

	/**
	 * test if nothing is found in an empty database.
	 */
	public final void nothingShouldBeDoundInEmptyTable() {
		assertTrue(service.findAll().isEmpty());
	}

	/**
	 * tests if all items inserted to the database are found.
	 */
	@Rollback
	public final void allDocumentsShouldBeFound() {
		final int documentCount = 10;
		List<Document> expectedDocuments = new ArrayList<Document>();
		for (int i = 0; i < documentCount; i++) {
			Document document = new Document();
			document.setPath("test/path" + i);
			document.setCreatedAt(new Timestamp(System.currentTimeMillis()));
			service.save(document);
			expectedDocuments.add(document);
		}

		List<Document> documents = service.findAll();
		assertTrue(documents.containsAll(expectedDocuments));
		assertEquals(documents.size(), expectedDocuments.size());

		for (Document doc: expectedDocuments) {
			service.delete(doc.getId());
		}
	}

	/**
	 * tests if the documents are found by id.
	 */
	@Rollback
	public final void shouldBeFoundById() {
		Document document = new Document();
		document.setPath("test/path");
		document.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		service.save(document);

		Iterable<Document> documents = service.findAll(Collections.singletonList(document.getId()));
		for (Document doc: documents) {
			assertEquals(doc, document);
		}
	}
}

