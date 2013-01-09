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

import net.docca.backend.convert.hocr.Capabilities;
import net.docca.backend.convert.hocr.attributes.BoundingBox;

public class Word extends HocrElement {
	private final String textContent;

	public Word(String id, BoundingBox boundingBox, String textContent) {
		super(id, boundingBox);
		this.textContent = textContent;
	}

	public String getTextContent() {
		return textContent;
	}

	@Override
	public String getClassName() {
		return Capabilities.ocrx_word.name();
	}

	@Override
	public String getTagName() {
		return TagNames.span.name();
	}

	@Override
	public String toString() {
		return "Word [textContent=" + textContent + ", getId()=" + getId()
				+ ", getBoundingBox()=" + getBoundingBox() + "]";
	}

}
