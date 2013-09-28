package main;

import sun.security.krb5.internal.Ticket;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: ty
 * Date: 9/27/13
 * Time: 8:37 PM
 * To change this template use File | Settings | File Templates.
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
        private double mVel;

        /**
         * Constructor
         */
        public DumbTank() {
            mState = TankState.MOVING_FOWARD;
            mTimeInState = 0.0;
        }

        /**
         * Tank decides what to do after a period of time has passed
         * @param timeDiff
         */
        public void update(double timeDiff, double angle, double speed) {
            mTimeInState += timeDiff;

            switch (mState) {
                case MOVING_FOWARD:
                    if(mTimeInState >= 8) {
                        mState = TankState.TURNING;
                        mStartAngle = angle;
                    }
                    break;

                case TURNING:
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
            return mVel;
        }
    }

    /**Our portal to the server*/
    private BZRFlag mServer;

    /**
     * Constructor
     */
    public DumbAgent() throws IOException {
        BZRFlag mServer = new BZRFlag("localhost", 54117);
    }

    /**
     * Some time has passed; decide what to do.
     */
    public void tick(double timeDiff) {
        // update the agents
        // get their new state and tell the server
    }
}
