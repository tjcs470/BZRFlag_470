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
            System.out.println("Ang0, Ang1: " + ang0 + ", " + ang1);
            if(ang0 > ang1) {
                error = ang1;
                if(currAng > 0) {
                    error = -error;
                }
            }
            else{
                error = ang0;
                if(currAng < 0) {
                    error = -error;
                }
            }

//            if(currAng > 0 )
//            if(error < 0 && currAng > 0 || error > 0 && currAng < 0) {
//                System.out.println("Error switching signs");
//                error = -error;
//            }
        }
        else  {
           error = goalAng - currAng;
        }

        double errorDeriv = (Math.abs(error) - Math.abs(mPrevError)) / timeDiff;
        mPrevError = error;

        double force = mKP * error + mKD * errorDeriv;

        System.out.println("Time diff: " + timeDiff);
        System.out.println("Goal angle: " + goalAng);
        System.out.println("Current angle: " + currAng);
        System.out.println("Error: " + error);
        System.out.println("Error derivative: " + errorDeriv);
        System.out.println("Force: " + force);

        return force;
    }
}
