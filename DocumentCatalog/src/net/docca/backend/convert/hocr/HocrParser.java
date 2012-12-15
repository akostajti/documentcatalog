package net.docca.backend.convert.hocr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.docca.backend.convert.hocr.attributes.BoundingBox;
import net.docca.backend.convert.hocr.attributes.TextDirection;
import net.docca.backend.convert.hocr.elements.Carea;
import net.docca.backend.convert.hocr.elements.Line;
import net.docca.backend.convert.hocr.elements.Page;
import net.docca.backend.convert.hocr.elements.Paragraph;
import net.docca.backend.convert.hocr.elements.Word;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;

import org.apache.log4j.Logger;

/**
 * this class does the parsing of the hocr documents. produces a <code>Document</code> object based on the contents of
 * the file.
 * 
 * @author akostajti
 *
 */
public class HocrParser {
	private static final Logger logger = Logger.getLogger(HocrParser.class);

	private static final String CLASS = "class";
	private static final String TITLE = "title";
	private static final String ID = "id";
	private static final String DIR = "dir";

	private static final Pattern bboxPattern = Pattern.compile("bbox(\\s+\\d+){4}");
	private static final Pattern coordinatePattern = Pattern.compile("(-?\\d+)\\s+(-?\\d+)\\s+(-?\\d+)\\s+(-?\\d+)");
	private static final Pattern pageNumberPattern = Pattern.compile("ppageno\\s+(\\d+)");
	private static final Pattern imagePattern = Pattern.compile("image\\s+([^;]+)");


	private InputStream stream;

	public HocrParser(InputStream stream) {
		this.stream = stream;
	}

	public HocrParser(File file) throws FileNotFoundException {
		this(new FileInputStream(file));
	}

	public HocrParser(String fileName) throws FileNotFoundException {
		this(new File(fileName));
	}

	public HocrDocument parse() throws IOException {
		Source source = new Source(this.stream);

		HocrDocument document = createDocumentWithMetaInfo(source);

		StartTag pageTag = source.getNextStartTag(0, CLASS, Capabilities.ocr_page.name(), false);
		while(pageTag != null) {
			document.addPage(parsePage(pageTag));
			pageTag = source.getNextStartTag(pageTag.getEnd(), CLASS, Capabilities.ocr_page.name(), false);
		}

		return document;
	}

	private Page parsePage(StartTag pageTag) {
		Element element = pageTag.getElement();

		// create a page element from the attributes (except the image attribute
		String id = element.getAttributeValue(ID);
		BoundingBox bbox = parseBoundingBox(element);
		Integer pageNumber = parsePageNumber(element);
		String image = parseImage(element);

		Page page = new Page(id, bbox, image, pageNumber); // TODO: parse the image, too

		// read the careas from the page
		List<StartTag> careas = element.getAllStartTagsByClass(Capabilities.ocr_carea.name());
		for (StartTag carea: careas) {
			page.addCarea(parseCarea(carea));
		}
		return page;
	}

	private String parseImage(Element element) {
		String title = element.getAttributeValue(TITLE);
		if (title == null) {
			return null;
		}

		Matcher matcher = imagePattern.matcher(title);
		if (!matcher.find()) {
			return null;
		}

		return matcher.group(1);
	}

	private Carea parseCarea(StartTag startTag) {
		Element element = startTag.getElement();

		String id = element.getAttributeValue(ID);
		BoundingBox bbox = parseBoundingBox(element);

		Carea carea = new Carea(id, bbox);

		// read the paragraphs in the carea
		List<StartTag> paragraphs = element.getAllStartTagsByClass(Capabilities.ocr_par.name());
		for (StartTag paragraph: paragraphs) {
			carea.addParagraph(parseParagraph(paragraph));
		}

		return carea;
	}

