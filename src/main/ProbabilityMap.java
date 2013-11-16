package main;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

/**
 * Created with IntelliJ IDEA.
 * User: ty
 * Date: 11/12/13
 * Time: 10:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProbabilityMap extends JPanel implements Runnable{
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

    /** Thread that runs the animation */
    private Thread mAnimator;

    /** Origin of the world */
    private int mImageXOrig;
    private int mImageYOrig;
    /** World size */
    private int mWorldSize;
    private BufferedImage mRaster;
    private final Object lock = new Object();

    public ProbabilityMap(int worldSize, double initProb, double truePos, double trueNeg) {
        setDoubleBuffered(true);
        mWorldSize = worldSize;
        mImageXOrig = mWorldSize / 2;
        mImageYOrig = mWorldSize / 2;
        mRaster =  new BufferedImage(mWorldSize + 20, mWorldSize + 20, BufferedImage.TYPE_BYTE_GRAY);

        mProbOccupied = new double [worldSize][worldSize];
        for(int i = 0; i < mWorldSize; i++) {
            for(int j = 0; j < mWorldSize; j++) {
                mProbOccupied[i][j] = initProb;
                setProbability(i, j, (float) initProb);
            }
        }

        mTruePos = truePos;
        mFalsePos = 1.0 - mTruePos;
        mTrueNeg = trueNeg;
        mFalseNeg = 1.0 - mTrueNeg;
    }

    /**
     * Filters belief of occupancy
     */
    public void updateProbability(int worldX, int worldY, boolean observedOccupied)
    {
        int imageX = mImageXOrig + worldX;
        int imageY = mImageYOrig - worldY;

        if(imageY < 0 || imageX < 0 || imageX >= getWorldSize() || imageY >= getWorldSize())
            return;

        if(observedOccupied) {
            double truePos = mTruePos * mProbOccupied[imageX][imageY];
            double falsePos = mFalsePos * (1.0 - mProbOccupied[imageX][imageY]);
            mProbOccupied[imageX][imageY] = truePos / (truePos + falsePos);
        }
        else {
            double trueNeg = mTrueNeg * (1.0 - mProbOccupied[imageX][imageY]);
            double falseNeg = mFalseNeg * mProbOccupied[imageX][imageY];
            mProbOccupied[imageX][imageY] = falseNeg / (trueNeg + falseNeg);
        }

        setProbability(imageX, imageY, (float) mProbOccupied[imageX][imageY]);
        //setProbability(imageX, imageY, 1.0f);
    }

    /**
     * Sets the probability in the raster
     * @param imageX
     * @param imageY
     * @param probability
     */
    private void setProbability(int imageX, int imageY, float probability) {
        synchronized (lock) {
            /*System.out.println("Given x: " + Integer.toString(worldX));
            System.out.println("Given y: " + Integer.toString(worldY));
            System.out.println("X: " + Integer.toString(imageX));
            System.out.println("Y: " + Integer.toString(imageY));*/

            if(imageX < 0 || imageY < 0)
                return;

            float[] pixel = {(1 - probability) * 255, (1 - probability) * 255, (1 - probability) * 255};
            mRaster.getRaster().setPixel(imageX, imageY, pixel);
        }
    }

    public int getWorldSize() {
        return mWorldSize;
    }

    public void paint(Graphics g) {
        synchronized (lock) {
            super.paint(g);
            Graphics2D g2 = (Graphics2D) g;
            Toolkit.getDefaultToolkit().sync();
            g2.drawImage(mRaster, null, null);
            g.dispose();
        }
    }

    public void addNotify() {
        super.addNotify();
        mAnimator = new Thread(this);
        mAnimator.start();
    }

    @Override
    public void run() {
        while(true) {
            repaint();

            try {
                Thread.sleep(50);
            }
            catch (InterruptedException e) {
                System.out.println("Interuppted");
            }
        }
    }
}
