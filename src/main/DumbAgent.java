package main;

import ServerResponse.MyTank;
import ServerResponse.Tank;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: ty
 * Date: 9/27/13
 * Time: 8:37 PM
 */
public class DumbAgent {
    /**
     * Dumb tank
     */
    private static class DumbTank {
        private enum TankState {
            MOVING_FOWARD,
            TURNING
        }

        /**State of the tank*/
        private TankState mState;
        /**The time that has passed in the current state*/
        private double mTimeInState;
        /**Angle at beginning of turning state*/
        private double mStartAngle;
        /**Angular velocity*/
        private double mAngVel;
        /**Forward velocity*/
        private double mSpeed;
        /**60 degrees constant*/
        private final double SIXTY_DEG = Math.PI / 3.0;

        /**
         * Constructor
         */
        public DumbTank() {
            mState = TankState.MOVING_FOWARD;
            mSpeed = 1.0;
            mTimeInState = 0.0;
        }

        /**
         * Tank decides what to do after a period of time has passed
         * @param timeDiff
         */
        public void update(double timeDiff, double angle) {
            mTimeInState += timeDiff;

            switch (mState) {
                case MOVING_FOWARD:
                    if(mTimeInState >= 8) {
                        mState = TankState.TURNING;
                        mSpeed = 0.0;
                        mAngVel = 1.0;
                        mStartAngle = angle;
                        mTimeInState = 0.0;
                    }
                    break;

                case TURNING:
                    if(Math.abs(mStartAngle - angle) >= SIXTY_DEG) {
                        mState = TankState.MOVING_FOWARD;
                        mAngVel = 0.0;
                        mSpeed = 1.0;
                        mTimeInState = 0.0;
                    }
                    break;

                default:
                   assert(false);
            }
        }

        /**
         * Getter for the angular velocity
         */
        public double getAngVel() {
            return mAngVel;
        }

        /**
         * Getter for the velocity
         */
        public double getVel() {
            return mSpeed;
        }

        /**
         * Should shoot
         */
        public boolean shouldShoot() {
            if(mTimeInState < 1.5)
                return false;
            else if(mTimeInState > 2.5)
                return true;

            Random randNum = new Random(System.currentTimeMillis());
            return randNum.nextBoolean();
        }
    }

    /**Our portal to the server*/
    private BZRFlag mServer;
    /**Previous time stamp in seconds*/
    private double mPrevTime;
    /**A dumb guard tank*/
    private DumbTank dumbTank0;
    private DumbTank dumbTank1;
    /**My team color*/
    private Tank.TeamColor mTeamColor;

    /**
     * Constructor
     */
    public DumbAgent(BZRFlag teamConnection, Tank.TeamColor teamColor) throws IOException {
        //mServer = new BZRFlag("localhost", 38508);
        mServer = teamConnection;
        mServer.handshake();

        mTeamColor = teamColor;

        mPrevTime = System.currentTimeMillis();
        dumbTank0 = new DumbTank();
        dumbTank1 = new DumbTank();
    }

    /**
     * Some time has passed; decide what to do.
     */
    public void tick() throws IOException {
        ArrayList<MyTank> myTanks = mServer.getMyTanks(Tank.TeamColor.BLUE);
        double newTime = System.currentTimeMillis();
        double timeDiffInSec = (newTime - mPrevTime) / 1000;
        mPrevTime = newTime;

        dumbTank0.update(timeDiffInSec, myTanks.get(0).getAngle());

        mServer.speed(0, dumbTank0.getVel());
        mServer.angVel(0, dumbTank0.getAngVel());
        if(dumbTank0.shouldShoot())
            mServer.shoot(0);

        dumbTank1.update(timeDiffInSec, myTanks.get(1).getAngle());

        mServer.speed(1, dumbTank1.getVel());
        mServer.angVel(1, dumbTank1.getAngVel());
        if(dumbTank1.shouldShoot())
            mServer.shoot(1);

    }
}
