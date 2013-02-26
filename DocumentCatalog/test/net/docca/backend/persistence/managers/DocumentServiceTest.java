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
import static org.testng.Assert.fail;

import java.sql.Timestamp;

import net.docca.backend.configurations.JpaConfiguration;
import net.docca.backend.configurations.SpringConfiguration;
import net.docca.backend.persistence.entities.Document;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

/**
 * tests for <code>DocumentManager</code>.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Test(groups = {"mustrun", "managers", "persistence" })
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {JpaConfiguration.class,
	SpringConfiguration.class })
public class DocumentServiceTest extends AbstractTestNGSpringContextTests {
	@Autowired
	private DocumentService service;

	/**
	 * tests if the save method works correctly.
	 */
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
}

