package main;

import ServerResponse.MyTank;
import ServerResponse.Tank;
import math.geom2d.Point2D;
import org.apache.commons.math3.linear.AnyMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: ty
 * Date: 12/5/13
 * Time: 8:12 AM
 */

public class KalmanAgent {

    private RealMatrix mU_t = new Array2DRowRealMatrix(new double[]{0,0,0,0,0,0});
    private RealMatrix mE_t = new Array2DRowRealMatrix(new double[][]
            {{100,0,0,0,0,0},
                {0,.1,0,0,0,0},
                {0,0,.1,0,0,0},
                {0,0,0,100,0,0},
                {0,0,0,0,.1,0},
                {0,0,0,0,0,.1},
            });
    private RealMatrix mE_x = new Array2DRowRealMatrix(new double[][]
            {   {.1, 0, 0, 0, 0, 0 },
                {0, .1, 0, 0, 0, 0 },
                {0, 0, 100, 0, 0, 0},
                {0, 0, 0, .1, 0, 0 },
                {0, 0, 0, 0, .1, 0 },
                {0, 0, 0, 0, 0, 100},
            });

    private RealMatrix mF = new Array2DRowRealMatrix(new double[][]
    {   {1, 0, 0, 0, 0, 0},
        {0, 1, 0, 0, 0, 0},
        {0, 0, 1, 0, 0, 0},
        {0, 0, 0, 1, 0, 0},
        {0, 0, 0, 0, 1, 0},
        {0, 0, 0, 0, 0, 1},
    });

    private RealMatrix mH = new Array2DRowRealMatrix(new double[][] {{1, 0, 0, 0, 0, 0}, {0, 0, 0, 1, 0, 0}});
    private RealMatrix mHTranspose = mH.transpose();

    private RealMatrix mE_z = new Array2DRowRealMatrix(new double[][] {{25, 0}, {0, 25}});

    private BZRFlag mServer;
    private Tank.TeamColor enemyTankColor;

    private RealMatrix kalmanGainMatrix;

    /**Time delta between ticks*/
    private double mPrevTime;

    private KalmanPlot kalmanPlot;

    private int obsvCounter;


    private PDAngVelController controller = new PDAngVelController(.8, .2);


    public KalmanAgent(BZRFlag mServer, Tank.TeamColor enemyTankColor) throws IOException {
        this.mServer = mServer;
        this.enemyTankColor = enemyTankColor;
        mPrevTime = System.currentTimeMillis();
        kalmanPlot = new KalmanPlot();
        new GnuplotRadar(kalmanPlot);
        obsvCounter = 0;
    }


    public RealMatrix getLocationAsMatrixForColor(Tank.TeamColor color) throws IOException {
        for(Tank t : mServer.getOtherTanks()) {
            if(t.getColor() == color) {
                Point2D pos = t.getPos();
                if(t.getStatus() == Tank.TankStatus.DEAD) {
                    pos = mServer.getBases().get(color).getCenter();
                }
                return new Array2DRowRealMatrix(new double[][]{{pos.x()}, {pos.y()}});
            }
        }
        throw new IllegalArgumentException("Could not find tank with color " + color);
    }

    public RealMatrix getLocationAsMatrixOfEnemyTank() throws IOException {
        return getLocationAsMatrixForColor(enemyTankColor);
    }

    public void updateKalmanGainMatrix() {
        RealMatrix firstChunk = mF.multiply(mE_t).multiply(mF.transpose()).add(mE_x);

        firstChunk = firstChunk.multiply(mHTranspose);

        RealMatrix secondChunk = mH.multiply(firstChunk).add(mE_z);

        kalmanGainMatrix = firstChunk.multiply(MatrixUtils.blockInverse(secondChunk, 0));
    }

    public void getMeanUpdate() throws IOException {
        RealMatrix first = mF.multiply(mU_t);
        RealMatrix inner = getLocationAsMatrixOfEnemyTank().subtract(mH.multiply(mF).multiply(mU_t));

        mU_t = first.add(kalmanGainMatrix.multiply(inner));
    }

    public void getNoiseUpdate() {
        RealMatrix left = MatrixUtils.createRealIdentityMatrix(6).subtract(kalmanGainMatrix.multiply(mH));
        RealMatrix right = mF.multiply(mE_t).multiply(mF.transpose()).add(mE_x);

        mE_t = left.multiply(right);
    }

    public void updatePhysics(double deltaT) {
        mF.setEntry(0, 1, deltaT);
        mF.setEntry(0, 2, deltaT * deltaT / 2.0);
        mF.setEntry(1, 2, deltaT);
        mF.setEntry(3, 4, deltaT);
        mF.setEntry(3, 5, deltaT * deltaT / 2.0);
        mF.setEntry(4, 5, deltaT);
    }

