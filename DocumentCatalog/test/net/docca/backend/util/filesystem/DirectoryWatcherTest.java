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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

/**
 * tests for the directory watcher.
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Test(groups = {"mustrun", "filesystem" })
public class DirectoryWatcherTest {
	/**
	 * tests the watch method of the directory watcher. tests if all listeners are notified correctly.
	 * @throws IOException when the tmp directory couldn't be created
	 * @throws InterruptedException when the sleep was interrupted
	 */
	public final void testWatch() throws IOException, InterruptedException {
		final int files = 3;
		File directory = File.createTempFile("test", "dir");
		directory.delete();
		directory.mkdirs();

		DirectoryListener listener = mock(DirectoryListener.class);
		DirectoryWatcher watcher = new DirectoryWatcher(directory.getAbsoluteFile().toPath());
		watcher.addListener(listener);
		watcher.startWatching();

		TimeUnit.SECONDS.sleep(3);

		// create some files after the watching started
		List<Path> paths = new ArrayList<>();

		for (int i = 0; i < files; i++) {
			File file = File.createTempFile("test", ".html", directory);
			paths.add(file.toPath());
		}

		TimeUnit.SECONDS.sleep(3);

		// verify
		for (Path path: paths) {
			verify(listener).notify(path.getFileName());
		}
	}

	/**
	 * tests invalid options.
	 * @throws Exception thrown on any kind of error.
	 */
	public final void testInvalid() throws Exception {
		// test with null directory
		DirectoryWatcher watcher = new DirectoryWatcher(null);

		Field field = DirectoryWatcher.class.getDeclaredField("logger");
		field.setAccessible(true);
		Logger mockLogger = mock(Logger.class);
		field.set(watcher, mockLogger);

		watcher.startWatching();
		TimeUnit.SECONDS.sleep(3);
		verify(mockLogger).debug("Exception while running the watcher");
	}
}

