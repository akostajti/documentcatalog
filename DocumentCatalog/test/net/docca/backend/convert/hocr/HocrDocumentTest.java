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
package net.docca.backend.convert.hocr;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import net.docca.backend.convert.hocr.elements.Page;
import net.docca.backend.search.lucene.LuceneProxy;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * tests for <code>HocrDocument</code>.
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Test(groups = {"hocr", "mustrun" })
public class HocrDocumentTest {
	/**
	 * the bytes of the md5 digest of the textual contents of the test hocr file.
	 */
	private static final byte[] CONTENT_BYTES =
			new byte[] {-89, 36, -60, 118, 28, 77, -56, -78, -45, -96, 59, -6, -80, -11, 17, 125};

	/**
	 * tests the <code>getProperties()</code> method implemented from the <code>Indexable</code> interface.
	 * @throws IOException thrown when it was not possible to read the hocr file
	 * @throws NoSuchAlgorithmException thrown when the md5 algorith (used for creating the digest) is not found
	 */
	public final void testGetProperties() throws IOException, NoSuchAlgorithmException {
		// test with an empty document
		HocrDocument emptyDocument = new HocrDocument();
		emptyDocument.setPages(null);
		Assert.assertTrue(emptyDocument.getProperties().isEmpty());

		emptyDocument.setPages(new ArrayList<Page>());
		Assert.assertTrue(emptyDocument.getProperties()
				.get(LuceneProxy.DEFAULT_INDEX_FIELD).getValue().toString().isEmpty());

		// read a valid document
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("hocr/hocr-test1.html");
		HocrParser parser = new HocrParser(in);
		HocrDocument document = parser.parse();

		MessageDigest md5 = MessageDigest.getInstance("MD5");
		byte[] contentDigest = md5.digest(document.getProperties().get(LuceneProxy.DEFAULT_INDEX_FIELD)
				.getValue().toString().getBytes());
		Assert.assertEquals(contentDigest, CONTENT_BYTES);
	}
}
