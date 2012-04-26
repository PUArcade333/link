package puArcade.princetonTD.grid;

import org.jgrapht.graph.DefaultWeightedEdge;

public class Arc extends DefaultWeightedEdge {
	
	private static final long serialVersionUID = -3342545915708624018L;

	private Node start, end;

	public Arc(Node start, Node end)
	{
		this.start = start;
		this.end = end;
	}

//	public Line2D toLine2D()
//	{
//		return new Line2D()
//		{
//			public Point2D getP1()
//			{
//				return getStart();
//			}
//
//			public Point2D getP2()
//			{
//				return getEnd();
//			}
//
//			public double getX1()
//			{
//				return getStart().x;
//			}
//
//			public double getX2()
//			{
//				return getEnd().x;
//			}
//
//			public double getY1()
//			{
//				return getStart().y;
//			}
//
//			public double getY2()
//			{
//				return getEnd().y;
//			}

//			public void setLine(double x1, double y1, double x2, double y2)
//			{
//				throw new IllegalArgumentException("Unimplemented");
//
//			}

//			public Rectangle2D getBounds2D()
//			{
//				throw new IllegalArgumentException("Unimplemented");
//			}
//		};
//	}

	public Node getStart()
	{
		return start;
	}

	public Node getEnd()
	{
		return end;
	}

}
