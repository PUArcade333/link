package puArcade.princetonTD.grid;

import android.graphics.Point;

public class Node extends Point {

	private boolean active = false;

	protected final int NODE_WIDTH;

	Node(int x, int y, int pixel)
	{
		this.x = center(x, pixel);
		this.y = center(y, pixel);
		this.NODE_WIDTH = pixel;
	}

	Node(Node node)
	{
		this.x = node.x;
		this.y = node.y;
		this.NODE_WIDTH = node.NODE_WIDTH;
		this.active = node.active;
	}

	public boolean isActive()
	{
		return active;
	}

	public boolean equals(Node node)
	{
		if (node == null)
			throw new IllegalArgumentException("Null node");

		return x == node.x && y == node.y
				&& NODE_WIDTH == node.NODE_WIDTH;
	}

	@Override
	public String toString()
	{
		return super.toString() + " pixel : " + NODE_WIDTH + "active : "
				+ active;
	}

	void setActive(boolean active)
	{
		this.active = active;
	}

	public static int center(int i, int pixel)
	{
		return i - (i % pixel) + (pixel / 2);
	}

	public static int nodePixel(int x, int pixel)
	{
		return (center(x, pixel) - (pixel / 2)) / pixel;
	}

	public static int[] coordinates(Node node, int deltaX, int deltaY)
	{
		if (node == null)
			throw new IllegalArgumentException(
					"Null node parameters");

		int[] r = new int[2];
		r[0] = (node.x - deltaX) / node.NODE_WIDTH;
		r[1] = (node.y - deltaY) / node.NODE_WIDTH;
		return r;
	}

}
