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

	private String id;
	private BoundingBox boundingBox;

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
