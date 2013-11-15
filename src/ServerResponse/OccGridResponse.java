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
    /** x location of the grid */
    public int x;
    /** y location of the grid */
    public int y;
    /** rows in the grid */
    public int rows;
    /** cols in the grid */
    public int cols;
    /**Actual reading */
    public boolean [][] occupiedObservation;

    /**
     * Constructor
     */
    public OccGridResponse(int x, int y, int rows, int cols) {
        this.x = x;
        this.y = y;
        this.rows = rows;
        this.cols = cols;
        occupiedObservation = new boolean [rows][cols];
    }
}
