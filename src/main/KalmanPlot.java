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
    private Integer mFoo = 1;

    /**
     * Constructor
     */
    public KalmanPlot() {
        JavaPlot p = new JavaPlot(true);
        ImageTerminal imageTerminal = new ImageTerminal();
        p.setTerminal(imageTerminal);

        //p.addPlot("sin(x)");
        p.set("xrange", "[-400.0: 400.0]");
        p.set("yrange", "[-400.0: 400.0]");
        p.set("pm3d", "");
        p.set("view", "map");
        p.set("size", "square");
        p.set("isosamples", "100");
        String plotStr = "1.0/ (2.0 * pi * 30 * 20 * sqrt(1 - 0.2**2) )  * exp(-1.0/2.0 * ((x - 0)**2 / 30**2 + (y - 0)**2 / 20**2 - 2.0*0.2*(x - 0) * (y - 0) /(30*20) ) ) with pm3d";
        p.addPlot(plotStr);
        //p.addPlot("sin(x)*sin(y)");
        //p.addPlot(plotStr);

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

        /*p.set("pm3d", "");
        p.set("view", "map");
        p.set("size", "square");
        p.set("isosamples", "100");*/
        //p.plot();
        //mRaster =  imageTerminal.getImage();

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
