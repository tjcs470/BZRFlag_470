package ServerResponse;

import main.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: ty
 * Date: 9/28/13
 * Time: 9:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class Obstacle {
    /**Corners of obstacle*/
    private Vector mP0;
    private Vector mP1;

    public Obstacle(Vector p0, Vector p1) {
        mP0 = p0;
        mP1 = p1;
    }

    public Vector getP0() {
        return mP0;
    }

    public Vector getP1() {
        return mP1;
    }
}
