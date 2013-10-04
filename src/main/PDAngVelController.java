package main;

import Util.GeometryUtils;

/**
 * Created with IntelliJ IDEA.
 * User: ty
 * Date: 10/2/13
 * Time: 6:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class PDAngVelController {
    /**Error coeff*/
    private double mKP;
    /**Deriv coeff*/
    private double mKD;
    /**Previous error*/
    private double mPrevError;

    public PDAngVelController(double kP, double kD) {
        mKP = kP;
        mKD = kD;
    }

    public double getAcceleration(double goalAng, double currAng, double timeDiff){
        assert(timeDiff > 0.0);
        double error = GeometryUtils.angDiff(goalAng, currAng);
        double errorDeriv = (error - mPrevError) / timeDiff;
        mPrevError = error;
        double acceleration = mKP * error + mKD * errorDeriv;
        return acceleration;
    }
}
