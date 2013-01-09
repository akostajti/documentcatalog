package net.docca.backend.convert.hocr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.exceptions.InvalidPdfException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

@Test(groups = {"mustrun", "hocr"})
public class HocrToPdfConverterTest {

	private File file;
	private File multipageFile;
	private File _image;

	/**
	 *  md5 hashes of the page contents of the multipage file.
	 */
	private static final byte[] FIRST_PAGE_DIGEST =
			new byte[]{-44, 48, 21, 25, -14, 117, 103, -58, 118, -100, -55, 101, 107, 13, -48, 86};
	/**
	 *  md5 hashes of the page contents of the multipage file.
	 */
	private static final byte[] SECOND_PAGE_DIGEST =
			new byte[]{69, -119, -107, -114, -64, 74, 24, -97, 43, 44, -104, -101, 44, -40, -77, -24};

	@BeforeTest
	public void createInputFiles() throws IOException {
		file = createInputFile("hocr/hocr-test1.html", "hocr/scanned1.jpg",
				"\"d:\\scanned documents\\bazso\\felszolitas.jpg\"");
		multipageFile = createInputFile("hocr/hocr-multipage.html", "hocr/multipage.tif", "\"d:\\image.tif\"");
	}

	/**
	 * gets a hocr file and replaces all occurrences of <code>oldImageFile</code> to <code>imageFile</code>.
	 * @param hocrFile
	 * @param imageFile
	 * @param oldImageFile
	 * @return returns the newly created temporary file.
	 * @throws IOException
	 */
	private File createInputFile(final String hocrFile, final String imageFile,
			final String oldImageFile) throws IOException {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream(hocrFile);
		InputStream imageStream = this.getClass().getClassLoader().getResourceAsStream(imageFile);
		_image = File.createTempFile("image", ".jpg");
		IOUtils.copy(imageStream, new FileOutputStream(_image));
		byte[] bytes = new byte[in.available()];
		in.read(bytes);
		String content = new String(bytes, Charset.forName("utf8"));
		content = content.replace(oldImageFile, _image.getAbsolutePath());
		File inputFile = File.createTempFile("doccat", ".html");
		FileUtils.write(inputFile, content);

		return inputFile;
	}

	@Test
	public void testConvert() throws IOException, DocumentException {
		String expectedContent = "Cimzett/Ados: Komlosi Zoltan Imre "
				+ "Felszabadulas Budapest 3000 utca 6. "
				+ "Mate Felado/Kovetelo: Budapest Akos "
				+ "Tisztelt Komlosi Zoltan Imre! Miklos 2000 Kelt: Budapest, utca 12., 2012.04.17. 8/46";
		File pdf = File.createTempFile("test", ".pdf");
		new HocrToPdfConverter().convert(file, new FileOutputStream(pdf));
		InputStream stream = FileUtils.openInputStream(pdf);

		// check the file size
		Assert.assertTrue(stream.available() > 0);

		PdfReader reader = new PdfReader(stream);
		Assert.assertEquals(reader.getNumberOfPages(), 1);
		Assert.assertEquals(PdfTextExtractor.getTextFromPage(reader, 1).replace('\n', ' '), expectedContent);
	}

	@Test
	public void testMultipageConvert() throws IOException, DocumentException, NoSuchAlgorithmException {
		File pdf = File.createTempFile("test", ".pdf");
		new HocrToPdfConverter().convert(multipageFile, new FileOutputStream(pdf));

		PdfReader reader = new PdfReader(new FileInputStream(pdf));

		// check the number of pages
		Assert.assertEquals(reader.getNumberOfPages(), 2);

		MessageDigest md5 = MessageDigest.getInstance("MD5");
		String firstPageContent = PdfTextExtractor.getTextFromPage(reader, 1);
		Assert.assertEquals(md5.digest(firstPageContent.getBytes()), FIRST_PAGE_DIGEST);

		String secondPageContent = PdfTextExtractor.getTextFromPage(reader, 2);
		Assert.assertEquals(md5.digest(secondPageContent.getBytes()), SECOND_PAGE_DIGEST);
	}

	@Test
	public void testConvertWithInvalidImagePath() throws IOException, DocumentException {
		file = createInputFile("hocr/hocr-test1.html", "hocr/scanned1.jpg",
				"\"d:\\scanned documents\\bazsó\\felszólítás.jpg\"");
		File pdf = File.createTempFile("test", ".pdf");
		String content = FileUtils.readFileToString(file);
		content = content.replaceAll(_image.getAbsolutePath().replace("\\", "\\\\"), "zzzhz");
		FileUtils.write(file, content);
		new HocrToPdfConverter().convert(file, new FileOutputStream(pdf));

		try {
			new PdfReader(new FileInputStream(pdf));
			Assert.fail("the result is not a pdf document so an exception must be thrown");
		} catch (InvalidPdfException ex) {

		}

		// the file must be empty
		FileInputStream in = null;
		try {
			in = new FileInputStream(pdf);
			Assert.assertEquals(in.available(), 0);
		} finally {
			if (in != null) {
				in.close();
			}
		}
	}
}
