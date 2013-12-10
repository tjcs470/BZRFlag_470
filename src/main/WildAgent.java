package main;

import ServerResponse.MyTank;
import ServerResponse.ServerConstants;
import ServerResponse.Tank;
import potentialFields.PotentialField;
import potentialFields.circular.SeekGoalCircularPF;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: ty
 * Date: 12/5/13
 * Time: 8:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class WildAgent {
    /**Server*/
    private BZRFlag mServer;
    /**The potential field*/
    private SeekGoalCircularPF mRandomPf;
    /**The size of the map*/
    private final double mWORLD_SIZE;
    /**The index of the lone wild tank*/
    private final int mWILD_TANK = 0;
    /**My team color*/
    private final Tank.TeamColor mMY_COLOR;
    /**Controls wild tank steering*/
    private PDAngVelController mPdController;
    /**Time delta between ticks*/
    private double mPrevTime;
    /**Elapsed time*/
    private double mElapsedTime;
    /**Random generator*/
    private Random mRand;

    /**
     * Constructor
     */
    public WildAgent(BZRFlag server) throws IOException {
        mServer = server;
        mServer.speed(mWILD_TANK, 1.0);
        ServerConstants serverConsts = mServer.getConstants();
        mWORLD_SIZE = serverConsts.worldSize;
        mMY_COLOR = serverConsts.team;
        mPrevTime =  System.currentTimeMillis();
        mPdController = new PDAngVelController(0.2, 0.8);
        //updateRandPf();
        mElapsedTime = 0.0;
        mRand = new Random();
    }

    /**
     * Some time has passed; decide what to do.
     */
    public void tick() throws IOException {
        ArrayList<ServerResponse.MyTank> myTanks = mServer.getMyTanks(mMY_COLOR);
        ServerResponse.MyTank myTank = myTanks.get(mWILD_TANK);
        //double goalAng = mRandomPf.getAngleToPotentialField(myTank.getPos());

        double newTime = System.currentTimeMillis();
        double timeDiffInSec = (newTime - mPrevTime) / 1000;
        mPrevTime = newTime;
        //double angAccel = mPdController.getAcceleration(goalAng, myTank.getAngle(), timeDiffInSec);

        if(mElapsedTime > 5) {
            //updateRandPf();
            mServer.angVel(mWILD_TANK, 2.0 * mRand.nextDouble() - 1.0);
            mServer.speed(mWILD_TANK, 2.0 * mRand.nextDouble() - 1.0);
            mElapsedTime = 0.0;
        }
        else {
            mElapsedTime += timeDiffInSec;
        }

        //double targetVel = myTank.getAngVel() + angAccel;
        //mServer.angVel(mWILD_TANK, targetVel);
    }

    /**
     * Gets a random location on the map
     */
    /*private void updateRandPf() {
        double randX = (Math.random() * mWORLD_SIZE) - (mWORLD_SIZE / 2.0);
        double randY = (Math.random() * mWORLD_SIZE) - (mWORLD_SIZE / 2.0);
        mRandomPf = new SeekGoalCircularPF(1, new Vector(randX, randY), 30, 1);
    }*/
}
