package main;

/**
 * Created with IntelliJ IDEA.
 * User: ty
 * Date: 9/21/13
 * Time: 7:55 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class PotentialField {

    public PotentialField(Vector centerPoint)
    {
        mCenterPoint = centerPoint;
    }

    public Vector getNetForce(Vector pos)
    {
        return Vector.add(getTangentialForce(), getAttractiveForce(), getRepulsiveForce());
    }

    protected abstract Vector getTangentialForce();
    protected abstract Vector getRepulsiveForce();
    protected abstract Vector getAttractiveForce();

    public Vector mCenterPoint;
}
