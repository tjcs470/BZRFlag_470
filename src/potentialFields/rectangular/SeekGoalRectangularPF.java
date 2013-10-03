package potentialFields.rectangular;

import main.Vector;
import math.geom2d.Point2D;
import potentialFields.RectangularPotentialField;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: larsojor
 * Date: 10/1/13
 * Time: 10:44 PM
 */
public class SeekGoalRectangularPF extends RectangularPotentialField {

    private double sDistance;

    public SeekGoalRectangularPF(double sDistance, double alpha, List<Point2D> points) {
        super(alpha, points);
        this.sDistance = sDistance;
    }

    @Override
    public Vector getVectorForce(Point2D location) {
        if(isPointOnPotentialField(location)) {
            return emptyVector();
        }

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
