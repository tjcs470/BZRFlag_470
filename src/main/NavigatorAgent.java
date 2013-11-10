package main;

import ServerResponse.MyTank;
import ServerResponse.NavigatorTank;
import ServerResponse.Tank;
import math.geom2d.Point2D;
import potentialFields.PotentialField;
import potentialFields.circular.SeekGoalCircularPF;
import potentialFields.rectangular.SeekGoalRectangularPF;

import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: larsojor
 * Date: 11/9/13
 * Time: 3:35 PM
 */
public class NavigatorAgent {

    private BZRFlag mServer;
    private Tank.TeamColor mTeamColor;
    private long mPrevTime;
    private List<PDAngVelController> mTankPdControllers;
    private List<Double> mTimeDiffs = new ArrayList<Double>(10);
    private Map<Integer, Integer> tankGoalMap = new HashMap<Integer, Integer>(); //stores current goal of tank
    private Map<Integer, Point2D> tankPosMap = new HashMap<Integer, Point2D>(); //stores previous position of tank for stuck detection
    private Map<Integer, Integer> tankAlignmentCounter = new HashMap<Integer, Integer>(); //gives tank time to align
    Random gen = new Random();

    private List<NavigatorTank> army;

    public NavigatorAgent(BZRFlag teamConnection, Tank.TeamColor myTeamColor) throws IOException {
        mServer = teamConnection;
        mServer.handshake();
        mTeamColor = myTeamColor;
        mPrevTime = System.currentTimeMillis();
        mTankPdControllers = new ArrayList<PDAngVelController>();
        for(int i = 0; i < 10; i++) {
            PDAngVelController pdController = new PDAngVelController(0.2, 0.8);
            mTankPdControllers.add(pdController);
            mTimeDiffs.add((double) System.currentTimeMillis());
            tankGoalMap.put(i,0);
            tankAlignmentCounter.put(i,0);
            mServer.speed(i, 0);
        }
    }


    public void tick() throws IOException {
        ArrayList<NavigatorTank> army = mServer.getNavigatorTanks(mTeamColor);
        for(NavigatorTank tank : army) {
            int tankIndex = tank.getIndex();
            if(tankIndex > 0) continue;

//            if(tankIndex > 0) continue;

//            if(isTankStuck(tank) || tank.hasReachedGoal(tankGoalMap.get(tankIndex))) {
            if(tank.hasReachedGoal(tankGoalMap.get(tankIndex))) {
                System.out.println("Tank " + tankIndex + " is either stuck or has reached its goal of " + tank.getDesiredLocation(tankGoalMap.get(tankIndex)));
                int goalNum = tankGoalMap.get(tankIndex);
                goalNum++;
                if(goalNum > 7) goalNum = 0;
                tankGoalMap.put(tankIndex, goalNum);
                //tell tank to stop so it can get aligned
                mServer.speed(tankIndex, 0);
                tankAlignmentCounter.put(tankIndex, 0);
            }

            double newTime = System.currentTimeMillis();
            double timeDiffInSec = (newTime - mTimeDiffs.get(tankIndex)) / 1000;
            mTimeDiffs.set(tankIndex, newTime);

            double currAng = tank.getAngle();
            double currAngVel = tank.getAngVel();
            SeekGoalCircularPF pf = getGoalForTank(tank);
            Vector vectorForce = pf.getVectorForce(tank.getPos());
            double goalAngle = vectorForce.getAngle();

            double angAcceleration = mTankPdControllers.get(tankIndex).getAcceleration(goalAngle, currAng, timeDiffInSec);
            double targetVel = currAngVel + angAcceleration;

            mServer.angVel(tankIndex, targetVel);

            if(tankAlignmentCounter.get(tankIndex) > 55) {
                mServer.speed(tankIndex, 1);
            } else {
                int alignCount = tankAlignmentCounter.get(tankIndex);
                alignCount++;
                tankAlignmentCounter.put(tankIndex, alignCount);
            }
//            if(gen.nextDouble() < .2)
//                mServer.shoot(tankIndex);
        }

    }

    private boolean isTankStuck(NavigatorTank tank) {
        Point2D prevPos = tankPosMap.get(tank.getIndex());
        Point2D currPos = tank.getPos();
        tankPosMap.put(tank.getIndex(), currPos);

        return prevPos != null && prevPos.distance(currPos) < .3;
    }

    private SeekGoalCircularPF getGoalForTank(NavigatorTank t) {
        Point2D desiredLocation = t.getDesiredLocation(tankGoalMap.get(t.getIndex()));
//        System.out.println("Tank[" + t.getIndex() + "] goal: " + desiredLocation);
        return new SeekGoalCircularPF(1, desiredLocation, 30, 1);
    }

}
