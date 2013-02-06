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

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

/**
 * responsible for watching several directories. when a new file was added to the directory it
 * notifies the listeners. the order in which the listeners are notified is not defined.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
public class DirectoryWatcher {
	/**
	 * the logger for this class.
	 */
	private static Logger logger = Logger.getLogger(DirectoryWatcher.class);

	/**
	 * the threadpool used for executing the watchers.
	 */
	private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();

	/**
	 * the directory to watch.
	 */
	private final Path path;

	/**
	 * the set of directory listeners that will be notified when a new file was added to the
	 * directory.
	 */
	private final Set<DirectoryListener> listeners = new HashSet<>();

	/**
	 * creates a directory watcher for the specified path.
	 * @param path the directory to watch
	 */
	public DirectoryWatcher(final Path path) {
		this.path = path;
	}

	/**
	 * adds a new listener to the watcher.
	 * @param listener the new listener.
	 */
	public final void addListener(final DirectoryListener listener) {
		if (listener != null) {
			listeners.add(listener);
		}
	}

	/**
	 * starts watching the directory.
	 */
	public final void startWatching() {
		EXECUTOR.submit(new Watcher());
	}

	// TODO: review this thread usage when moved to a servlet container.
	/**
	 * thread implementation for running the watcher in a separate thread.
	 * @author Akos Tajti <akos.tajti@gmail.com>
	 *
	 */
	private class Watcher implements Runnable {
		@Override
		public void run() {
			try {
				WatchService service = FileSystems.getDefault().newWatchService();
				WatchKey key = path.register(service, StandardWatchEventKinds.ENTRY_CREATE);
				for (;;) {
					try {
						key = service.take();
					} catch (InterruptedException x) {
						return;
					}
					// poll the events
					for (WatchEvent<?> event: key.pollEvents()) {
						WatchEvent.Kind<?> kind = event.kind();
						if (kind == StandardWatchEventKinds.OVERFLOW) {
							continue;
						}

						// get the filename from the context
						WatchEvent<Path> ev = (WatchEvent<Path>) event;
						Path filename = ev.context();

						// notify all listeners about the new files
						for (DirectoryListener listener: listeners) {
							listener.notify(filename);
						}
					}
					boolean valid = key.reset();
					if (!valid) {
						return;
					}
				}
			} catch (Exception e) {
				logger.debug("Exception while running the watcher");
				logger.debug(e);
			}
		}
	}
}

