package main;

import com.panayotis.gnuplot.GNUPlotParameters;
import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.plot.Graph3D;
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
    /** X-Noise */
    private double mXSigma;
    /** Y-Noise */
    private double mYSigma;
    /** Rho */
    private double mRho;

    /**
     * Constructor
     */
    public KalmanPlot() {
        mXSigma = 30;
        mYSigma = 40;
        mRho = 0.2;

        JavaPlot p = new JavaPlot(true);
        ImageTerminal imageTerminal = new ImageTerminal();
        p.setTerminal(imageTerminal);
        p.set("xrange", "[-400.0: 400.0]");
        p.set("yrange", "[-400.0: 400.0]");
        p.set("pm3d", "");
        p.set("view", "map");
        p.set("size", "square");
        p.set("isosamples", "100");
        String plotStrFormat = "1.0/ (2.0 * pi * %f * %f * sqrt(1 - %f**2) )  * exp(-1.0/2.0 * (x**2 / %f**2 + y**2 / %f**2 - 2.0*%f*x * (y - 0) /(%f*%f) ) ) with pm3d";
        String plotStr = String.format(plotStrFormat, mXSigma, mYSigma, mRho, mXSigma, mYSigma, mRho, mXSigma, mYSigma);
        p.addPlot(plotStr);
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

        mXSigma += 1;
        if(mXSigma > 100)
            mXSigma = 20;

        JavaPlot p = new JavaPlot(true);
        ImageTerminal imageTerminal = new ImageTerminal();
        p.setTerminal(imageTerminal);
        p.set("xrange", "[-400.0: 400.0]");
        p.set("yrange", "[-400.0: 400.0]");
        p.set("pm3d", "");
        p.set("view", "map");
        p.set("size", "square");
        p.set("isosamples", "100");
        String plotStrFormat = "1.0/ (2.0 * pi * %f * %f * sqrt(1 - %f**2) )  * exp(-1.0/2.0 * (x**2 / %f**2 + y**2 / %f**2 - 2.0*%f*x * (y - 0) /(%f*%f) ) ) with pm3d";
        String plotStr = String.format(plotStrFormat, mXSigma, mYSigma, mRho, mXSigma, mYSigma, mRho, mXSigma, mYSigma);
        p.addPlot(plotStr);
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
