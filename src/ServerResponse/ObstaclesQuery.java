package ServerResponse;

import main.Vector;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: ty
 * Date: 9/27/13
 * Time: 8:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class ObstaclesQuery {
    /**
     * Primitive obstacle object
     */
    public static class Obstacle {
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

    /**Array of pairs of coordinates that define an obstacle*/
    private ArrayList<ObstaclesQuery.Obstacle> mObstacles;

    /**
     * Constructor
     */
    public ObstaclesQuery() {
        mObstacles = new ArrayList<Obstacle>();
    }

    /**
     * Getter for the obstacles
     */
    public ArrayList<ObstaclesQuery.Obstacle> getObstacles() {
        return mObstacles;
    }

    /**
     * Adds an obstacle
     */
    public void addObstacle(ObstaclesQuery.Obstacle newObstacle) {
        mObstacles.add(newObstacle);
    }
}
