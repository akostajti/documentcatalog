package net.docca.backend.convert.hocr.elements;

import java.util.ArrayList;
import java.util.List;

import net.docca.backend.convert.hocr.Capabilities;
import net.docca.backend.convert.hocr.HocrDocument;
import net.docca.backend.convert.hocr.attributes.BoundingBox;

public class Page extends HocrElement {
	private String image;
	private Integer pageNumber;
	private HocrDocument document;
	private List<Carea> careas = new ArrayList<Carea>();

	public Page(String id, BoundingBox boundingBox, String image, Integer pageNumber) {
		super(id, boundingBox);
	}

	public String getImage() {
		return image;
	}

	public Integer getPageNumber() {
		return pageNumber;
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

	@Override
	public String getClassName() {
		return Capabilities.ocr_page.name();
	}

	@Override
	public String getTagName() {
		return TagNames.div.name();
	}
}
