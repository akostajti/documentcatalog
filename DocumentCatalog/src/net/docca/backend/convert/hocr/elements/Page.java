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
import net.docca.backend.convert.hocr.HocrDocument;
import net.docca.backend.convert.hocr.attributes.BoundingBox;

/**
 * represents a page in a hocr document.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
public class Page extends HocrElement {
	/**
	 * the path of the image from which this page was generate.
	 */
	private final String image;

	/**
	 * the pagenumber.
	 */
	private Integer pageNumber;

	/**
	 * the document that contains this page.
	 */
	private HocrDocument document;

	/**
	 * the <code>carea</code> elements of this page.
	 */
	private List<Carea> careas = new ArrayList<Carea>();

	/**
	 * constructor for setting all fields.
	 * @param id the id
	 * @param boundingBox the bounding box
	 * @param image the image
	 * @param pageNumber the page number
	 */
	public Page(final String id, final BoundingBox boundingBox, final String image, final Integer pageNumber) {
		super(id, boundingBox);
		this.image = image;
		this.pageNumber = pageNumber;
	}

	public String getImage() {
		return image;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public final void setPageNumber(final Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public HocrDocument getDocument() {
		return document;
	}

	public void setDocument(HocrDocument document) {
		this.document = document;
	}

	public List<Carea> getCareas() {
		return careas;
	}

	public void setCareas(List<Carea> careas) {
		this.careas = careas;
	}

	public void addCarea(Carea carea) {
		this.careas.add(carea);
		carea.setPage(this);
	}

	public List<Paragraph> getParagraphs() {
		List<Paragraph> result = new ArrayList<Paragraph>();

		for (Carea carea: this.careas) {
			result.addAll(carea.getParagraphs());
		}

		return result;
	}

	public List<Line> getLines() {
		List<Line> result = new ArrayList<Line>();
		List<Paragraph> paragraphs = getParagraphs();

		for (Paragraph carea: paragraphs) {
			result.addAll(carea.getLines());
		}

		return result;
	}

	/**
	 * computes the textual content of this page without preserving the structure.
	 * @return the textual content (that is the paragraphs concatenated)
	 */
	public final String getTextContent() {
		if (getParagraphs() == null) {
			return "";
		}

		StringBuilder builder = new StringBuilder();
		for (Paragraph paragraph: getParagraphs()) {
			builder.append(paragraph.getTextContent()).append(" ");
		}
		return builder.toString();
	}

	@Override
	public String getClassName() {
		return Capabilities.ocr_page.name();
	}

	@Override
	public String getTagName() {
		return TagNames.div.name();
	}

	@Override
	public String toString() {
		return "Page [image=" + image + ", pageNumber=" + pageNumber
				+ ", getId()=" + getId() + ", getBoundingBox()="
				+ getBoundingBox() + "]";
	}
}