    int nObvs = 0;
    public void tick() throws IOException, InterruptedException {
        double newTime = System.currentTimeMillis();
        double timeDiffInSec = (newTime - mPrevTime) / 1000;
        mPrevTime = newTime;

        //System.out.println(timeDiffInSec);

        updateKalmanGainMatrix();
        updatePhysics(timeDiffInSec);
        getMeanUpdate();
        getNoiseUpdate();

        double xSigma = Math.sqrt(1.0 / mE_t.getEntry(0, 0));
        double ySigma = Math.sqrt(1.0 / mE_t.getEntry(3, 3));
        kalmanPlot.setXSigma(xSigma);
        kalmanPlot.setYSigma(ySigma);
        kalmanPlot.setTargetPos(new Vector(mU_t.getEntry(0, 0), mU_t.getEntry(3, 0)));

        System.out.println("Sigma_t: " + mE_t.toString());

        ///leadAndShoot();

        MyTank myTank = mServer.getMyTanks(Tank.TeamColor.BLUE).get(0);
        Vector currTargetPos = getPos(mU_t);
        double distToTarget = Point2D.distance(currTargetPos,myTank.getPos());
        double bulletTravelTime = distToTarget / 100.0;

        updatePhysics(bulletTravelTime);
        RealMatrix meanPrediction = mF.multiply(mU_t);
        Vector targetPosition = new Vector(meanPrediction.getEntry(0, 0), meanPrediction.getEntry(3,0));
        double deltaY = targetPosition.y() - myTank.getPos().y();
        double deltaX = targetPosition.x() - myTank.getPos().x();
        double targetAngle = Math.atan2(deltaY, deltaX);

        if(Math.abs(myTank.getAngle() - targetAngle) < 0.05)
            mServer.shoot(0);

        mServer.angVel(0, 1.1 * controller.getAcceleration(targetAngle, myTank.getAngle(), timeDiffInSec));

        /*mServer.shoot(0);
        Thread.sleep(2000);
        mServer.shoot(0);*/
        //System.out.println("Time " + (System.currentTimeMillis() - current));
    }

    /**
     * Returns the position in the state vector
     * @return
     */
    public Vector getPos(RealMatrix stateVector) {
        return new Vector(stateVector.getEntry(0, 0), stateVector.getEntry(3,0));
    }

    /**
     * Gets a point in front of the tank
     */
    public void leadAndShoot() throws IOException, InterruptedException {
        double timeToPrepareStart = System.currentTimeMillis();

        double waitTime = 7.0;

        updatePhysics(waitTime);
        RealMatrix meanPrediction = mF.multiply(mU_t);
        System.out.println("Predicted: " + meanPrediction.toString());

        MyTank myTank = mServer.getMyTanks(Tank.TeamColor.BLUE).get(0);

        Vector targetPosition = new Vector(meanPrediction.getEntry(0, 0), meanPrediction.getEntry(3,0));
        double deltaY = targetPosition.y() - myTank.getPos().y();
        double deltaX = targetPosition.x() - myTank.getPos().x();
        double targetAngle = Math.atan2(deltaY, deltaX);

        // line up
        double prevTime = mPrevTime;
        while(true) {
            double newTime = System.currentTimeMillis();
            double timeDiffInSec = (newTime - prevTime) / 1000;
            prevTime = newTime;

            myTank = mServer.getMyTanks(Tank.TeamColor.BLUE).get(0);
            double currAngVel = myTank.getAngVel();
            double angAccel = controller.getAcceleration(targetAngle, myTank.getAngle(), timeDiffInSec);
            mServer.angVel(0, currAngVel + angAccel);
            if(Math.abs(angAccel) < 0.01 && Math.abs(myTank.getAngle() - targetAngle) < 0.01)
                break;
        }

        // calculate bullet travel time
        double distToTarget = Point2D.distance(targetPosition, myTank.getPos());
        double bulletTravelTime = distToTarget / 100.0;

        double timeToPrepare = (System.currentTimeMillis() - timeToPrepareStart) / 1000;

        System.out.println("Bullet travel time: " + bulletTravelTime);
        System.out.println("Time to prepare: " + timeToPrepare);

        waitTime -= bulletTravelTime;
        waitTime -= timeToPrepare;

        //System.out.println(waitTime);
        long waitTimeMilli = (long) (waitTime * 1000);
        System.out.println("How long we should wait: " + waitTimeMilli);
        System.out.println("Before wait: " + (System.currentTimeMillis() - timeToPrepareStart));
        if(waitTimeMilli > 0)
            Thread.sleep(waitTimeMilli);

        mServer.shoot(0);
        System.out.println("End: " + (System.currentTimeMillis() - timeToPrepareStart));
        System.out.println("");
    }
}
