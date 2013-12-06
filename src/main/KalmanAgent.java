package main;

import ServerResponse.Tank;
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

    private RealMatrix mE_z = new Array2DRowRealMatrix(new double[][] {{0, 0}, {0, 0}});

    private BZRFlag mServer;
    private Tank.TeamColor enemyTankColor;

    private RealMatrix kalmanGainMatrix;

    /**Time delta between ticks*/
    private double mPrevTime;

    private KalmanPlot kalmanPlot;


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
                Vector pos = t.getPos();
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

    public RealMatrix getMeanUpdate() throws IOException {
        RealMatrix first = mF.multiply(mU_t);
        RealMatrix inner = getLocationAsMatrixOfEnemyTank().subtract(mH.multiply(mF).multiply(mU_t));

        return first.add(kalmanGainMatrix.multiply(inner));
    }

    public RealMatrix getNoiseUpdate() {
        RealMatrix left = MatrixUtils.createRealIdentityMatrix(6).subtract(kalmanGainMatrix.multiply(mH));
        RealMatrix right = mF.multiply(mE_t).multiply(mF.transpose()).add(mE_x);

        return left.multiply(right);
    }

    public void updatePhysics(double deltaT) {
        mF.setEntry(0, 1, deltaT);
        mF.setEntry(0, 2, deltaT * deltaT / 2.0);
        mF.setEntry(1, 2, deltaT);
        mF.setEntry(3, 4, deltaT);
        mF.setEntry(3, 5, deltaT * deltaT / 2.0);
        mF.setEntry(4, 5, deltaT);
    }

    public void tick() throws IOException {
        double newTime = System.currentTimeMillis();
        double timeDiffInSec = (newTime - mPrevTime) / 1000;
        mPrevTime = newTime;

        Tank myTank = mServer.getMyTanks(Tank.TeamColor.BLUE).get(0);

        updateKalmanGainMatrix();
        updatePhysics(timeDiffInSec);
        RealMatrix meanUpdate = getMeanUpdate();
        RealMatrix noiseUpdate = getNoiseUpdate();

        double xSigma = Math.sqrt(noiseUpdate.getEntry(0, 0));
        double ySimga = Math.sqrt(noiseUpdate.getEntry(3, 3));
        kalmanPlot.setXSigma(xSigma);
        kalmanPlot.setYSigma(ySimga);
        kalmanPlot.setTargetPos(new Vector(meanUpdate.getEntry(0, 0), meanUpdate.getEntry(3, 0)));

        RealMatrix meanPrediction = mF.multiply(meanUpdate);
        Vector targetPosition = new Vector(meanPrediction.getEntry(0, 0), meanPrediction.getEntry(3,0));
        double targetAngle = Math.atan2(targetPosition.y() - myTank.getPos().y(), targetPosition.x() - myTank.getPos().x());

        mServer.angVel(0, controller.getAcceleration(targetAngle, myTank.getAngle(), timeDiffInSec));

        mServer.shoot(0);
    }
}
