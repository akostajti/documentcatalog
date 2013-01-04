package net.docca.backend.util.image;

import net.docca.backend.convert.hocr.elements.Page;

import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import com.itextpdf.text.pdf.codec.TiffImage;

class TiffImageHandler extends AbstractMultipageImageHandler {

	@Override
	public Image getPageImage(Page page, byte[] imageBytes) {
		return getPageImage(page.getPageNumber().intValue(), imageBytes);
	}

	@Override
	public Image getPageImage(int pageNumber, byte[] imageBytes) {
		RandomAccessFileOrArray array = new RandomAccessFileOrArray(imageBytes);
		int pages = TiffImage.getNumberOfPages(array);
		Image image = null;
		if (pages > pageNumber) {
			image = TiffImage.getTiffImage(array, pageNumber + 1);
		}
		return image;
	}

}
