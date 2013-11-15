package main;

/**
 * Created with IntelliJ IDEA.
 * User: ty
 * Date: 11/9/13
 * Time: 4:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class GridFilter {
    /** The size of the grid */
    private int mGridSize;
    /** P(S = occupied | O)*/
    private double [][] mProbOccupied;
    /** P(O = occupied | S = occupied) the true positive */
    private double mTruePos;
    /** P(O = unoccupied | S = occupied) the true positive */
    private double mFalsePos;
    /** P(O = unoccupied | S = unoccupied) the true negative */
    private double mTrueNeg;
    /** P(O = unoccupied | S = occupied) the false negative */
    private double mFalseNeg;
    /**The image used to represent the grid*/
    private ProbabilityMap mProbabilityMap;

    /**
     * Constructor
     */
    public GridFilter(int worldSize, double initProb, double truePos, double trueNeg) {
        assert(initProb > 0 && initProb <= 1.0);

        mGridSize = worldSize;

        mProbOccupied = new double [worldSize][worldSize];
        for(int i = 0; i < mGridSize; i++)
            for(int j = 0; j < mGridSize; j++)
                mProbOccupied[i][j] = initProb;

        mTruePos = truePos;
        mFalsePos = 1.0 - mTruePos;
        mTrueNeg = trueNeg;
        mFalseNeg = 1.0 - mTrueNeg;
    }

    /**
     * Filters belief of occupancy
     */
    public void filter(int i, int j, boolean observedOccupied)
    {
        if(observedOccupied) {
            double truePos = mTruePos * mProbOccupied[i][j];
            double falsePos = mFalsePos * (1.0 - mProbOccupied[i][j]);
            mProbOccupied[i][j] = truePos / (truePos + falsePos);
        }
        else {
            double trueNeg = mTrueNeg * (1.0 - mProbOccupied[i][j]);
            double falseNeg = mFalseNeg * mProbOccupied[i][j];
            mProbOccupied[i][j] = falseNeg / (trueNeg + falseNeg);
        }
    }
}
