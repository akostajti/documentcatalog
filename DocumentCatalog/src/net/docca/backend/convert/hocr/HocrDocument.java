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
package net.docca.backend.convert.hocr;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.docca.backend.convert.hocr.elements.Page;
import net.docca.backend.search.Indexable;

/**
 * represents a hocr document parsed from a hocr file.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
public class HocrDocument implements Indexable {
	/**
	 * content type of the original document.
	 */
	private String contentType;
	/**
	 * the ocr application that created the ocr file.
	 */
	private String ocrSystem;
	/**
	 * the list of page in the document.
	 */
	private List<Page> pages = new ArrayList<Page>();
	/**
	 * the capabilities supported by the original document.
	 */
	private Set<Capabilities> capabilities = new HashSet<Capabilities>();

	public final String getContentType() {
		return contentType;
	}

	public final void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public final String getOcrSystem() {
		return ocrSystem;
	}

	public final void setOcrSystem(String ocrSystem) {
		this.ocrSystem = ocrSystem;
	}

	public final List<Page> getPages() {
		return pages;
	}

	public final void setPages(List<Page> pages) {
		this.pages = pages;
	}

	public final void addPage(Page page) {
		pages.add(page);
		page.setDocument(this);
	}

	public final Set<Capabilities> getCapabilities() {
		return capabilities;
	}

	public final void setCapabilities(Set<Capabilities> capabilities) {
		this.capabilities = capabilities;
	}

	/**
	 * adds a capability to the document.
	 *
	 * @param capability
	 */
	public final void addCapability(Capabilities capability) {
		capabilities.add(capability);
	}

	@Override
	public final String toString() {
		return "HocrDocument [contentType=" + contentType + ", ocrSystem="
				+ ocrSystem + ", capabilities=" + capabilities + "]";
	}

	@Override
	public final Map<String, Object> getProperties() {
		// TODO Auto-generated method stub
		return null;
	}
}

