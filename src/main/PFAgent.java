package main;

import ServerResponse.Tank;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: ty
 * Date: 10/2/13
 * Time: 6:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class PFAgent {

    /**Our portal to the server*/
    private BZRFlag mServer;
    /**Previous time stamp in seconds*/
    private double mPrevTime;
    /**My team color*/
    private Tank.TeamColor mTeamColor;

    /**
     * Constructor
     */
    public PFAgent(BZRFlag teamConnection, Tank.TeamColor teamColor) throws IOException {
        //mServer = new BZRFlag("localhost", 38508);
        mServer = teamConnection;
        mServer.handshake();

        mTeamColor = teamColor;

        mPrevTime = System.currentTimeMillis();
    }
    /**
     * Some time has passed; decide what to do.
     */
    public void tick() throws IOException {
        /*ArrayList<MyTank> myTanks = mServer.getMyTanks(Tank.TeamColor.BLUE);
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
            mServer.shoot(1);*/

    }
}
