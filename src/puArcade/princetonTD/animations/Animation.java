package puArcade.princetonTD.animations;

import android.graphics.Point;

public abstract class Animation extends Point {

	public static final int HEIGHT_LAND = 0;
	public static final int HEIGHT_AIR = 1;

	protected boolean finished;
	protected int height = HEIGHT_AIR;

	// Constructor
	public Animation(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

//	abstract public void draw(Graphics2D g2);

	abstract public void animate(long t);

	public boolean isFinished()
	{
		return finished;
	}
	
	public int getHeight()
	{
		return height;
	}

}
