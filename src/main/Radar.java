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
public class Radar extends JFrame {

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

}
