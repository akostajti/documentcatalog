package net.docca.backend.convert.hocr.elements;

import java.util.ArrayList;
import java.util.List;

import net.docca.backend.convert.hocr.Capabilities;
import net.docca.backend.convert.hocr.attributes.BoundingBox;
import net.docca.backend.convert.hocr.attributes.TextDirection;

public class Paragraph extends HocrElement {
	private Carea carea;
	private TextDirection direction = TextDirection.ltr;
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
