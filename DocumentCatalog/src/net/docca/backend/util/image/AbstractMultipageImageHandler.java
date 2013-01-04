package net.docca.backend.util.image;

import java.util.HashMap;
import java.util.Map;

import org.apache.sanselan.ImageFormat;

public abstract class AbstractMultipageImageHandler implements
		MultipageImageHandler {

	private static final Map<ImageFormat, MultipageImageHandler> handlers = new HashMap<ImageFormat, MultipageImageHandler>();
	static {
		handlers.put(ImageFormat.IMAGE_FORMAT_TIFF, new TiffImageHandler());
	}
	public static MultipageImageHandler getHandlerForFormat(ImageFormat format) {
		return handlers.get(format);
	}

	public static boolean isMultipageFormat(ImageFormat format) {
		if (format == null) {
			return false;
		}

		if (format.equals(ImageFormat.IMAGE_FORMAT_TIFF)) {
			return true;
		}
		return false;
	}
}
