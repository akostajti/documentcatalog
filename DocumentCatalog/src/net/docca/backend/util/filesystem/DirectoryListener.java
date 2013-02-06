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
package net.docca.backend.util.filesystem;

import java.nio.file.Path;

/**
 * listener for directory changes. used by <code>DirectoryWatcher</code>.
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
public interface DirectoryListener {
	/**
	 * notifies the listener that the path was added to the directory watched.
	 * @param path the path that was added to the directory.
	 */
	void notify(final Path path);
}

