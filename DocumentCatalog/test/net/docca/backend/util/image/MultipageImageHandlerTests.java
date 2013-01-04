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
		Image image = handler.getPageImage(0, bytes);
		Assert.assertNotNull(image);

		image = handler.getPageImage(1, bytes);
		Assert.assertNotNull(image);

		image = handler.getPageImage(2, bytes);
		Assert.assertNotNull(image);

		image = handler.getPageImage(3, bytes);
		Assert.assertNull(image);
	}

	@Test
	public void testIsMultipageFormat() {
		Assert.assertTrue(AbstractMultipageImageHandler.isMultipageFormat(ImageFormat.IMAGE_FORMAT_TIFF));
		Assert.assertFalse(AbstractMultipageImageHandler.isMultipageFormat(ImageFormat.IMAGE_FORMAT_BMP));
		Assert.assertFalse(AbstractMultipageImageHandler.isMultipageFormat(ImageFormat.IMAGE_FORMAT_JPEG));
		Assert.assertFalse(AbstractMultipageImageHandler.isMultipageFormat(ImageFormat.IMAGE_FORMAT_PNG));
	}
}
