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

    protected RectangularPotentialField(Point2D... points) { //can also be constructed with array
        this(Arrays.asList(points));
    }

    protected RectangularPotentialField(List<Point2D> points) {
        this.points = points;
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
        int i;
        int j;
        boolean result = false;
        for (i = 0, j = points.size() - 1; i < points.size(); j = i++) {
            if ((points.get(i).y() > point.y()) != (points.get(j).y() > point.y()) &&
                    (point.x() < (points.get(j).x() - points.get(i).x()) * (point.y() - points.get(i).y()) / (points.get(j).y()-points.get(i).y()) + points.get(i).x())) {
                result = !result;
            }
        }
        return result;
    }
}

