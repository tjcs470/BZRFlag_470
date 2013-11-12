package ServerResponse;

import java.awt.geom.Point2D;

/**
 * Created with IntelliJ IDEA.
 * User: ty
 * Date: 11/9/13
 * Time: 5:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class OccGridResponse {
    /** y location of the grid */
    public int x;
    /** y location of the grid */
    public int y;
    /**Actual reading */
    public boolean [][] occupiedObservation;

    /**
     * Constructor
     */
    public OccGridResponse(int x, int y, int rows, int cols) {
        this.x = x;
        this.y = y;
        occupiedObservation = new boolean [rows][cols];
    }
}
