package test;

import junit.framework.Assert;
import math.geom2d.Point2D;
import org.junit.Test;
import potentialFields.PotentialField;
import potentialFields.rectangular.AvoidObstacleTangentialRectangularPF;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;


/**
 * Created with IntelliJ IDEA.
 * User: Jordan
 * Date: 10/2/13
 * Time: 5:35 PM
 */
public class TestTangentialPF {

    @Test
    public void testPointEquality() {
        Point2D point = new Point2D(0,0);
        assertEquals(point, PotentialField.emptyVector());
    }

    @Test
    public void testRectTangPF() {
        PotentialField pf = new AvoidObstacleTangentialRectangularPF(0, true,
                new Point2D(-1, -1), new Point2D(-1,1), new Point2D(1,1), new Point2D(1,-1)); //sqaure around origin

        assertTrue(pf.isPointOnPotentialField(new Point2D(0, 0)));
        assertFalse(pf.isPointOnPotentialField(new Point2D(0, 2)));

        assertEquals(0.0, pf.getAngleToPotentialField(new Point2D(0, -2)));
        assertEquals(-Math.PI, pf.getAngleToPotentialField(new Point2D(0, 2)));
    }
}
