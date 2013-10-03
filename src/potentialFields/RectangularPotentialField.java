package potentialFields;

import math.geom2d.Point2D;
import math.geom2d.polygon.SimplePolygon2D;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: larsojor
 * Date: 10/1/13
 * Time: 10:35 PM
 */
public abstract class RectangularPotentialField extends PotentialField {

    protected List<Point2D> points;
    private SimplePolygon2D polygon;
    protected double alpha;

    protected RectangularPotentialField(double alpha, Point2D... points) { //can also be constructed with array
        this(alpha, Arrays.asList(points));
    }

    protected RectangularPotentialField(double alpha, List<Point2D> points) {
        this.points = points;
        this.alpha = alpha;
        polygon = new SimplePolygon2D(points);
    }

    @Override
    public double getDistanceToPotentialField(Point2D location) {
        return polygon.distance(location);
    }

    @Override
    public double getAngleToPotentialField(Point2D location) {
        Point2D center = polygon.centroid();
        return Math.atan2(center.y() - location.y(), center.x() - location.x());
    }

    @Override
    public boolean isPointOnPotentialField(Point2D point) {
        return polygon.contains(point);
    }
}

