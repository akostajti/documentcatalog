package net.docca.backend.convert.hocr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.exceptions.InvalidPdfException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

@Test(groups = {"mustrun", "hocr"})
public class HocrToPdfConverterTest {

	private File file;
	private File multipageFile;
	private File _image;

	/* md5 hashes of the page contents of the multipage file */
	private static final byte[] firstPageDigest = new byte[]{81, 4, 46, 50, 25, -2, -75, 101, 52, -39, -10, -67, 84, 66, 110, 114};
	private static final byte[] secondPageDigest = new byte[]{-75, 45, 77, -21, -107, 57, 112, -39, 62, 103, -71, -77, 121, 46, 16, -116};

	@BeforeTest
	public void createInputFiles() throws IOException {
		file = createInputFile("hocr/hocr-test1.html", "hocr/scanned1.jpg", "\"d:\\scanned documents\\bazsó\\felszólítás.jpg\"");
		multipageFile = createInputFile("hocr/hocr-multipage.html", "hocr/multipage.tif", "\"d:\\image.tif\"");
	}

	private File createInputFile(String hocrFile, String imageFile, String oldImageFile) throws IOException {
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
	public void testConvert() throws BadElementException, FileNotFoundException, IOException, DocumentException {
		String expectedContent = "Címzett/Adós: Komlósi Zoltán Imre "+
				"Felszabadulás Budapest 3000 utca 6. " +
				"Maté Feladó/Követel: Budapest Ákos " +
				"Tisztelt Komlósi Zoltán Imre! Miklós 2000 Kelt: Budapest, utca 12., 2012.04.17. 8/46";
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
	public void testMultipageConvert() throws IOException, BadElementException, DocumentException, NoSuchAlgorithmException {
		File pdf = File.createTempFile("test", ".pdf");
		new HocrToPdfConverter().convert(multipageFile, new FileOutputStream(pdf));

		PdfReader reader = new PdfReader(new FileInputStream(pdf));

		// check the number of pages
		Assert.assertEquals(reader.getNumberOfPages(), 2);

		MessageDigest md5 = MessageDigest.getInstance("MD5");
		String firstPageContent = PdfTextExtractor.getTextFromPage(reader, 1);
		Assert.assertEquals(md5.digest(firstPageContent.getBytes()), firstPageDigest);

		String secondPageContent = PdfTextExtractor.getTextFromPage(reader, 2);
		Assert.assertEquals(md5.digest(secondPageContent.getBytes()), secondPageDigest);
	}

	@Test
	public void testConvertWithInvalidImagePath() throws IOException, BadElementException, DocumentException {
		file = createInputFile("hocr/hocr-test1.html", "hocr/scanned1.jpg", "\"d:\\scanned documents\\bazsó\\felszólítás.jpg\"");
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
			in.close();
		}
	}
}
