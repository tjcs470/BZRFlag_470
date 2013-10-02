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
                obstacle.getP0().x(), obstacle.getP0().x(), obstacle.getP1().x(), obstacle.getP1().x()));
        plotCommands.append(String.format("set arrow from %f, %f to %f, %f nohead lt 3\n",
                obstacle.getP1().x(), obstacle.getP1().x(), obstacle.getP2().x(), obstacle.getP2().x()));
        plotCommands.append(String.format("set arrow from %f, %f to %f, %f nohead lt 3\n",
                obstacle.getP2().x(), obstacle.getP2().x(), obstacle.getP3().x(), obstacle.getP3().x()));
        plotCommands.append(String.format("set arrow from %f, %f to %f, %f nohead lt 3\n",
                obstacle.getP3().x(), obstacle.getP3().x(), obstacle.getP0().x(), obstacle.getP0().x()));

        return plotCommands.toString();
    }
}
