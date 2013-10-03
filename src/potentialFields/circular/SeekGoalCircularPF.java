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
        if(isPointOnPotentialField(location)) return emptyVector();

        double distance = getDistanceToPotentialField(location);
        double angle = getAngleToPotentialField(location);
        if(distance < sDistance) {
            return new Vector(
                    alpha * distance * Math.cos(angle), //do not need to subtract "radius" because distance is to the outside of the polygon (not the center)
                    alpha * distance * Math.sin(angle));
        }

        return new Vector(alpha * Math.cos(angle), alpha * Math.sin(angle));
    }
}
