package net.docca.backend.convert.hocr;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

@Test(groups = {"mustrun", "hocr"})
public class HocrToPdfConverterTest {

	private File inputFile;
	private File image;

	@BeforeTest
	public void createInputFile() throws IOException {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("hocr/hocr-test1.html");
		InputStream imageStream = this.getClass().getClassLoader().getResourceAsStream("hocr/scanned1.jpg");
		image = File.createTempFile("image", ".jpg");
		IOUtils.copy(imageStream, new FileOutputStream(image));
		byte[] bytes = new byte[in.available()];
		in.read(bytes);
		String content = new String(bytes, Charset.forName("utf8"));
		content = content.replace("\"d:\\scanned documents\\bazsó\\felszólítás.jpg\"", image.getAbsolutePath());
		inputFile = File.createTempFile("doccat", ".html");
		FileUtils.write(inputFile, content);
	}

	@Test
	public void testConvert() throws BadElementException, FileNotFoundException, IOException, DocumentException {
		String expectedContent = "Címzett/Adós: Komlósi Zoltán Imre "+
				"Felszabadulás Budapest 3000 utca 6. " +
				"Maté Feladó/Követel: Budapest Ákos " +
				"Tisztelt Komlósi Zoltán Imre! Miklós 2000 Kelt: Budapest, utca 12., 2012.04.17. 8/46";
		File pdf = File.createTempFile("test", ".pdf");
		new HocrToPdfConverter().convert(inputFile, new FileOutputStream(pdf));
		InputStream stream = FileUtils.openInputStream(pdf);

		// check the file size
		Assert.assertTrue(stream.available() > 0);

		PdfReader reader = new PdfReader(stream);
		Assert.assertEquals(reader.getNumberOfPages(), 1);
		Assert.assertEquals(PdfTextExtractor.getTextFromPage(reader, 1).replace('\n', ' '), expectedContent);
	}
}
