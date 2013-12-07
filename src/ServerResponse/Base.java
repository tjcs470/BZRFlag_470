package ServerResponse;

import main.Vector;
import math.geom2d.Point2D;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: ty
 * Date: 10/5/13
 * Time: 11:23 AM
 * To change this template use File | Settings | File Templates.
 */
public class Base {
    /**The team color of the base*/
    private Tank.TeamColor mColor;
    /**The corners of the base in counter-clockwise order*/
    private ArrayList<Point2D> corners;

    /**
     * Constructor
     */
    public Base(Point2D p0, Point2D p1, Point2D p2, Point2D p3) {
        this.corners = new ArrayList<Point2D>(Arrays.asList(p3, p2, p1, p0));
    }

    /**
     * Gets the list of corners in counter-clockwise order
     */
    public ArrayList<Point2D> getCorners() {
        return corners;
    }

    /**
     * Get center
     */
    public Point2D getCenter() {
        return Point2D.centroid(corners);
    }
}
