package puArcade.princetonTD.grid;

import java.util.ArrayList;

import puArcade.princetonTD.exceptions.PathNotFoundException;


import android.graphics.Point;
import android.graphics.Rect;

public interface Grid {
	
	// calculate shortest path
	public abstract ArrayList<Point> shortestPath(int xStart, int yStart,
            int xEnd, int yEnd) throws PathNotFoundException,
            IllegalArgumentException;

    // activate zone
    public abstract void activateZone(Rect rect, boolean update)
            throws IllegalArgumentException;

    // deactivate zone
    public abstract void deactivateZone(Rect rect, boolean update)
            throws IllegalArgumentException;

    // width in pixels
    public abstract int getWidth();

    // height in pixels
    public abstract int getHeight();

    // grid nodes
    public abstract Node[] getNodes();

    // grid edges
//    public abstract Line2D[] getArcs();

    // calculate path length
    public abstract double getPathLength(ArrayList<Point> path);

    // return number of nodes
    public abstract int getNodesN();
    
    // Exit point
    public void addExit(int x, int y);
    
    // update
    public void update();
	
}
