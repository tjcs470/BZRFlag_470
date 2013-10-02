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
    protected double getDistanceToPotentialField(Point2D location) {
        return polygon.distance(location);
    }

    @Override
    protected double getAngleToPotentialField(Point2D location) {
        Point2D center = polygon.centroid();
        return Math.atan2(center.getY() - location.getY(), center.getX() - location.getX());
    }

    @Override
    protected boolean isPointOnPotentialField(Point2D point) {
        int i;
        int j;
        boolean result = false;
        for (i = 0, j = points.size() - 1; i < points.size(); j = i++) {
            if ((points.get(i).getY() > point.getY()) != (points.get(j).getY() > point.getY()) &&
                    (point.getX() < (points.get(j).getX() - points.get(i).getX()) * (point.getY() - points.get(i).getY()) / (points.get(j).getY()-points.get(i).getY()) + points.get(i).getX())) {
                result = !result;
            }
        }
        return result;
    }
}

