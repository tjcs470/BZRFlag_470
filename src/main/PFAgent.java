package main;

import ServerResponse.*;
import Util.GeometryUtils;
import math.geom2d.Point2D;
import potentialFields.PotentialField;
import potentialFields.circular.AvoidObstacleTangentialCircularPF;
import potentialFields.circular.SeekGoalCircularPF;
import potentialFields.rectangular.AvoidObstacleRectangularPF;
import potentialFields.rectangular.AvoidObstacleTangentialRectangularPF;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ty
 * Date: 10/2/13
 * Time: 6:32 PM
 */
public class PFAgent {

    /**Our portal to the server*/
    private BZRFlag mServer;
    /**Previous time stamp in seconds*/
    private double mPrevTime;
    /**My team color*/
    private Tank.TeamColor mTeamColor;
    /**Oponent color*/
    private Tank.TeamColor mOpponentColor;
    /**Potential fields in the world*/
    private List<PotentialField> mPotentialFields;
    /**PF for flag*/
    // should only have one potential field at any time, made into list for paramater passing in Potential Field class
    private List<PotentialField> mFlagPf = new ArrayList<PotentialField>(1);
    private List<PotentialField> tankPfs = new ArrayList<PotentialField>(30);
    /**PD Controller for angles*/
    //PDAngVelController mPdAngVelController;
    private List<PDAngVelController> mTankPdControllers;
    private List<Double> mTimeDiffs = new ArrayList<Double>(10);


    /**
     * Constructor
     */
    public PFAgent(BZRFlag teamConnection, Tank.TeamColor myTeamColor) throws IOException {
        //mServer = new BZRFlag("localhost", 38508);
        mServer = teamConnection;
        mServer.handshake();
        mTeamColor = myTeamColor;
        mPrevTime = System.currentTimeMillis();
        //mPdAngVelController = new PDAngVelController(50, 50);
        mOpponentColor = Tank.TeamColor.PURPLE;

        mTankPdControllers = new ArrayList<PDAngVelController>();
        for(int i = 0; i < 10; i++) {
            PDAngVelController pdController = new PDAngVelController(0.2, 0.8);
            mTankPdControllers.add(pdController);
            mTimeDiffs.add(new Double(System.currentTimeMillis()));
        }
    }

    /**
     * Builds up the potential fields
     */
    private void buildObstaclePotentialFields() throws IOException {
        mPotentialFields = new ArrayList<PotentialField>();
        ArrayList<Obstacle> obstacles = mServer.getObstacles();
        for(Obstacle obstacle : obstacles) {
            AvoidObstacleRectangularPF rectPF = new AvoidObstacleRectangularPF(obstacle.getPoints(), 100.0, .45);
            mPotentialFields.add(rectPF);

            // calculate the relationship of obstacle center to the goal
            Point2D obsCentroid = Point2D.centroid(obstacle.getPoints());
            Point2D goalToCentroid = new Point2D(new Point2D(obsCentroid.x() - mFlagPf.get(0).getCenter().x(),
                    obsCentroid.y() - mFlagPf.get(0).getCenter().y()));
            int quadrant = Util.GeometryUtils.getQuadrant(goalToCentroid);
            boolean clockwise = true;
            if(quadrant == 1 || quadrant == 3)
                clockwise = false;

            AvoidObstacleTangentialRectangularPF tangRectPf = new AvoidObstacleTangentialRectangularPF(obstacle.getPoints(), 200, clockwise, .02);
            mPotentialFields.add(tangRectPf);
        }
    }

    private void buildTankPotentialFields(int currTank) throws IOException {
        tankPfs.clear();
        ArrayList<MyTank> myTanks = mServer.getMyTanks(mTeamColor);
        ArrayList<Tank> otherTanks = mServer.getOtherTanks();

        for(MyTank myTank : myTanks) {
            if(myTank.getIndex() == currTank) {
                continue;
            }

            Point2D pos = myTank.getPos();
            tankPfs.add(new AvoidObstacleTangentialCircularPF(4.32, pos, 3.0, GeometryUtils.shouldBeClockwise(pos), .5));
        }

        for(Tank otherTank : otherTanks) {
            Point2D pos = otherTank.getPos();
            tankPfs.add(new AvoidObstacleTangentialCircularPF(4.32, pos, 3.0, GeometryUtils.shouldBeClockwise(pos), .5));
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

        for(double x = -400; x <= 400; x += 50.0) {
            for(double y = -400; y <= 400; y += 50.0) {
                Vector pos = new Vector(x, y);
                Vector force = PotentialField.getNetVector(pos, mPotentialFields, mFlagPf, tankPfs);
                gpiFile.println(String.format("%s %s %s %s", x, y, force.x(), force.y()));
            }
        }

        gpiFile.println("e");
        gpiFile.close();
    }

    /**
     * Builds the goal potential field
     */
    public void buildGoalPotentialField(Tank.TeamColor goalColor) throws IOException {
        if(mFlagPf.isEmpty())
            mFlagPf.add(null);

        if(goalColor == mTeamColor) {
            Map<Tank.TeamColor, Base> bases = mServer.getBases();
            Point2D myBaseCentroid = Point2D.centroid(bases.get(mTeamColor).getCorners());
            mFlagPf.set(0, new SeekGoalCircularPF(.1, myBaseCentroid, 100, .3));
        }
        else {
            ArrayList<Flag> flags = mServer.getFlags();
            for(Flag flag : flags) {
                if(flag.getTeamColor() == goalColor) {
                    mFlagPf.set(0, new SeekGoalCircularPF(.1, flag.getPos(), 100, .3));
                    break;
                }
            }
        }
    }

    /**
     * Some time has passed; decide what to do.
     */
    public void tick() throws IOException, InterruptedException {
        //must be done each time because tanks may have moved

        //int pfTankIndex = 0;
        for(int pfTankIndex = 0; pfTankIndex < 10; pfTankIndex++) {
            ArrayList<MyTank> myTanks = mServer.getMyTanks(Tank.TeamColor.BLUE);

            double newTime = System.currentTimeMillis();
            double timeDiffInSec = (newTime - mTimeDiffs.get(pfTankIndex)) / 1000;
            mTimeDiffs.set(pfTankIndex, newTime);

            //buildTankPotentialFields(pfTankIndex);
            MyTank pfTank0 = myTanks.get(pfTankIndex);

            boolean capturedFlag = pfTank0.getFlagColor() != Tank.TeamColor.NONE;
            if(capturedFlag)
                buildGoalPotentialField(mTeamColor);
            else
                buildGoalPotentialField(mOpponentColor);
            buildObstaclePotentialFields();

            // get the goal angle
            double currAng = pfTank0.getAngle();
            double currAngVel = pfTank0.getAngVel();
            double goalAngle = PotentialField.getNetAngle(pfTank0.getPos(), mPotentialFields, mFlagPf, tankPfs);
    //        double goalAngle = PotentialField.getNetAngle(pfTank0.getPos(), tankPfs);

            //mPdAngVelController.getAcceleration(goalAngle, currAng, timeDiffInSec);
            double angAcceleration = mTankPdControllers.get(pfTankIndex).getAcceleration(goalAngle, currAng, timeDiffInSec);
            double targetVel = currAngVel + angAcceleration;

            if(pfTankIndex == 0)
                System.out.println(angAcceleration);
            mServer.angVel(pfTankIndex, targetVel);
            mServer.speed(pfTankIndex, 1.0);
            mServer.shoot(pfTankIndex);
        }
    }
}
