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


/**
 * a simple, immutable class for wrapping any kind of objects and giving
 * them a priority.
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 * @param <T>
 */
public class Prioritized<T> implements Comparable<Prioritized<T>> {
	/**
	 * the subject of this object. it is any kind of object wrapped
	 * just for giving it a priority.
	 */
	private final T subject;

	/**
	 * the priority of this object. <strong>the LOWER</strong> the priority,
	 * the sooner the object will get processed.
	 */
	private final int priority;

	/**
	 * sets the basic properties of the class.
	 * @param subject the wrapped subject
	 * @param priority the priority
	 */
	public Prioritized(final T subject, final int priority) {
		this.subject = subject;
		this.priority = priority;
	}
	/**
	 * returns the subject of this object.
	 * @return the subject
	 */
	public final T getSubject() {
		return subject;
	}

	/** returns the priority of this object.
	 * @return the priority
	 */
	public final int getPriority() {
		return priority;
	}

	/**
	 * compares the objects by priority.
	 * @param other to compare with
	 * @return as usual
	 */
	@Override
	public final int compareTo(final Prioritized<T> other) {
		if (other == null) {
			return 1;
		}

		return this.priority - other.getPriority();
	}

	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + priority;
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
		return result;
	}

	@Override
	public final boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Prioritized<T> other = (Prioritized<T>) obj;
		if (priority != other.priority) {
			return false;
		}
		if (subject == null) {
			if (other.subject != null) {
				return false;
			}
		} else if (!subject.equals(other.subject)) {
			return false;
		}
		return true;
	}


}

