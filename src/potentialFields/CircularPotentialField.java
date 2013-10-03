package potentialFields;


import math.geom2d.Point2D;
import potentialFields.PotentialField;

/**
 * Created with IntelliJ IDEA.
 * User: larsojor
 * Date: 10/1/13
 * Time: 10:35 PM
 */
public abstract class CircularPotentialField extends PotentialField {

    protected double radius;
    private Point2D center;

    public CircularPotentialField(double radius, Point2D center, double alpha) {
        super(alpha);
        this.radius = radius;
        this.center = center;
    }

    @Override
    public double getDistanceToCenterOfPotentialField(Point2D location) {
        return Point2D.distance(center, location);
    }

    @Override
    public boolean isPointOnPotentialField(Point2D location) {
        return getDistanceToCenterOfPotentialField(location) <= radius;
    }

    @Override
    public double getAngleToPotentialField(Point2D location) {
        return Math.atan2(center.y() - location.y(), center.x() - location.x());
    }

    @Override
    public double getDistanceToOutsideOfPotentialField(Point2D location) {
        return getDistanceToCenterOfPotentialField(location) - radius;
    }
}
