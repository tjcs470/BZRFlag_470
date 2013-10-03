package main;

import ServerResponse.Flag;
import ServerResponse.GnuplotPrinter;
import ServerResponse.Obstacle;
import ServerResponse.Tank;
import potentialFields.PotentialField;
import potentialFields.circular.AvoidObstacleTangentialCircularPF;
import potentialFields.circular.SeekGoalCircularPF;
import potentialFields.rectangular.AvoidObstacleRectangularPF;
import potentialFields.rectangular.AvoidObstacleTangentialRectangularPF;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

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
    /**Potential fields in the world*/
    private ArrayList<PotentialField> mPotentialFields;
    /**PF for flag*/
    private SeekGoalCircularPF mFlagPf;

    /**
     * Constructor
     */
    public PFAgent(BZRFlag teamConnection, Tank.TeamColor teamColor) throws IOException {
        //mServer = new BZRFlag("localhost", 38508);
        mServer = teamConnection;
        mServer.handshake();

        mTeamColor = teamColor;

        mPrevTime = System.currentTimeMillis();

        mPotentialFields = new ArrayList<PotentialField>();
        ArrayList<Obstacle> obstacles = mServer.getObstacles();
        for(Obstacle obstacle : obstacles) {
            AvoidObstacleRectangularPF rectPF = new AvoidObstacleRectangularPF(obstacle.getPoints(), 250.0, .15);
            AvoidObstacleTangentialRectangularPF tangRectPf = new AvoidObstacleTangentialRectangularPF(obstacle.getPoints(), 350, true, .1);
            mPotentialFields.add(rectPF);
            mPotentialFields.add(tangRectPf);
        }

        ArrayList<Flag> flags = mServer.getFlags();
        for(Flag flag : flags) {
            if(flag.getTeamColor() != teamColor) {
                mFlagPf = new SeekGoalCircularPF(.1, flag.getPos(), 100, .1);
                break;
            }
        }
    }

    public void plotPfs() throws IOException {
        ArrayList<Obstacle> obstacles = mServer.getObstacles();

        PrintWriter gpiFile = new PrintWriter("world.gpi", "UTF-8");
        gpiFile.println("set xrange [-400.0: 400.0]");
        gpiFile.println("set yrange [-400.0: 400.0]");
        gpiFile.println("unset arrow");

        for(Obstacle obstacle : obstacles) {
            gpiFile.println(GnuplotPrinter.getObstaclePlotCmds(obstacle));
        }

        gpiFile.println("plot '-' with vectors head");

        mPotentialFields.add(mFlagPf);
        for(double x = -400; x <= 400; x += 50.0) {
            for(double y = -400; y <= 400; y += 50.0) {
                Vector pos = new Vector(x, y);
                Vector force = PotentialField.getNetVector(pos, mPotentialFields);
                gpiFile.println(String.format("%s %s %s %s", x, y, force.x(), force.y()));
            }
        }
        mPotentialFields.remove(mFlagPf);

        gpiFile.println("e");
        gpiFile.close();
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
