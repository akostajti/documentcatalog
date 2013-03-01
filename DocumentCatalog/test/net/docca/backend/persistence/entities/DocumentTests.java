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
package net.docca.backend.persistence.entities;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.sql.Timestamp;

import net.docca.backend.persistence.entities.Document.DocumentType;

import org.testng.annotations.Test;

/**
 * tests for the document entity.
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Test(groups = {"mustrun", "persistence" })
public class DocumentTests {

	/**
	 * tests if the equals and hashcode methods work properly.
	 */
	public final void equalsAndHashcodeShouldWork() {
		Document doc1 = new Document();
		doc1.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		doc1.setId(Long.valueOf(1000));
		doc1.setPath("mu/ha/ha");
		doc1.setSource("lol");
		doc1.setType(DocumentType.PDF);

		Document doc2 = new Document();
		doc2.setCreatedAt(doc1.getCreatedAt());
		doc2.setId(doc1.getId());
		doc2.setPath(doc1.getPath());
		doc2.setSource(doc1.getSource());
		doc2.setType(doc1.getType());

		assertTrue(doc1.equals(doc2));
		assertTrue(doc2.equals(doc1));
		assertFalse(doc1.equals(null));
		assertEquals(doc1.hashCode(), doc2.hashCode());

		doc2.setPath("an/other/path");
		assertFalse(doc1.equals(doc2));
		assertFalse(doc1.hashCode() == doc2.hashCode());

		doc2.setPath(doc1.getPath());
		doc2.setType(DocumentType.DOC);
		assertFalse(doc1.equals(doc2));
		assertFalse(doc1.hashCode() == doc2.hashCode());

		doc2.setType(doc1.getType());
		doc2.setSource("an/other/source");
		assertFalse(doc1.equals(doc2));
		assertFalse(doc1.hashCode() == doc2.hashCode());

		doc2.setSource(doc1.getSource());
		doc2.setCreatedAt(new Timestamp(System.currentTimeMillis() + 100));
		assertTrue(doc1.equals(doc2));
		assertTrue(doc1.hashCode() == doc2.hashCode());
	}
}

