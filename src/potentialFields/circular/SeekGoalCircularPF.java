package potentialFields.circular;

import main.Vector;
import math.geom2d.Point2D;
import potentialFields.CircularPotentialField;

/**
 * Created with IntelliJ IDEA.
 * User: Jordan
 * Date: 10/2/13
 * Time: 10:05 AM
 */
public class SeekGoalCircularPF extends CircularPotentialField {

    private double sDistance;
    public SeekGoalCircularPF(double radius, Point2D center, double sDistance, double alpha) {
        super(radius, center, alpha);
        this.sDistance = sDistance;
    }

    @Override
    public Vector getVectorForce(Point2D location) {
        if(isPointOnPotentialField(location)) {
            return emptyVector();
        }

        double distanceToCenter = getDistanceToCenterOfPotentialField(location);
        double distanceToOutside = getDistanceToOutsideOfPotentialField(location);
        double angle = getAngleToPotentialField(location);
        if(distanceToOutside < sDistance) {
            return new Vector(
                    sign * alpha * distanceToOutside * Math.cos(angle), //do not need to subtract "radius" because distance is to the outside of the polygon (not the center)
                    sign * alpha * distanceToOutside * Math.sin(angle));
        }

        return new Vector(sign * alpha * sDistance * Math.cos(angle), sign * alpha * sDistance * Math.sin(angle));

    }
}
