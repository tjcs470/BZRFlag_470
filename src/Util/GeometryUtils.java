package Util;

import math.geom2d.Point2D;

/**
 * Created with IntelliJ IDEA.
 * User: ty
 * Date: 10/3/13
 * Time: 11:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class GeometryUtils {
    /**
     * Returns which quadrant a point is in
     */
    public static int getQuadrant(Point2D p) {
        int quadrantNum = 0;
        if(p.x() > 0) {
            quadrantNum = p.y() > 0 ? 1 : 4;
        }
        else {
            quadrantNum = p.y() > 0 ? 2 : 3;
        }

        return quadrantNum;
    }
}
