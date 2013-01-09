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
