package main;

import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.terminal.ImageTerminal;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created with IntelliJ IDEA.
 * User: ty
 * Date: 12/3/13
 * Time: 8:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class KalmanPlot extends JPanel implements Runnable {
    /** Raster of the plot */
    private BufferedImage mRaster;
    /** Thread that runs the animation */
    private Thread mAnimator;
    private Integer mFoo = 1;

    /**
     * Constructor
     */
    public KalmanPlot() {
        JavaPlot p = new JavaPlot();
        ImageTerminal imageTerminal = new ImageTerminal();
        p.setTerminal(imageTerminal);
        p.addPlot("sin(x)");
        p.plot();
        mRaster =  imageTerminal.getImage();
    }

    /**
     * Gets the raster width
     * @return
     */
    public int getWidth()
    {
        return mRaster.getWidth();
    }

    /**
     * Gets the raster height
     */
    public int getHeight()
    {
        return mRaster.getHeight();
    }

    public void addNotify() {
        super.addNotify();
        mAnimator = new Thread(this);
        mAnimator.start();
    }
    /**
     * Redraws the plot
     * @param g
     */
    public void paint(Graphics g) {
        super.paint(g);

        JavaPlot p = new JavaPlot();
        ImageTerminal imageTerminal = new ImageTerminal();
        p.setTerminal(imageTerminal);
        mFoo += 1;
        if(mFoo > 10)
            mFoo = 0;
        //p.addPlot("sin(x - " + mFoo + ")");
        p.addPlot("sin(x - " + mFoo.toString() + ")");
        p.plot();
        mRaster =  imageTerminal.getImage();

        Graphics2D g2 = (Graphics2D) g;
        Toolkit.getDefaultToolkit().sync();
        g2.drawImage(mRaster, null, null);
        g.dispose();
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
