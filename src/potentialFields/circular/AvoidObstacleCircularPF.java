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

    public AvoidObstacleCircularPF(double radius, Point2D center, double sDistance) {
        super(radius, center);
        this.sDistance = sDistance;
    }

    @Override
    protected Vector getVectorForce(Point2D location) {

        double angle = getAngleToPotentialField(location);
        if(isPointOnPotentialField(location)) {
            System.out.println("WARNING: LOCATION IS ON REPULSIVE FIELD");
            return new Vector(-Math.cos(angle) * POSITIVE_INFINITY, -Math.sin(angle) * POSITIVE_INFINITY);
        }

        double distance = getDistanceToPotentialField(location);

        if(distance > sDistance) {
            return emptyVector();  // far away, no influence
        }
        double beta = 1;  //TODO - not sure what this should be
        return new Vector(
                beta *(sDistance - distance) * Math.cos(angle),
                beta * (sDistance-distance) * Math.cos(angle));
    }
}
