package main;

/**
 * Created with IntelliJ IDEA.
 * User: ty
 * Date: 9/21/13
 * Time: 8:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class Goal extends PotentialField {
    protected Goal(Vector centerPoint) {
        super(centerPoint);
    }

    @Override
    protected Vector getTangentialForce() {
       return new Vector(0,0);
    }

    @Override
    protected Vector getRepulsiveForce() {
        return new Vector(0,0);
    }

    @Override
    protected Vector getAttractiveForce() {
        return null;
    }
}
