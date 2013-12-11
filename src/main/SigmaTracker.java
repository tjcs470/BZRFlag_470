package main;

/**
 * Created with IntelliJ IDEA.
 * User: ty
 * Date: 12/11/13
 * Time: 2:52 PM
 * To change this template use File | Settings | File Templates.
 */

import java.util.LinkedList;
import java.util.Queue;

/**
 * Tracks the sigma values
 */
public class SigmaTracker {
    /**Sigma samples*/
    private Queue<Double> mSigmaSamples = new LinkedList<Double>();
    /**The number of samples taken*/
    private int mNSamples = 0;
    /**Max number of samples*/
    private int MAX_SAMPLES = 10;

    /**
     * Records a sigma value
     * @param sigma
     */
    public void recordSigma(Double sigma) {
        if(mSigmaSamples.size() < MAX_SAMPLES) {
            mSigmaSamples.add(sigma);
        }
        else {
            mSigmaSamples.remove();
            mSigmaSamples.add(sigma);
        }

        mNSamples += 1;
    }

    boolean converged() {
        if(mSigmaSamples.size() < MAX_SAMPLES)
            return false;

        Double percentDiff = 1.0 - (minSigma() / maxSigma());
        return (percentDiff < 0.1);
    }

    /**
     * Gets the maximum
     * @return
     */
    private Double maxSigma() {
        Double maxSigma = Double.MIN_VALUE;
        for(Double sigma : mSigmaSamples) {
            if(sigma > maxSigma)
                maxSigma = sigma;
        }
        return maxSigma;
    }

    /**
     * Gets the minimum recorded sigma sample
     */
    private Double minSigma() {
        Double minSigma = Double.MAX_VALUE;
        for(Double sigma : mSigmaSamples) {
            if(sigma < minSigma)
                minSigma = sigma;
        }
        return minSigma;
    }
}

