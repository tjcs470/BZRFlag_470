package Util;

import math.geom2d.Point2D;

/**
 * Created with IntelliJ IDEA.
 * User: ty
 * Date: 10/3/13
 * Time: 11:38 AM
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

    public static boolean shouldBeClockwise(Point2D p) {
        int quadrant = getQuadrant(p);
        return quadrant == 2 || quadrant == 4;

    }

    /**
     * Returns the difference between two angles (ang0 - ang1)
     */
    public static double angDiff(double atan2Ang0, double atan2Ang1){
        double ang0 = (atan2Ang0 < 0 ? Math.PI + (Math.PI + atan2Ang0) : atan2Ang0);
        double ang1 = (atan2Ang1 < 0 ? Math.PI + (Math.PI + atan2Ang1) : atan2Ang1);
        return ang0 - ang1;
    }
}
