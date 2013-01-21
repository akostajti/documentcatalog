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
import net.docca.backend.convert.hocr.attributes.TextDirection;

/**
 * represents a paragraph in a hocr document.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
public class Paragraph extends HocrElement {
	/**
	 * the carea that contains this paragraph.
	 */
	private Carea carea;
	/**
	 * the text direction of the paragraph.
	 */
	private TextDirection direction = TextDirection.ltr;
	/**
	 * the lines in this document.
	 */
	private List<Line> lines = new ArrayList<Line>();

	public Paragraph(String id, BoundingBox boundingBox, TextDirection direction) {
		super(id, boundingBox);
		this.direction = direction;
	}

	public TextDirection getDirection() {
		return direction;
	}

	public Carea getCarea() {
		return carea;
	}

	public void setCarea(Carea carea) {
		this.carea = carea;
	}

	public List<Line> getLines() {
		return lines;
	}

	public void setLines(List<Line> lines) {
		this.lines = lines;
	}

	public void addLine(Line line) {
		this.lines.add(line);
		line.setParagraph(this);
	}

	/**
	 * returns the concatenation of lines in this paragraph.
	 * @return the textual content of this paragraph. doesn't keep the structure.
	 */
	public final String getTextContent() {
		if (getLines() == null) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		for (Line line: getLines()) {
			builder.append(line.getTextContent()).append(" ");
		}
		return builder.toString();
	}

	@Override
	public String getClassName() {
		return Capabilities.ocr_par.name();
	}

	@Override
	public String getTagName() {
		return TagNames.p.name();
	}

	@Override
	public String toString() {
		return "Paragraph [direction=" + direction + ", getId()=" + getId()
				+ ", getBoundingBox()=" + getBoundingBox() + "]";
	}
}
