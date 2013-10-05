package ServerResponse;

import main.Vector;
import math.geom2d.Point2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ty
 * Date: 9/28/13
 * Time: 9:51 PM
 */
public class Obstacle {
    /**Corners of obstacle*/
    private Vector mP0;
    private Vector mP1;
    private Vector mP2;
    private Vector mP3;

    public Obstacle(Vector p0, Vector p1, Vector p2, Vector p3) {
        mP0 = p0;
        mP1 = p1;
        mP2 = p2;
        mP3 = p3;
    }

    public Vector getP0() {
        return mP0;
    }

    public Vector getP1() {
        return mP1;
    }

    public Vector getP2() {
        return mP2;
    }

    public Vector getP3() {
        return mP3;
    }

    /**
     * Getter for the corners of the obstacle in counter-clockwise order
     */
    public List<Point2D> getPoints() {
        Point2D[] points = {mP3, mP2, mP1, mP0};
        return Arrays.asList(points);
    }
}
