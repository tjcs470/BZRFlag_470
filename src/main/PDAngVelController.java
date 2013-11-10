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
        double error = 0;//goalAng - currAng;
        if((goalAng > 0 && currAng < 0) || (goalAng < 0 && currAng > 0)) {
            double ang0 = (Math.PI - Math.abs(currAng)) + (Math.PI - Math.abs(goalAng));
            double ang1 = Math.abs(currAng) + Math.abs(goalAng);
            if(ang0 > ang1)
                error = ang1;
            else
                error = ang0;

            if(error < 0 && currAng > 0 || error > 0 && currAng < 0)
                error = -error;
        }
        else  {
           error = goalAng - currAng;
        }

        double errorDeriv = (Math.abs(error) - Math.abs(mPrevError)) / timeDiff;
        mPrevError = error;

        double force = mKP * error + mKD * errorDeriv;

        System.out.println("Time diffs: " + Double.toString(timeDiff));
        System.out.println("mKP: " + Double.toString(mKP));
        System.out.println("mKD: " + Double.toString(mKD));
        System.out.println("Goal angle: " + Double.toString(goalAng));
        System.out.println("Current angle: " + Double.toString(currAng));
        System.out.println("Error: " + Double.toString(error));
        System.out.println("Error derivative: " + Double.toString(errorDeriv));
        System.out.println("Force: " + Double.toString(force));

        return force;
    }
}
