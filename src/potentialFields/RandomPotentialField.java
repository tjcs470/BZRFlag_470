package potentialFields;

import main.Vector;
import math.geom2d.Point2D;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Jordan
 * Date: 10/2/13
 * Time: 10:00 AM
 * To change this template use File | Settings | File Templates.
 */
public class RandomPotentialField extends PotentialField{

    private double maxX;
    private double maxY;
    private Random gen;

    public RandomPotentialField(double maxX, double maxY) {
        this.maxX = maxX;
        this.maxY = maxY;
        gen = new Random();
    }

    @Override
    public Vector getVectorForce(Point2D location) {
        return new Vector(maxX * gen.nextDouble(), maxY * gen.nextDouble());
    }

    @Override
    public boolean isPointOnPotentialField(Point2D location) {
        return true;
    }

    @Override
    protected double getDistanceToPotentialField(Point2D location) {
        return 0;
    }

    @Override
    protected double getAngleToPotentialField(Point2D location) {
        return 0;
    }
}
