package potentialFields.rectangular;

import main.Vector;
import math.geom2d.Point2D;
import potentialFields.RectangularPotentialField;

import java.util.List;

import static java.lang.Double.POSITIVE_INFINITY;

/**
 * Created with IntelliJ IDEA.
 * User: larsojor
 * Date: 10/1/13
 * To change this template use File | Settings | File Templates.
 */
public class AvoidObstacleRectangularPF extends RectangularPotentialField {

    private double sDistance;

    public AvoidObstacleRectangularPF(double sDistance, double alpha, Point2D... points) {
        super(alpha, points);
        this.sDistance = sDistance;
    }

    public AvoidObstacleRectangularPF(List<Point2D> points, double sDistance, double alpha) {
        super(alpha, points);
        this.sDistance = sDistance;
    }

    @Override
    public Vector getVectorForce(Point2D location) {

        double angle = getAngleToPotentialField(location);
        if(isPointOnPotentialField(location)) {
            System.out.println("WARNING: LOCATION IS ON REPULSIVE FIELD");
            return new Vector(-Math.cos(angle) * POSITIVE_INFINITY, -Math.sin(angle) * POSITIVE_INFINITY);
        }

        double distance = getDistanceToPotentialField(location);

        if(distance > sDistance) {
            return emptyVector();  // far away, no influence
        }
        return new Vector(
                alpha *(sDistance - distance) * Math.cos(angle),
                alpha * (sDistance-distance) * Math.cos(angle));
    }
}
