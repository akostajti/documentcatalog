package net.docca.backend.convert.hocr.elements;

import net.docca.backend.convert.hocr.Capabilities;
import net.docca.backend.convert.hocr.attributes.BoundingBox;

public class Word extends HocrElement {

	public Word(String id, BoundingBox boundingBox) {
		super(id, boundingBox);
	}

	@Override
	public String getClassName() {
		return Capabilities.ocrx_word.name();
	}

	@Override
	public String getTagName() {
		return TagNames.span.name();
	}

}
