package potentialFields.circular;

import math.geom2d.Point2D;

/**
 * Created with IntelliJ IDEA.
 * User: Jordan
 * Date: 10/2/13
 * Time: 10:22 AM
 */
public class AvoidObstacleTangentialCircularPF extends AvoidObstacleCircularPF {

    private boolean clockwise;

    public AvoidObstacleTangentialCircularPF(double radius, Point2D center, double sDistance, boolean clockwise) {
        super(radius, center, sDistance);
        this.clockwise = clockwise;
    }

    protected double getAngleToPotentialField(Point2D location) {
        double angle = super.getAngleToPotentialField(location);
        return clockwise ? angle - 90 : angle + 90;
    }
}
