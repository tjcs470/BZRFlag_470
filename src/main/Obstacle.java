package main;

/**
 * Created with IntelliJ IDEA.
 * User: ty
 * Date: 9/21/13
 * Time: 7:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class Obstacle extends PotentialField {
    public Obstacle(Vector centerPoint) {
        super(centerPoint);
    }

    @Override
    protected Vector getTangentialForce() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected Vector getRepulsiveForce() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected Vector getAttractiveForce() {
        return new Vector(0,0);
    }
}
