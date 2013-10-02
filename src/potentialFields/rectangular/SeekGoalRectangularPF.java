package potentialFields.rectangular;

import main.Vector;
import math.geom2d.Point2D;
import potentialFields.RectangularPotentialField;

/**
 * Created with IntelliJ IDEA.
 * User: larsojor
 * Date: 10/1/13
 * Time: 10:44 PM
 */
public class SeekGoalRectangularPF extends RectangularPotentialField {

    private double sDistance;

    public SeekGoalRectangularPF(double sDistance, Point2D... points) {
        super(points);
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
                    (distance - sDistance) * Math.cos(angle),
                    (distance - sDistance) * Math.sin(angle));
        }

        return new Vector(Math.cos(angle), Math.sin(angle));

    }

}
