package potentialFields.circular;

import main.Vector;
import math.geom2d.Point2D;
import potentialFields.CircularPotentialField;

import java.util.List;

import static java.lang.Double.POSITIVE_INFINITY;

/**
 * Created with IntelliJ IDEA.
 * User: Jordan
 * Date: 10/2/13
 * Time: 10:05 AM
 */
public class AvoidObstacleCircularPF extends CircularPotentialField {

    private double sDistance;

    public AvoidObstacleCircularPF(double radius, Point2D center, double sDistance, double alpha) {
        super(radius, center, alpha);
        this.sDistance = sDistance;
    }

    @Override
    public Vector getVectorForce(Point2D location) {

        double angle = getAngleToPotentialField(location);
        if(isPointOnPotentialField(location)) {
            System.out.println("WARNING: LOCATION IS ON REPULSIVE FIELD");
//            return new Vector(-Math.cos(angle) * POSITIVE_INFINITY, -Math.sin(angle) * POSITIVE_INFINITY);
            return emptyVector();
        }

        double distance = getDistanceToPotentialField(location);

        if(distance > sDistance) {
            return emptyVector();  // far away, no influence
        }

        return new Vector(
                alpha * (sDistance - distance) * Math.cos(angle),
                alpha * (sDistance - distance) * Math.sin(angle));
    }
}
