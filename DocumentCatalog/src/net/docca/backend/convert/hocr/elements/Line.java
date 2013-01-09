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

import java.util.ArrayList;
import java.util.List;

import net.docca.backend.convert.hocr.Capabilities;
import net.docca.backend.convert.hocr.attributes.BoundingBox;

public class Line extends HocrElement {
	private Paragraph paragraph;
	private List<Word> words = new ArrayList<Word>();

	public Line(String id, BoundingBox boundingBox) {
		super(id, boundingBox);
	}

	public Paragraph getParagraph() {
		return paragraph;
	}

	public void setParagraph(Paragraph paragraph) {
		this.paragraph = paragraph;
	}

	public List<Word> getWords() {
		return words;
	}

	public void setWords(List<Word> words) {
		this.words = words;
	}

	public void addWord(Word word) {
		this.words.add(word);
	}

	public String getTextContent() {
		StringBuilder builder = new StringBuilder();
		for (int index = 0; index < words.size(); index++) {
			if (index != 0) {
				builder.append(" ");
			}
			builder.append(words.get(index).getTextContent());
		}

		return builder.toString();
	}

	@Override
	public String getClassName() {
		return Capabilities.ocr_line.name();
	}

	@Override
	public String getTagName() {
		return TagNames.span.name();
	}

	@Override
	public String toString() {
		return "Line [paragraph=" + paragraph + ", words=" + words
				+ ", getTextContent()=" + getTextContent() + ", getId()="
				+ getId() + ", getBoundingBox()=" + getBoundingBox() + "]";
	}
}
