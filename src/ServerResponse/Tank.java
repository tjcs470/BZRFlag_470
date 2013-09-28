package ServerResponse;

import main.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: ty
 * Date: 9/28/13
 * Time: 8:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class Tank {
    public enum TeamColor {
        RED,
        GREEN,
        BLUE,
        PURPLE,
        NONE
    }

    public enum TankStatus {
        ALIVE,
        DEAD
    }

    /**Tank callsign*/
    private String mCallsign;
    /**Color*/
    private TeamColor mColor;
    /**Status*/
    private TankStatus mStatus;
    /**Color of the flag that the tank is holding*/
    private TeamColor mFlagColor;
    /**Position of the tank*/
    private Vector mPos;
    /**Angle of the tank*/
    private double mAngle;

    public Tank(String callSign, TeamColor color, TankStatus status, TeamColor flagColor, Vector pos, double angle) {
        mCallsign = callSign;
        mColor = color;
        mStatus = status;
        mFlagColor = flagColor;
        mPos = pos;
        mAngle = angle;
    }

    public String getCallsign() {
        return mCallsign;
    }

    public TeamColor getColor() {
        return mColor;
    }

    public TankStatus getStatus() {
        return mStatus;
    }

    public TeamColor getFlagColor() {
        return mFlagColor;
    }

    public Vector getPos() {
        return mPos;
    }

    public double getAngle() {
        return mAngle;
    }
}
