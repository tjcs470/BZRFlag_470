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
public class ProbabilityMap extends JPanel {

    /** Origin of the world */
    private int mImageXOrig;
    private int mImageYOrig;
    /** World size */
    private int mWorldSize;
    private BufferedImage mRaster;
    private final Object lock = new Object();

    public ProbabilityMap(int worldSize) {
        mWorldSize = worldSize;
        mImageXOrig = mWorldSize / 2;
        mImageYOrig = mWorldSize / 2;
        mRaster =  new BufferedImage(mWorldSize + 20, mWorldSize + 20, BufferedImage.TYPE_BYTE_GRAY);
    }

    public void setProbability(int worldX, int worldY, float probability) {
        synchronized (lock) {
            int imageX = mImageXOrig + worldX;
            int imageY = mImageYOrig - worldY;

            System.out.println("Given x: " + Integer.toString(worldX));
            System.out.println("Given y: " + Integer.toString(worldY));
            System.out.println("X: " + Integer.toString(imageX));
            System.out.println("Y: " + Integer.toString(imageY));

            if(imageY < 0 || imageX < 0)
                return;

            float[] pixel = {probability * 255, probability * 255, probability * 255};
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
            g2.drawImage(mRaster, null, null);
        }
    }
}
