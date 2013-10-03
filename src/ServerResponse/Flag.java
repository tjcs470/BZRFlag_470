package ServerResponse;


import math.geom2d.Point2D;

/**
 * Created with IntelliJ IDEA.
 * User: ty
 * Date: 10/2/13
 * Time: 7:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class Flag {
    private Tank.TeamColor mTeamColor;
    private Tank.TeamColor mPossesingTeamColor;
    private Point2D mPos;

    public Flag(Tank.TeamColor mTeamColor, Tank.TeamColor mPossesingTeamColor, Point2D mPos) {
        this.mTeamColor = mTeamColor;
        this.mPossesingTeamColor = mPossesingTeamColor;
        this.mPos = mPos;
    }

    public Tank.TeamColor getTeamColor() {
        return mTeamColor;
    }

    public Tank.TeamColor getPossesingTeamColor() {
        return mPossesingTeamColor;
    }

    public Point2D getPos() {
        return mPos;
    }
}
