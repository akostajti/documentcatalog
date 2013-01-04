package net.docca.backend.convert.hocr;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import net.docca.backend.convert.AbstractConverter;
import net.docca.backend.convert.hocr.attributes.BoundingBox;
import net.docca.backend.convert.hocr.elements.Line;
import net.docca.backend.convert.hocr.elements.Page;
import net.docca.backend.convert.hocr.elements.Word;

import org.apache.log4j.Logger;
import org.apache.sanselan.ImageInfo;
import org.apache.sanselan.Sanselan;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

public class HocrToPdfConverter extends AbstractConverter {
	private static final Logger logger = Logger.getLogger(HocrToPdfConverter.class);

	/**
	 * the default dpi value used if it cannot be derived from the image itself.
	 */
	private static final float DEFAULT_DPI = 100.0f;

	/**
	 * the default resolution of the pdf file
	 */
	private static float DEFAULT_RESOLUTION = 72.0f;

	private static final Font DEFAULT_FONT = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, CMYKColor.BLACK);

	/**
	 * just for testing
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			File inputHOCRFile = null;
			FileOutputStream outputPDFStream = null;
			try {
				inputHOCRFile = new File(args[0]);
			} catch (Exception e) {
				System.out.println("The first parameter has to be a valid URL");
				System.exit(-1);
			}
			try {
				outputPDFStream = new FileOutputStream(args[1]);
			} catch (FileNotFoundException e) {
				System.out.println("The second parameter has to be a valid URL");
				System.exit(-1);
			}
			
			new HocrToPdfConverter().convert(inputHOCRFile, outputPDFStream);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * parses and converts a hOcr file (generated by tesseract) to pdf.
	 * 
	 * @param hocr
	 * @param out
	 * @throws IOException
	 * @throws BadElementException
	 * @throws DocumentException
	 */
	public void convert(File hocr,
			FileOutputStream out) throws IOException,
			BadElementException, DocumentException {
		// parse the hocr file
		HocrParser parser = new HocrParser(hocr);
		HocrDocument document = parser.parse();

		convertToPdf(document, out);
	}

	/**
	 * converts a <code>HocrDocument</code> to pdf.
	 * 
	 * @param document
	 * @param out
	 * @throws DocumentException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public void convertToPdf(HocrDocument document, FileOutputStream out) throws DocumentException, MalformedURLException, IOException {
		Document pdf = new Document();
		PdfWriter writer = PdfWriter.getInstance(pdf, out);

		try {
			// add the pages one by one
			PageConverter pageConverter = new PageConverter(pdf, writer);
			for (Page page: document.getPages()) {
				pageConverter.addPage(page);
			}
		} finally {
			if (pdf.isOpen()) {
				pdf.close(); // always try to close the document
			}
		}
	}


	/**
	 * not threadsafe
	 * 
	 * @author Akos Tajti <akos.tajti@gmail.com>
	 *
	 */
	class PageConverter {
		private Document document;
		private PdfWriter writer;

		public PageConverter(Document document, PdfWriter writer) throws BadElementException, MalformedURLException, IOException {
			this.document = document;
			this.writer = writer;
		}

		public void addPage(Page page) throws MalformedURLException, IOException, DocumentException {
			Image image = loadImage(page);
			if (image == null) {
				logger.info("failed to load the image for page " + page);
				return;
			}
			float dotsPerPointX = image.getDpiX() > 0.0 ? image.getDpiX() : DEFAULT_DPI / DEFAULT_RESOLUTION;
			float dotsPerPointY = image.getDpiY() > 0.0 ? image.getDpiY() : DEFAULT_DPI / DEFAULT_RESOLUTION;

			setupPage(image, page, dotsPerPointX, dotsPerPointY);
			openOrAddPage();
			addImage(page, image, dotsPerPointX, dotsPerPointY);

			addLines(page, dotsPerPointX, dotsPerPointY);
		}

		/**
		 * adds the background image to the document and sets its position.
		 * 
		 * @param page
		 * @param image
		 * @param dotsPerPointX
		 * @param dotsPerPointY
		 * @throws DocumentException
		 */
		private void addImage(Page page, Image image, float dotsPerPointX,
				float dotsPerPointY) throws DocumentException {
			image.scaleToFit(page.getBoundingBox().getWidth() / dotsPerPointX, page.getBoundingBox().getHeight() / dotsPerPointY);
			image.setAbsolutePosition(0, 0);
			writer.getDirectContent().addImage(image);
		}

		/**
		 * ensures that the page is open. if not then opens it else just adds a new page.
		 */
		private void openOrAddPage() {
			if (this.document.isOpen()) {
				document.newPage();
			} else {
				document.open();
			}
		}

		/**
		 * loads the background image using the <code>image</code> property of the page. handles windows file names correctly.
		 * also handles tff files with multiple pages.
		 * @param page
		 * @return
		 */
		private Image loadImage(Page page) {
			FileInputStream stream = null;
			String filename = page.getImage();
			filename = filename.replaceAll("\"", "");
			Image image = null;
			try {
				File source = new File(filename);
				stream = new FileInputStream(source);
				byte[] inBytes = new byte[stream.available()];
				stream.read(inBytes);
				// getting all images contained in the file
				ArrayList images = Sanselan.getAllBufferedImages(inBytes);
				if (images.size() > page.getPageNumber().intValue()) {
					BufferedImage bImage = (BufferedImage) images.get(page.getPageNumber().intValue());
					ByteArrayOutputStream output = new ByteArrayOutputStream();
					try {
						ImageIO.write(bImage, "jpg", output);
						image = Image.getInstance(output.toByteArray());
					} finally {
						output.close();
					}
					// use Sanselan to get the image info and write it to the image
					ImageInfo info = Sanselan.getImageInfo(inBytes);
					image.setDpi(info.getPhysicalWidthDpi(), info.getPhysicalHeightDpi());
				}
				return image;
			} catch (Exception e) {
				logger.warn(e);
			} finally {
				if (stream != null) {
					try {
						stream.close();
					} catch (IOException e) {

					}
				}
			}
			return null;
		}

		/**
		 * sets up everything about the page (size, position etc.)
		 * 
		 * @param image
		 * @param dotsPerPointX
		 * @param dotsPerPointY
		 * @throws DocumentException
		 */
		private void setupPage(Image image, Page page, float dotsPerPointX,
				float dotsPerPointY) throws DocumentException {
			Rectangle pageRectangle = new Rectangle(page.getBoundingBox().getWidth() / dotsPerPointX, page.getBoundingBox().getHeight() / dotsPerPointY);
			logger.debug("page size for page " + page.getId() + ": " + pageRectangle);
			document.setPageSize(pageRectangle);
			logger.debug("dotsperpoint x = " + dotsPerPointX);
			logger.debug("dotsperpoint y = " + dotsPerPointY);
		}

		/**
		 * adds the lines of the page to the pdf.
		 * 
		 * @param page
		 * @param dotsPerPointX
		 * @param dotsPerPointY
		 */
		private void addLines(Page page, float dotsPerPointX,
				float dotsPerPointY) {
			PdfContentByte contentBackground = writer.getDirectContentUnder();
			List<Line> lines = page.getLines();
			for (Line line: lines) {
				contentBackground.beginText();
				contentBackground.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_INVISIBLE);

				List<Word> words = line.getWords();
				float lineHeight = line.getBoundingBox().getHeight() / dotsPerPointY;
				float fontSize = computeFontSize(line.getBoundingBox(), dotsPerPointY);
				contentBackground.setFontAndSize(DEFAULT_FONT.getBaseFont(), fontSize);

				for (Word word: words) {
					adjustCharSpacing(contentBackground, word, dotsPerPointX);

					float y = (page.getBoundingBox().getHeight() - lineHeight - line.getBoundingBox().getBottom()) / dotsPerPointY;
					float x = word.getBoundingBox().getLeft() / dotsPerPointX;

					contentBackground.showTextAligned(PdfContentByte.ALIGN_LEFT, word.getTextContent(), x, y, 0);
					logger.debug("moving word " + word + " to " + x + "," + y);
				}
				contentBackground.endText();
			}
		}

		/**
		 * computes the font size based on the bounding box of the line.
		 * 
		 * @param box
		 * @param dotsPerPointVertical
		 * @return
		 */
		private float computeFontSize(BoundingBox box, float dotsPerPointVertical) {
			float lineHeight = box.getHeight() / dotsPerPointVertical;
			float result = Math.round(lineHeight);
			if (result == 0.0f) {
				result = lineHeight;
			}

			return result;
		}

		/**
		 * adjust the character spacing. the mechanism is really simple: reduce/increase the spacing until
		 * the word width is the same as it was on the original image.
		 * 
		 * @param contentByte
		 * @param word
		 * @param dotsPerPointsHorizontal
		 */
		private void adjustCharSpacing(PdfContentByte contentByte, Word word, float dotsPerPointsHorizontal) {
			float wordWidth = word.getBoundingBox().getWidth() / dotsPerPointsHorizontal;
			float spacing = 0;
			contentByte.setCharacterSpacing(spacing);

			float textWidth = contentByte.getEffectiveStringWidth(word.getTextContent(), false);

			if (textWidth > wordWidth) {
				while (textWidth > wordWidth) {
					spacing -= 0.05;
					contentByte.setCharacterSpacing(spacing);
					float newTextWidth = contentByte.getEffectiveStringWidth(word.getTextContent(), false);
					if (newTextWidth == textWidth || spacing > -0.5) {
						break;
					}
					else {
						textWidth = newTextWidth;
					}
				}
			} else {
				while (wordWidth > textWidth) {
					spacing += 0.1;
					contentByte.setCharacterSpacing(spacing);
					float newTextWidth = contentByte.getEffectiveStringWidth(word.getTextContent(), false);
					if (newTextWidth == textWidth || spacing > 0.5) {
						break;
					} else {
						textWidth = newTextWidth;
					}
				}
			}
		}
	}
}

