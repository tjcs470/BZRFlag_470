package ServerResponse;

import main.Vector;
import math.geom2d.Point2D;

/**
 * Created with IntelliJ IDEA.
 * User: larsojor
 * Date: 11/9/13
 * Time: 3:48 PM
 */
public class NavigatorTank extends MyTank {

    public double worldDimensionLength;
    public double maxXAndY;

    public NavigatorTank(int index,
                         String callSign,
                         TeamColor color,
                         TankStatus status,
                         int mShotsLeft,
                         double timeToReload,
                         TeamColor flagColor,
                         Vector pos,
                         double angle,
                         Vector vel,
                         double angVel,
                         double worldDimensionLength) {
        super(index, callSign, color, status, mShotsLeft, timeToReload, flagColor, pos, angle, vel, angVel);
        this.worldDimensionLength = worldDimensionLength;
        this.maxXAndY = worldDimensionLength/2;
    }


    public Point2D getDesiredLocation(int goalNum) {
        int numberOfTanks = 10;
        double xLocation = maxXAndY - (worldDimensionLength/numberOfTanks * getIndex() + 40);
        double yLocation = maxXAndY - (worldDimensionLength/numberOfTanks * getIndex() + 40);
        switch(goalNum) {
            case 0:
            case 5:
                return new Point2D(xLocation, maxXAndY);
            case 1:
            case 4:
                return new Point2D(xLocation, -maxXAndY);
            case 2:
            case 7:
                return new Point2D(maxXAndY, yLocation);
            case 3:
            case 6:
                return new Point2D(-maxXAndY, yLocation);
        }
        throw new IllegalArgumentException("Something crazy happened. Goalnum: " + goalNum);
    }

    public boolean hasReachedGoal(int goalNum) {
        double dist = getDesiredLocation(goalNum).distance(getPos());
        System.out.println("Tank[" + getIndex() + "] dist: " + dist);
        return dist < 20;
    }

}
