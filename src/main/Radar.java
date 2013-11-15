package main;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: ty
 * Date: 11/12/13
 * Time: 10:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class Radar extends JFrame implements Runnable{

    /** Thread that runs the animation */
    private Thread mAnimator;

    public Radar(ProbabilityMap probabilityMap) {
        //ProbabilityMap probMap = new ProbabilityMap(800);
        add(probabilityMap);
        setTitle("Radar");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(probabilityMap.getWorldSize(), probabilityMap.getWorldSize());
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    public void addNotify() {
        super.addNotify();
        mAnimator = new Thread(this);
        mAnimator.start();
    }

    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2d = (Graphics2D) g;
        Toolkit.getDefaultToolkit().sync();
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
