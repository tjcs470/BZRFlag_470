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
        return new Vector(alpha * gen.nextDouble(), alpha * gen.nextDouble());
    }

    @Override
    public boolean isPointOnPotentialField(Point2D location) {
        return true;
    }

    @Override
    public double getDistanceToPotentialField(Point2D location) {
        return 0;
    }

    @Override
    public double getAngleToPotentialField(Point2D location) {
        return gen.nextDouble() * 2 * Math.PI;
    }
}
