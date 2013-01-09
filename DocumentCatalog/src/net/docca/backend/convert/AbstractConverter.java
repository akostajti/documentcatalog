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
package net.docca.backend.convert;

import java.io.File;
import java.io.FileOutputStream;
/**
 * represents a class that converts a file to some other format.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
public abstract class AbstractConverter {
	/**
	 * reads <code>input</code> and writes the converted data to
	 * <code>out</code>.
	 * @param input
	 * @param out
	 * @throws Exception
	 */
	public abstract void convert(File input, FileOutputStream out)
			throws Exception;
}
