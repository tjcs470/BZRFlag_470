package ServerResponse;

import main.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: ty
 * Date: 9/28/13
 * Time: 9:58 AM
 */
public class MyTank extends Tank {
    /**Tank index*/
    private int mIndex;
    /**Shots available to be fired*/
    private int mShotsLeft;
    /**Time to reload the tank*/
    private double mTimeToReload;
    /**Velocity of the tank*/
    private Vector mVel;
    /**Angular velocity of the tank*/
    private double mAngVel;

    public MyTank(int index,
                  String callSign,
                  TeamColor color,
                  TankStatus status,
                  int mShotsLeft,
                  double timeToReload,
                  TeamColor flagColor,
                  Vector pos,
                  double angle,
                  Vector vel,
                  double angVel) {
        super(callSign, color, status, flagColor, pos, angle);
        this.mIndex = index;
        this.mShotsLeft = mShotsLeft;
        this.mTimeToReload = timeToReload;
        this.mVel = vel;
        this.mAngVel = angVel;
    }

    public int getIndex() {
        return mIndex;
    }

    public int getShotsLeft() {
        return mShotsLeft;
    }

    public double getTimeToReload() {
        return mTimeToReload;
    }

    public Vector getVel() {
        return mVel;
    }

    public double getAngVel() {
        return mAngVel;
    }
}
