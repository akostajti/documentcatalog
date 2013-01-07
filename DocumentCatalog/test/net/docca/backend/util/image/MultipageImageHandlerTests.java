package net.docca.backend.util.image;

import java.io.IOException;
import java.io.InputStream;

import org.apache.sanselan.ImageFormat;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.itextpdf.text.Image;

public class MultipageImageHandlerTests {

	@Test
	public void testGetPageImage() throws IOException, ImageReadException {
		InputStream imageStream = this.getClass().getClassLoader().getResourceAsStream("hocr/multipage.tif");
		byte[] bytes = new byte[imageStream.available()];
		imageStream.read(bytes);
		ImageFormat format = Sanselan.guessFormat(bytes);

		MultipageImageHandler handler = AbstractMultipageImageHandler.getHandlerForFormat(format);
		Image image = null;

		for (int i = 0; i < 3; i++) {
			image = handler.getPageImage(i, bytes);
			Assert.assertNotNull(image);

			Assert.assertEquals((int) image.getWidth(), 1696);
			Assert.assertEquals((int) image.getHeight(), 2480);
		}

		// negative tests
		image = handler.getPageImage(3, bytes);
		Assert.assertNull(image);

		try {
			image = handler.getPageImage(-1, bytes);
			Assert.fail("an exception must be thrown if the page number is less than 0");
		} catch (IllegalArgumentException e) {
			
		}
	}

	@Test
	public void testIsMultipageFormat() {
		Assert.assertTrue(AbstractMultipageImageHandler.isMultipageFormat(ImageFormat.IMAGE_FORMAT_TIFF));
		Assert.assertFalse(AbstractMultipageImageHandler.isMultipageFormat(ImageFormat.IMAGE_FORMAT_BMP));
		Assert.assertFalse(AbstractMultipageImageHandler.isMultipageFormat(ImageFormat.IMAGE_FORMAT_JPEG));
		Assert.assertFalse(AbstractMultipageImageHandler.isMultipageFormat(ImageFormat.IMAGE_FORMAT_PNG));
	}

	@Test
	public void testGetHandlerForFormat() {
		Assert.assertNotNull(AbstractMultipageImageHandler.getHandlerForFormat(ImageFormat.IMAGE_FORMAT_TIFF));
		Assert.assertNull(AbstractMultipageImageHandler.getHandlerForFormat(ImageFormat.IMAGE_FORMAT_JPEG));
		Assert.assertNull(AbstractMultipageImageHandler.getHandlerForFormat(ImageFormat.IMAGE_FORMAT_BMP));
		Assert.assertNull(AbstractMultipageImageHandler.getHandlerForFormat(ImageFormat.IMAGE_FORMAT_PNG));
	}
}
