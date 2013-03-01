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
package net.docca.test;

import net.docca.backend.configurations.SpringConfiguration;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

/**
 * abstract base class for testng tests. it' properly configured and all common beans are injected.
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {SpringConfiguration.class })
public abstract class AbstractTestNGTest extends AbstractTestNGSpringContextTests {

}

