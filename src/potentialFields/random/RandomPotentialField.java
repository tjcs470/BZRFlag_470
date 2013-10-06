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

        double x1 = alpha * (gen.nextDouble() - .5);
        double x2 = alpha * (gen.nextDouble() - .5);
        double x3 = alpha * (gen.nextDouble() - .5);

        double y1 = alpha * (gen.nextDouble() - .5);
        double y2 = alpha * (gen.nextDouble() - .5);
        double y3 = alpha * (gen.nextDouble() - .5);

        //returns the min which allows for large alpha
        return new Vector(min(x1, min(x2, x3)), min(y1, min(y2,y3)));
    }

    private double min(double d1, double d2) {
        return d1 < d2 ? d1 : d2;
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
