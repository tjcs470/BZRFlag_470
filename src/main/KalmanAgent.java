package main;

import ServerResponse.MyTank;
import ServerResponse.Tank;
import math.geom2d.Point2D;
import org.apache.commons.math3.linear.AnyMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

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

    /**The elapsed time of the trial*/
    private double mElapsedTime = 0.0;


    private PDAngVelController controller = new PDAngVelController(.8, .2);


    public KalmanAgent(BZRFlag mServer, Tank.TeamColor enemyTankColor) throws IOException {
        this.mServer = mServer;
        this.enemyTankColor = enemyTankColor;
        mPrevTime = System.currentTimeMillis();
        kalmanPlot = new KalmanPlot();
        new GnuplotRadar(kalmanPlot);
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

    /**
     * Outputs the sigma values
     */
    private SigmaTracker xPosSigmaSamples = new SigmaTracker();
    private SigmaTracker yPosSigmaSamples = new SigmaTracker();
    private SigmaTracker xVelSigmaSamples = new SigmaTracker();
    private SigmaTracker yVelSigmaSamples = new SigmaTracker();
    private SigmaTracker xAccelSigmaSamples = new SigmaTracker();
    private SigmaTracker yAccelSigmaSamples = new SigmaTracker();;

    boolean xPosConverged = false;
    boolean xVelConverged = false;
    boolean xAccelConverged = false;
    boolean yPosConverged = false;
    boolean yVelConverged = false;
    boolean yAccelConverged = false;

    public void printSigmaValues() throws IOException {
        double xPosSigma = Math.sqrt(1.0 / mE_t.getEntry(0, 0));
        xPosSigmaSamples.recordSigma(xPosSigma);
        if(xPosSigmaSamples.converged() && !xPosConverged) {
            System.out.println("X position converged in " + xPosSigmaSamples.getTotalRecorded());
            System.out.println(xPosSigma);
            xPosConverged = true;
        }

        double xVelSigma = Math.sqrt(1.0 / mE_t.getEntry(1, 1));
        xVelSigmaSamples.recordSigma(xVelSigma);
        if(xVelSigmaSamples.converged() && !xVelConverged) {
            System.out.println("X velocity converged in " + xVelSigmaSamples.getTotalRecorded());
            System.out.println(xVelSigma);
            xVelConverged = true;
        }

        double xAccelSigma = Math.sqrt(1.0 / mE_t.getEntry(2, 2));
        xAccelSigmaSamples.recordSigma(xAccelSigma);
        if(xAccelSigmaSamples.converged() && !xAccelConverged) {
            System.out.println("X acceleration converged in " + xAccelSigmaSamples.getTotalRecorded());
            System.out.println(xAccelSigma);
            xAccelConverged = true;
        }

        double yPosSigma = Math.sqrt(1.0 / mE_t.getEntry(3, 3));
        yPosSigmaSamples.recordSigma(yPosSigma);
        if(yPosSigmaSamples.converged() && !yPosConverged) {
            System.out.println("Y position converged in " + yPosSigmaSamples.getTotalRecorded());
            System.out.println(yPosSigma);
            yPosConverged = true;
        }

        double yVelSigma = Math.sqrt(1.0 / mE_t.getEntry(4, 4));
        yVelSigmaSamples.recordSigma(yVelSigma);
        if(yVelSigmaSamples.converged() && !yVelConverged) {
            System.out.println("Y velocity converged in " + yVelSigmaSamples.getTotalRecorded());
            System.out.println(yVelSigma);
            yVelConverged = true;
        }

        double yAccelSigma = Math.sqrt(1.0 / mE_t.getEntry(5, 5));
        yAccelSigmaSamples.recordSigma(yAccelSigma);
        if(yAccelSigmaSamples.converged() && !yAccelConverged) {
            System.out.println("Y acceleration converged in " + yAccelSigmaSamples.getTotalRecorded());
            System.out.println(yAccelSigma);
            yAccelConverged = true;
        }


        /*System.out.println("x-position-sigma: " + xPosSigma);
        System.out.println("y-position-sigma: " + yPosSigma);
        System.out.println("x-velocity-sigma: " + xVelSigma);
        System.out.println("y-velocity-sigma: " + yVelSigma);
        System.out.println("x-acceleration-sigma: " + xAccelSigma);
        System.out.println("y-acceleration-sigma: " + yAccelSigma);
        System.out.println("");*/
    }

    int nObvs = 0;
    public void tick() throws IOException, InterruptedException {
        double newTime = System.currentTimeMillis();
        double timeDiffInSec = (newTime - mPrevTime) / 1000;
        mPrevTime = newTime;

        mElapsedTime += timeDiffInSec;
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

        //System.out.println("Sigma_t: " + mE_t.toString());
        printSigmaValues();

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
}
