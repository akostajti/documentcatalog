package net.docca.backend.convert.hocr.attributes;

public class BoundingBox implements HocrAttribute {
	private int left;
	private int bottom;
	private int right;
	private int top;

	public BoundingBox(int left, int bottom, int right, int top) {
		super();
		this.left = left;
		this.bottom = bottom;
		this.right = right;
		this.top = top;
	}

	public int getLeft() {
		return left;
	}

	public int getBottom() {
		return bottom;
	}

	public int getRight() {
		return right;
	}

	public int getTop() {
		return top;
	}

	@Override
	public String toString() {
		return "BoundingBox [left=" + left + ", bottom=" + bottom + ", right="
				+ right + ", top=" + top + "]";
	}
}
