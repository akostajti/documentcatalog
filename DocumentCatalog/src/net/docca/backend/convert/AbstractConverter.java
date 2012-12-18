package net.docca.backend.convert;

import java.io.File;
import java.io.FileOutputStream;

public abstract class AbstractConverter {
	abstract public void convert(File input, FileOutputStream out) throws Exception;
}
