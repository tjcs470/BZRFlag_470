package potentialFields;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import main.Vector;
import math.geom2d.Point2D;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: larsojor
 * Date: 10/1/13
 */
public abstract class PotentialField {

    public abstract Vector getVectorForce(Point2D location);
    public abstract boolean isPointOnPotentialField(Point2D location);
    public abstract double getDistanceToPotentialField(Point2D location);
    public abstract double getAngleToPotentialField(Point2D location);

    public static Vector emptyVector() {
        return new Vector(0.0, 0.0);
    }

    public static double getNetAngle(Point2D location, List<PotentialField> fields) {
        return getNetVector(location, fields).getAngle();
    }

    public static Vector getNetVector(Point2D location, List<PotentialField> fields) {
        assert location != null;
        List<Vector> vectors = new ArrayList<Vector>();
        for(PotentialField field : fields) {
            if(field == null) continue;
            vectors.add(field.getVectorForce(location));
        }
        return sumVectors(vectors);
    }

    public static Vector sumVectors(List<Vector> vectors) {
        double x = 0;
        double y = 0;

        for(Vector v : vectors) {
            x += v.x();
            y += v.y();
        }

        return new Vector(x,y);
    }
}
