package potentialFields;


import main.Vector;
import math.geom2d.Point2D;

/**
 * Created with IntelliJ IDEA.
 * User: larsojor
 * Date: 10/1/13
 */
public abstract class PotentialField {

    protected abstract Vector getVectorForce(Point2D location);
    protected abstract boolean isPointOnPotentialField(Point2D location);
    protected abstract double getDistanceToPotentialField(Point2D location);
    protected abstract double getAngleToPotentialField(Point2D location);

    public Vector emptyVector() {
        return new Vector(0.0, 0.0);
    }
}

