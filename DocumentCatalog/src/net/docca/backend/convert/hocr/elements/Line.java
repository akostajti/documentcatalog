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

	@Override
	public String getClassName() {
		return Capabilities.ocr_line.name();
	}

	@Override
	public String getTagName() {
		return TagNames.span.name();
	}

}
