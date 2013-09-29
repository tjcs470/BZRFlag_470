package main;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ty
 * Date: 9/21/13
 * Time: 7:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class World {
    public PotentialField mGoal;
    public List<PotentialField> mObstacles;

    public World() {
        mGoal = new Goal(new Vector(0,0));
        mObstacles = new ArrayList<PotentialField>();
        /*for(int i = 0; i < 5; i++) {
             mObstacles.add(new Obstacle(new Vector(0,0)));
        }*/
    }
}
