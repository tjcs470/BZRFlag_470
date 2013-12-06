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

    private RealMatrix mFTranspose = mF.transpose();
    private RealMatrix mH = new Array2DRowRealMatrix(new double[][] {{1, 0, 0, 0, 0, 0}, {0, 0, 0, 1, 0, 0}});
    private RealMatrix mHTranspose = mH.transpose();

    private RealMatrix mE_z = new Array2DRowRealMatrix(new double[][] {{25, 0}, {0, 25}});

    private BZRFlag mServer;
    private Tank.TeamColor enemyTankColor;

    private RealMatrix kalmanGainMatrix;


    public KalmanAgent(BZRFlag mServer, Tank.TeamColor enemyTankColor) {
        this.mServer = mServer;
        this.enemyTankColor = enemyTankColor;
    }

    public RealMatrix getLocationAsMatrix() throws IOException {
        for(Tank t : mServer.getOtherTanks()) {
            if(t.getColor() == enemyTankColor) {
                Vector pos = t.getPos();
                return new Array2DRowRealMatrix(new double[][]{{pos.x(), pos.y()}});
            }
        }
        throw new IllegalArgumentException("Could not find tank with color " + enemyTankColor);
    }

    public void updateKalmanGainMatrix() {
        RealMatrix firstChunk = mF.multiply(mE_t).multiply(mFTranspose).add(mE_x);

        firstChunk = firstChunk.multiply(mHTranspose);

        RealMatrix secondChunk = mH.multiply(firstChunk).add(mE_z);

        kalmanGainMatrix = firstChunk.multiply(MatrixUtils.blockInverse(secondChunk, 0));
    }

    public RealMatrix getMeanUpdate() throws IOException {
        RealMatrix first = mF.multiply(mU_t);
        RealMatrix inner = getLocationAsMatrix().subtract(mH.multiply(mF).multiply(mU_t));

        return first.add(kalmanGainMatrix.multiply(inner));
    }

    public RealMatrix getNoiseUpdate() {
        RealMatrix left = MatrixUtils.createRealIdentityMatrix(4).subtract(kalmanGainMatrix.multiply(mH));
        RealMatrix right = mF.multiply(mE_t).multiply(mFTranspose).add(mE_x);

        return left.multiply(right);
    }
}
