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

	public int getHeight() {
		return top - bottom;
	}

	public int getWidth() {
		return right - left;
	}

	@Override
	public String toString() {
		return "BoundingBox [left=" + left + ", bottom=" + bottom + ", right="
				+ right + ", top=" + top + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bottom;
		result = prime * result + left;
		result = prime * result + right;
		result = prime * result + top;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BoundingBox other = (BoundingBox) obj;
		if (bottom != other.bottom)
			return false;
		if (left != other.left)
			return false;
		if (right != other.right)
			return false;
		if (top != other.top)
			return false;
		return true;
	}
}
