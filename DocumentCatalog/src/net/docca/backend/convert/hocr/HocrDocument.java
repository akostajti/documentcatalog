package net.docca.backend.convert.hocr;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.docca.backend.convert.hocr.elements.Page;

public class HocrDocument {
	private String contentType;
	private String ocrSystem;
	private List<Page> pages = new ArrayList<Page>();
	private Set<Capabilities> capabilities = new HashSet<Capabilities>();

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getOcrSystem() {
		return ocrSystem;
	}

	public void setOcrSystem(String ocrSystem) {
		this.ocrSystem = ocrSystem;
	}

	public List<Page> getPages() {
		return pages;
	}

	public void setPages(List<Page> pages) {
		this.pages = pages;
	}

	public void addPage(Page page) {
		pages.add(page);
		page.setDocument(this);
	}

	public Set<Capabilities> getCapabilities() {
		return capabilities;
	}

	public void setCapabilities(Set<Capabilities> capabilities) {
		this.capabilities = capabilities;
	}

	public void addCapability(Capabilities capability) {
		capabilities.add(capability);
	}
}

