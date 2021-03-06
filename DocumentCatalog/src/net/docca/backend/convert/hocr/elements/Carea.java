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

public class Carea extends HocrElement {
	private Page page;
	private List<Paragraph> paragraphs = new ArrayList<Paragraph>();

	public Carea(String id, BoundingBox boundingBox) {
		super(id, boundingBox);
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public List<Paragraph> getParagraphs() {
		return paragraphs;
	}

	public void setParagraphs(List<Paragraph> paragraphs) {
		this.paragraphs = paragraphs;
	}

	public void addParagraph(Paragraph paragraph) {
		this.paragraphs.add(paragraph);
		paragraph.setCarea(this);
	}

	@Override
	public String getClassName() {
		return Capabilities.ocr_carea.name();
	}

	@Override
	public String getTagName() {
		return TagNames.div.name();
	}

	@Override
	public String toString() {
		return "Carea [getId()=" + getId() + ", getBoundingBox()="
				+ getBoundingBox() + "]";
	}
}
