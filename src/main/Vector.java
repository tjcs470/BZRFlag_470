package main;

/**
 * Created with IntelliJ IDEA.
 * User: ty
 * Date: 9/21/13
 * Time: 7:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class Vector {
    public Vector()
    {
        this.mX = 0.0;
        this.mY = 0.0;
    }

    public Vector(double mX, double mY) {
        this.mX = mX;
        this.mY = mY;
    }

    public static Vector add(Vector... vectors) {
        double xTotal = 0;
        double yTotal = 0;
        for(Vector v : vectors) {
            xTotal += v.mX;
            yTotal += v.mY;
        }
        return new Vector(xTotal, yTotal);
    }

    /**
     * Returns the magnitude of the vector
     * @return
     */
    public double getMag() {
        return Math.sqrt((mX * mX) + (mY * mY));
    }

    public double mX;
    public double mY;
}
