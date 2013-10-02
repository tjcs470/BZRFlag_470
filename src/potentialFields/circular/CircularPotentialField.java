package potentialFields.circular;


import math.geom2d.Point2D;
import potentialFields.PotentialField;

/**
 * Created with IntelliJ IDEA.
 * User: larsojor
 * Date: 10/1/13
 * Time: 10:35 PM
 */
public abstract class CircularPotentialField extends PotentialField {

    private double radius;
    private Point2D center;

    public CircularPotentialField(double radius, Point2D center) {
        this.radius = radius;
        this.center = center;
    }

    @Override
    protected double getDistanceToPotentialField(Point2D location) {
        return Point2D.getDistance(center, location);
    }

    @Override
    protected boolean isPointOnPotentialField(Point2D location) {
        return getDistanceToPotentialField(location) < radius;
    }

    @Override
    protected double getAngleToPotentialField(Point2D location) {
        return Math.atan2(center.getY() - location.getY(), center.getX() - location.getX());
    }

}
