package net.docca.backend.convert.hocr;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.docca.backend.convert.hocr.attributes.BoundingBox;
import net.docca.backend.convert.hocr.attributes.TextDirection;
import net.docca.backend.convert.hocr.elements.Line;
import net.docca.backend.convert.hocr.elements.Page;
import net.docca.backend.convert.hocr.elements.Paragraph;
import net.docca.backend.convert.hocr.elements.Word;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * test for hocr parser. tests using one single file (testResources/hocr/hocr-test1.html")
 * 
 * @author akostajti
 *
 */
@Test(groups = {"mustrun", "hocr"})
public class HocrParserTest {
	@Test
	public void testParse() throws IOException {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("hocr/hocr-test1.html");
		HocrParser parser = new HocrParser(in);
		HocrDocument document = parser.parse();

		// check the document metadata first
		Assert.assertEquals("text/html; charset=utf-8", document.getContentType());
		Assert.assertEquals("tesseract 3.02", document.getOcrSystem());
		Assert.assertTrue(document.getCapabilities().containsAll(Arrays.asList(new Capabilities[]{Capabilities.ocr_page,
				Capabilities.ocr_carea,
				Capabilities.ocr_par,
				Capabilities.ocr_line,
				Capabilities.ocrx_word})
			)
		);
		Assert.assertEquals(document.getCapabilities().size(), 5);

		// check the counts
		List<Page> pages = document.getPages();
		Page page = pages.get(0);
		Assert.assertEquals(pages.size(), 1);
		Assert.assertEquals(page.getCareas().size(), 3);
		Assert.assertEquals(page.getParagraphs().size(), 6);
		Assert.assertEquals(page.getLines().size(), 12);
		int wordCount = 0;
		for (Line line: page.getLines()) {
			wordCount += line.getWords().size();
		}
		Assert.assertEquals(wordCount, 25);

		// check the page properties
		Assert.assertEquals(page.getId(), "page_1");
		Assert.assertEquals(page.getPageNumber().intValue(), 0);
		Assert.assertEquals(page.getImage(), "\"d:\\scanned documents\\bazsó\\felszólítás.jpg\"");
		Assert.assertEquals(page.getBoundingBox(), new BoundingBox(0, 0, 1698, 2336));

		assertParagraphs(page.getParagraphs());
		assertLines(page.getLines());

		List<Word> words = new ArrayList<Word>();
		for (Line line: page.getLines()) {
			words.addAll(line.getWords());
		}
		assertWords(words);
	}

	private void assertParagraphs(List<Paragraph> paragraphs) {
		int [][] coordinates = {{223, 204, 426, 228},
				{223, 298, 487, 332},
				{223, 351, 350, 375},
				{222, 406, 478, 431},
				{223, 461, 1211, 839},
				{320, 870, 1286, 953}
		};

		for (int i = 0; i < paragraphs.size(); i++) {
			int[] bbox = coordinates[i];
			Paragraph paragraph = paragraphs.get(i);
			Assert.assertEquals(paragraph.getBoundingBox(), createBoundingBox(bbox));
			Assert.assertEquals(paragraph.getDirection(), TextDirection.ltr);
			Assert.assertEquals(paragraph.getId(), "par_" + (i + 1));
		}
	}

	private Object createBoundingBox(int[] bbox) {
		return new BoundingBox(bbox[0], bbox[1], bbox[2], bbox[3]);
	}

	private void assertLines(List<Line> lines) {
		int[][] coordinates = {{223,204,426,228},{223,298,487,332},{223,351,350,375},{222,406,478,431},
				{223,461,287,485},{912,558,1151,583},{911,645,1061,682},{912,705,1039,729},
				{911,760,1211,790},{911,815,976,839},{911,870,1286,901},{320,924,745,953}};
		for (int i = 0; i < lines.size(); i++) {
			Line line = lines.get(i);
			Assert.assertEquals(line.getBoundingBox(), createBoundingBox(coordinates[i]));
			Assert.assertEquals(line.getId(), "line_" + (i + 1));
		}
		assertTextContentIsOk(lines);
	}

	private void assertTextContentIsOk(List<Line> lines) {
		String[] lineContents = {"Címzett/Adós:",
			"Komlósi Zoltán Imre",
			"Budapest",
			"Felszabadulás utca 6.",
			"3000",
			"Feladó/Követelő:",
			"Maté Ákos",
			"Budapest",
			"Miklós utca 12., 8/46",
			"2000",
			"Kelt: Budapest, 2012.04.17.",
			"Tisztelt Komlósi Zoltán Imre!"
		};

		for (int i = 0; i < lines.size(); i++) {
			Assert.assertEquals(lines.get(i).getTextContent(), lineContents[i]);
		}
	}

	private void assertWords(List<Word> words) {
		int[][] coordinates = {{223,204,426,228},{223,298,305,321},{315,298,408,323},{413,298,487,332},
				{223,351,350,375},{222,406,383,431},{392,411,448,431},{456,407,478,431},
				{223,461,287,485},{912,558,1151,583},{911,651,981,682},{989,645,1061,676},
				{912,705,1039,729},{911,760,1021,785},{1029,764,1086,785},{1097,761,1142,790},
				{1153,761,1211,786},{911,815,976,839},{911,870,975,895},{985,871,1120,901},
				{1130,872,1286,898},{320,924,437,952},{446,925,541,953},{551,925,653,953},
				{663,926,745,953}
		};
		String[] textContents = {"Címzett/Adós:","Komlósi","Zoltán","Imre","Budapest","Felszabadulás","utca","6.","3000","Feladó/Követelő:",
				"Maté","Ákos","Budapest","Miklós","utca","12.,","8/46","2000","Kelt:","Budapest,","2012.04.17.","Tisztelt","Komlósi","Zoltán","Imre!"};

		for (int i = 0; i < words.size(); i++) {
			Word word = words.get(i);
			Assert.assertEquals(word.getBoundingBox(), createBoundingBox(coordinates[i]));
			Assert.assertEquals(word.getId(), "word_" + (i + 1));
			Assert.assertEquals(word.getTextContent(), textContents[i]);
		}
	}
}
