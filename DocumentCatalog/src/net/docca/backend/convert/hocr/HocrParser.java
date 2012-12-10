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
import net.docca.backend.convert.hocr.elements.Page;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;

/**
 * this class does the parsing of the hocr documents. produces a <code>Document</code> object based on the contents of
 * the file.
 * 
 * @author akostajti
 *
 */
public class HocrParser {
	private static final String CLASS = "class";
	private static final String TITLE = "title";
	private static final String ID = "id";

	private static final Pattern bboxPattern = Pattern.compile("bbox(\\s+\\d+){4}");
	private static final Pattern coordinatePattern = Pattern.compile("(-?\\d+)\\s+(-?\\d+)\\s+(-?\\d+)\\s+(-?\\d+)");

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

	}

	private Page parsePage(StartTag pageTag) {
		Element element = pageTag.getElement();

		// create a page element from the attributes (except the image attribute
		String id = element.getAttributeValue(ID);
		BoundingBox bbox = parseBoundingBox(element);
		// TODO: parse the page number then the carea-s
		return page;
	}

	private BoundingBox parseBoundingBox(Element element) {
		String title = element.getAttributeValue(TITLE);
		if (title == null) {
			return null;
		}

		Matcher bboxMatcher = bboxPattern.matcher(title);
		if (!bboxMatcher.find()) {
			return null;
		}

		Matcher coordinateMatcher = coordinatePattern.matcher(bboxMatcher.group());
		if (!coordinateMatcher.find()) {
			return null;
		}

		return new BoundingBox(Integer.parseInt(coordinateMatcher.group(1)), Integer.parseInt(coordinateMatcher.group(2)), 
				Integer.parseInt(coordinateMatcher.group(2)), Integer.parseInt(coordinateMatcher.group(4)));

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

