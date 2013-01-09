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
package net.docca.backend.convert.hocr.elements;

import net.docca.backend.convert.hocr.attributes.BoundingBox;

/**
 * represents a hocr element.
 * 
 * @author akostajti
 *
 */
public abstract class HocrElement {
	protected enum TagNames {
		p,
		div,
		span
	};

	private final String id;
	private final BoundingBox boundingBox;

	public HocrElement(String id, BoundingBox boundingBox) {
		super();
		this.id = id;
		this.boundingBox = boundingBox;
	}

	/**
	 * returns the value of the <code>class</code> attribute on the element.
	 * 
	 * @return
	 */
	public abstract String getClassName();

	/**
	 * retuns the html tag name of the element
	 * @return
	 */
	public abstract String getTagName() ;

	public String getId() {
		return id;
	}

	public BoundingBox getBoundingBox() {
		return boundingBox;
	}
}
