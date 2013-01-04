package net.docca.backend.util.image;

import net.docca.backend.convert.hocr.elements.Page;

import com.itextpdf.text.Image;

public interface MultipageImageHandler {
	Image getPageImage(Page page, byte[] imageBytes);

	Image getPageImage(int pageNumber, byte[] imageBytes);
}