	private Paragraph parseParagraph(StartTag startTag) {
		Element element = startTag.getElement();

		String id = element.getAttributeValue(ID);
		BoundingBox bbox = parseBoundingBox(element);
		TextDirection dir = TextDirection.valueOf(element.getAttributeValue(DIR));

		Paragraph paragraph = new Paragraph(id, bbox, dir);

		List<StartTag> lines = element.getAllStartTagsByClass(Capabilities.ocr_line.name());
		for (StartTag line: lines) {
			paragraph.addLine(parseLine(line));
		}

		return paragraph;
	}

	private Line parseLine(StartTag startTag) {
		Element element = startTag.getElement();

		String id = element.getAttributeValue(ID);
		BoundingBox bbox = parseBoundingBox(element);

		Line line = new Line(id, bbox);

		List<StartTag> words = element.getAllStartTagsByClass(Capabilities.ocrx_word.name());
		for (StartTag word: words) {
			line.addWord(parseWord(word));
		}

		return line;
	}

	private Word parseWord(StartTag startTag) {
		Element element = startTag.getElement();

		String id = element.getAttributeValue(ID);
		BoundingBox bbox = parseBoundingBox(element);
		String textContent = element.getTextExtractor().toString();

		return new Word(id, bbox, textContent);
	}

	private Integer parsePageNumber(Element element) {
		String title = element.getAttributeValue(TITLE);
		if (title == null) {
			return null;
		}

		Matcher matcher = pageNumberPattern.matcher(title);
		if (!matcher.find()) {
			return null;
		}

		return Integer.valueOf(matcher.group(1));
	}

	private BoundingBox parseBoundingBox(Element element) {
		String title = element.getAttributeValue(TITLE);
		if (title == null) {
			logger.debug("title is empty for element [" + element + "]");
			return null;
		}

		Matcher bboxMatcher = bboxPattern.matcher(title);
		if (!bboxMatcher.find()) {
			logger.debug("couldn't parse bbox from title [" + title + "]");
			return null;
		}

		Matcher coordinateMatcher = coordinatePattern.matcher(bboxMatcher.group());
		if (!coordinateMatcher.find()) {
			logger.debug("couldn't parse bbox from title [" + title + "]");
			return null;
		}

		BoundingBox bbox = new BoundingBox(Integer.parseInt(coordinateMatcher.group(1)), Integer.parseInt(coordinateMatcher.group(2)), 
				Integer.parseInt(coordinateMatcher.group(3)), Integer.parseInt(coordinateMatcher.group(4)));

		logger.debug("parsed bbox properties [" + bbox + "] from title [" + title + "]");
		return bbox;

	}

	/**
	 * Initializes a hocr document using the meta information in the file (capabilities, encoding and ocr system)
	 * 
	 * @param source
	 * @return
	 */
	private HocrDocument createDocumentWithMetaInfo(Source source) { // TODO: is this effective?
		HocrDocument document = new HocrDocument();
		// list all meta tags
		List<Element> metaTags = source.getAllElements("meta");
		if (metaTags != null) {
			for (Element element: metaTags) {
				// find the content type
				String httpEquiv = element.getAttributeValue("http-equiv");
				if ("ContentType".equals(httpEquiv)) {
					document.setContentType(element.getAttributeValue("content"));
					continue;
				}

				String name = element.getAttributeValue("name");
				if (name != null) {
					if (name.equalsIgnoreCase("ocr-system")) {
						document.setOcrSystem(element.getAttributeValue("content"));
					} else if (name.equalsIgnoreCase("ocr-capabilities")) {
						document.setCapabilities(parseCapabilities(element.getAttributeValue("content")));
					}
				}
			}
		}
		return document;
	}

	private Set<Capabilities> parseCapabilities(String attributeValue) {
		Set<Capabilities> capabilities = new HashSet<Capabilities>();
		if (attributeValue == null) {
			return capabilities;
		}

		String[] asString; 
		if (attributeValue.contains(" ")) {
			asString = attributeValue.split(" ");
		} else {
			asString = new String[]{ attributeValue };
		}

		for (String cap: asString) {
			capabilities.add(Capabilities.valueOf(cap)); // TODO: handle invalid/unknown values values
		}

		return capabilities;
	}
}

