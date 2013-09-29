package ServerResponse;

/**
 * Created with IntelliJ IDEA.
 * User: ty
 * Date: 9/28/13
 * Time: 10:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class GnuplotPrinter {
    public static String getObstaclePlotCmds(Obstacle obstacle) {
        StringBuilder plotCommands = new StringBuilder("");

        plotCommands.append(String.format("set arrow from %f, %f to %f, %f nohead lt 3\n",
                obstacle.getP0().mX, obstacle.getP0().mY, obstacle.getP1().mX, obstacle.getP1().mY));
        plotCommands.append(String.format("set arrow from %f, %f to %f, %f nohead lt 3\n",
                obstacle.getP1().mX, obstacle.getP1().mY, obstacle.getP2().mX, obstacle.getP2().mY));
        plotCommands.append(String.format("set arrow from %f, %f to %f, %f nohead lt 3\n",
                obstacle.getP2().mX, obstacle.getP2().mY, obstacle.getP3().mX, obstacle.getP3().mY));
        plotCommands.append(String.format("set arrow from %f, %f to %f, %f nohead lt 3\n",
                obstacle.getP3().mX, obstacle.getP3().mY, obstacle.getP0().mX, obstacle.getP0().mY));

        return plotCommands.toString();
    }
}
