package potentialFields.random;

import main.Vector;
import math.geom2d.Point2D;
import potentialFields.PotentialField;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Jordan
 * Date: 10/2/13
 * Time: 10:00 AM
 */
public class RandomPotentialField extends PotentialField {

    private Random gen;

    public RandomPotentialField(double alpha) {
        super(alpha);
        gen = new Random();
    }

    @Override
    public Vector getVectorForce(Point2D location) {
        return new Vector(alpha * (gen.nextDouble() - .5), alpha * (gen.nextDouble() - .5));
    }

    @Override
    public boolean isPointOnPotentialField(Point2D location) {
        return true;
    }

    @Override
    public double getDistanceToOutsideOfPotentialField(Point2D location) {
        return 0;
    }

    @Override
    public double getDistanceToCenterOfPotentialField(Point2D location) {
        return 0;
    }

    @Override
    public double getAngleToPotentialField(Point2D location) {
        return gen.nextDouble() * 2 * Math.PI;
    }

    @Override
    public Point2D getCenter() {
        return emptyVector();
    }
}
