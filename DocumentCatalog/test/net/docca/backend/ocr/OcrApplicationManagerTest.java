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
package net.docca.backend.ocr;

import static org.mockito.Mockito.when;
import net.docca.test.util.MockUtils;

import org.apache.commons.configuration.Configuration;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * tests for ocrapplication manager.
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Test(groups = {"ocr", "mustrun" })
public class OcrApplicationManagerTest {
	/**
	 * tests the manager with invalid application class name.
	 * @throws Exception on any error
	 */
	public final void testInvalidClassname() throws Exception {
		// not an application implementation
		Configuration configuration = MockUtils.getMockedConfiguration();
		when(configuration.getString("ocr.application"))
		.thenReturn("java.lang.Object");

		try {
			OcrApplicationManager.getInstance().findOcrApplication();
			Assert.fail();
		} catch (IllegalStateException ex) {}
	}
}

