package main;

import ServerResponse.*;
import math.geom2d.Point2D;
import potentialFields.circular.SeekGoalCircularPF;

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
    private List<PDAngVelController> mTankPdControllers;
    private List<Double> mTimeDiffs = new ArrayList<Double>(10);
    private Map<Integer, Integer> tankGoalMap = new HashMap<Integer, Integer>(); //stores current goal of tank
    private Map<Integer, Point2D> tankPosMap = new HashMap<Integer, Point2D>(); //stores previous position of tank for stuck detection
    /**The probability map*/
    private ProbabilityMap mProbabilityMap;
    /**JFrame that renders the probability map*/
    private Radar mRadar;
    private final int tankAlignments = 10;
    private Map<Integer, Integer> tankAlignmentCounter = new HashMap<Integer, Integer>(); //gives tank time to align
    Random gen = new Random();
    private boolean debug = false;
    private Set<Integer> validTankIndexes = new HashSet<Integer>();

    public NavigatorAgent(BZRFlag teamConnection, Tank.TeamColor myTeamColor) throws IOException {
        mServer = teamConnection;
        mServer.handshake();
        mTeamColor = myTeamColor;
        mTankPdControllers = new ArrayList<PDAngVelController>();
        for(int i = 0; i < 10; i++) {
            PDAngVelController pdController = new PDAngVelController(.2, .8);
            mTankPdControllers.add(pdController);
            mTimeDiffs.add((double) System.currentTimeMillis());
            tankGoalMap.put(i,0);
            tankAlignmentCounter.put(i,0);
            mServer.speed(i, 0);
        }
        ServerConstants serverConstants = mServer.getConstants();
        mProbabilityMap = new ProbabilityMap(serverConstants.worldSize, 0.5, serverConstants.truePos, serverConstants.trueNeg);
        mRadar = new Radar(mProbabilityMap);
        validTankIndexes.add(1);
        validTankIndexes.add(4);
        validTankIndexes.add(8);
    }


    public void tick() throws IOException {
        ArrayList<NavigatorTank> army = mServer.getNavigatorTanks(mTeamColor);

        for(NavigatorTank tank : army) {
            int tankIndex = tank.getIndex();
            if(!validTankIndexes.contains(tankIndex)) continue;
            if(tank.getStatus() == Tank.TankStatus.DEAD) {
                System.out.println("Tank["+tankIndex+"] is dead");
                tankAlignmentCounter.put(tankIndex, 0); // need to re align if killed
                continue;
            }

            OccGridResponse gridResponse = mServer.readOccGrid(tankIndex);
            for(int row = 0; row < gridResponse.rows; row++) {
                for(int col = 0; col < gridResponse.cols; col++) {
                    mProbabilityMap.updateProbability(gridResponse.x + row, gridResponse.y + col, gridResponse.occupiedObservation[row][col]);
                    /*if(gridResponse.occupiedObservation[row][col])
                        mProbabilityMap.updateProbability(gridResponse.x + row, gridResponse.y + col, 1);
                    else
                        mProbabilityMap.updateProbability(gridResponse.x + row, gridResponse.y + col, .75f);*/
                }
            }
            System.out.println(mServer.readOccGrid(tankIndex).occupiedObservation);

            if(isTankStuck(tank) || tank.hasReachedGoal(tankGoalMap.get(tankIndex))) {
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

            if(debug) {
                System.out.println("Curr ang vel: " + currAngVel);
                System.out.println("ang acc: " + angAcceleration);
                System.out.println("target vel: " + targetVel);
                System.out.println("\n");
            }

            mServer.angVel(tankIndex, targetVel);

            int alignCount = tankAlignmentCounter.get(tankIndex);
            if(alignCount > tankAlignments) {
                mServer.speed(tankIndex, 1);
            }

            alignCount++;
            System.out.println("Tank["+tankIndex+"] align count is " +alignCount);
            tankAlignmentCounter.put(tankIndex, alignCount);
//            if(gen.nextDouble() < .1)
//                mServer.shoot(tankIndex);
        }

    }

    private boolean isTankStuck(NavigatorTank tank) {
        if(tankAlignmentCounter.get(tank.getIndex()) < (tankAlignments + 5)) return false; //tank is positioning itself, or has just barely started moving which is what the +5 is for
        Point2D prevPos = tankPosMap.get(tank.getIndex());
        Point2D currPos = tank.getPos();
        tankPosMap.put(tank.getIndex(), currPos);

        return prevPos != null && prevPos.distance(currPos) < .1;
    }

    private SeekGoalCircularPF getGoalForTank(NavigatorTank t) {
        Point2D desiredLocation = t.getDesiredLocation(tankGoalMap.get(t.getIndex()));
//        System.out.println("Tank[" + t.getIndex() + "] goal: " + desiredLocation);
        return new SeekGoalCircularPF(1, desiredLocation, 30, 1);
    }

}
