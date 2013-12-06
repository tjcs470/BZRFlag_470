package main;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: ty
 * Date: 12/3/13
 * Time: 8:46 PM
 */
public class GnuplotRadar extends JFrame {
    public GnuplotRadar(KalmanPlot kalmanPlot) {
        //ProbabilityMap probMap = new ProbabilityMap(800);
        //KalmanPlot kalmanPlot = new KalmanPlot();
        add(kalmanPlot);
        setTitle("Radar");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(kalmanPlot.getWidth(), kalmanPlot.getHeight());
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    public static void main(String[] args) {
        //GnuplotRadar gnuplotRadar = new GnuplotRadar();
    }
}
