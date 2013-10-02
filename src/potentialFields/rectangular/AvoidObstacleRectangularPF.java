package potentialFields.rectangular;

import main.Vector;
import math.geom2d.Point2D;
import potentialFields.RectangularPotentialField;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: larsojor
 * Date: 10/1/13
 * To change this template use File | Settings | File Templates.
 */
public class AvoidObstacleRectangularPF extends RectangularPotentialField {
    public AvoidObstacleRectangularPF(Point2D... points) {
        super(points);
    }

    public AvoidObstacleRectangularPF(List<Point2D> points) {
        super(points);
    }

    @Override
    protected Vector getVectorForce(Point2D location) {
        return emptyVector();
    }
}
