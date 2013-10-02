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
    public SeekGoalCircularPF(double radius, Point2D center, double sDistance) {
        super(radius, center);
        this.sDistance = sDistance;
    }

    @Override
    protected Vector getVectorForce(Point2D location) {
        if(isPointOnPotentialField(location)) return emptyVector();

        double distance = getDistanceToPotentialField(location);
        double angle = getAngleToPotentialField(location);
        if(distance < sDistance) {
            return new Vector(
                    (distance - sDistance) * Math.cos(angle),
                    (distance - sDistance) * Math.sin(angle));
        }

        return new Vector(Math.cos(angle), Math.sin(angle));
    }
}
