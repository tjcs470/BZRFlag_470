package potentialFields.rectangular;

import main.Vector;
import math.geom2d.Point2D;
import potentialFields.RectangularPotentialField;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Jordan
 * Date: 10/2/13
 * Time: 10:22 AM
 */
public class AvoidObstacleTangentialRectangularPF extends AvoidObstacleRectangularPF {

    private boolean clockwise;

    public AvoidObstacleTangentialRectangularPF(double sDistance, boolean clockwise, Point2D... points) {
        super(sDistance, points);
        this.clockwise = clockwise;
    }

    public AvoidObstacleTangentialRectangularPF(List<Point2D> points, double sDistance, boolean clockwise) {
        super(points, sDistance);
        this.clockwise = clockwise;
    }

    public double getAngleToPotentialField(Point2D location) {
        double angle = super.getAngleToPotentialField(location);
        return clockwise ? angle - Math.PI/2.0 : angle + Math.PI/2.0;
    }
}
