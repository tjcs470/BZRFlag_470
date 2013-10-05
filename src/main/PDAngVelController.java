package main;

import Util.GeometryUtils;

/**
 * Created with IntelliJ IDEA.
 * User: ty
 * Date: 10/2/13
 * Time: 6:23 PM
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
        if(timeDiff <= 0.0)
            timeDiff = .000001;
        double error = goalAng - currAng;
        double errorDeriv = (error - mPrevError) / timeDiff;
        mPrevError = error;
        return mKP * error + mKD * errorDeriv;
    }
}
