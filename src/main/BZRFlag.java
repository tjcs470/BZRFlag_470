package main;

import ServerResponse.BoolResponse;
import ServerResponse.ObstaclesQuery;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created with IntelliJ IDEA.
 * User: ty
 * Date: 9/21/13
 * Time: 7:15 PM
 * This is basically the interface a bot has with the game
 */
public class BZRFlag {
    /**Output for messages to BZRflag game*/
    private final PrintWriter mOut;
    /**Input for messages from BZRflag game*/
    private final BufferedReader mIn;
    /**Debug flag*/
    private boolean mDebug;

    /**
     * Constructor
     */
    public BZRFlag(String host, int port) throws IOException {
        Socket socket = new Socket(host, port);
        mIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        mOut = new PrintWriter(socket.getOutputStream(), true);

        mDebug = true;
    }

    /**
     * Sends a message to the BZRflag game server
     * @param msgToServer
     * @return
     */
    private void sendLine(String msgToServer)
    {
        mOut.println(msgToServer);

        if(mDebug)
            System.out.println("Sent command:" + msgToServer);
    }

    /**
     * Reads reply from the BZRflag game
     * @return
     */
    private String readOneReplyLine() throws IOException {
        String serverResponse = mIn.readLine();

        if(mDebug)
            System.out.println(serverResponse);

        return serverResponse;
    }

    /**
     * Reads an acknowledgment message from the server
     * @return If the
     */
    private boolean readAck(String cmdSent) throws IOException
    {
        String ackLine = readOneReplyLine();

        Pattern ackRegex = Pattern.compile("ack ([0-9]+\\.[0-9]+) (" + cmdSent + ")");
        Matcher matcher = ackRegex.matcher(ackLine);

        assert(matcher.matches());
        double timeStamp = Double.parseDouble(matcher.group(1));

        return true;
    }

    /**
     * Reads the boolean line
     */
    private BoolResponse readBool() throws IOException {
        String boolLine = readOneReplyLine();
        Pattern boolRegex = Pattern.compile("(ok|fail)( .*)?");
        Matcher matcher = boolRegex.matcher(boolLine);

        assert(matcher.matches());
        boolean success = matcher.group(1).equals("ok") ? true : false;
        String descrip = matcher.group(2) != null ? matcher.group(2) : "";

        BoolResponse boolResponse = new BoolResponse(success, descrip);
        return boolResponse;
    }

    /**
     * Sends the shoot command to the indexed bot
     * @param botId Index of the bot we are telling to shoot
     * @throws IOException
     * @return Returns false if the command was successful and false otherwise
     */
    public BoolResponse shoot(int botId) throws IOException {
        StringBuilder cmdBuilder = new StringBuilder("shoot ");
        cmdBuilder.append(Integer.toString(botId));
        String shootCmd = cmdBuilder.toString();
        sendLine(shootCmd);
        readAck(shootCmd);
        return readBool();
    }

    /**
     * Sends the the speed command
     * @param args
     * @throws IOException
     */
    public BoolResponse speed(int botId, double speed) throws IOException {
        StringBuilder cmdBuilder = new StringBuilder("speed ");
        cmdBuilder.append(Integer.toString(botId));
        cmdBuilder.append(" " + Double.toString(speed));
        String speedCmd = cmdBuilder.toString();
        sendLine(speedCmd);
        readAck(speedCmd);
        return readBool();
    }

    /**
     * Sends the angular velocity command
     * @param botId
     * @param angVel
     * @return
     * @throws IOException
     */
    public BoolResponse angVel(int botId, double angVel) throws IOException {
        StringBuilder cmdBuilder = new StringBuilder("angvel ");
        cmdBuilder.append(Integer.toString(botId));
        cmdBuilder.append(" " + Double.toString(angVel));
        String angVelCmd = cmdBuilder.toString();
        sendLine(angVelCmd);
        readAck(angVelCmd);
        return readBool();
    }

    /**
     * Queries the obstacles within the world
     * @return
     * @throws IOException
     */
    public ObstaclesQuery getObstacles() throws IOException {
        String queryCmd = "obstacles";
        sendLine(queryCmd);
        readAck(queryCmd);

        ObstaclesQuery obstaclesQuery = new ObstaclesQuery();

        Pattern obstacleLine = Pattern.compile(
                "obstacle (-?[0-9]+\\.[0-9]?) (-?[0-9]+\\.[0-9]?) (-?[0-9]+\\.[0-9]?) (-?[0-9]+\\.[0-9]?) (-?[0-9]+\\.[0-9]?) (-?[0-9]+\\.[0-9]?) (-?[0-9]+\\.[0-9]?) (-?[0-9]+\\.[0-9]?)"
        );
        Matcher matcher = null;
        String arrayLine = readOneReplyLine();
        assert(arrayLine.equals("begin"));
        arrayLine = readOneReplyLine();
        while(!arrayLine.equals("end")) {
            matcher = obstacleLine.matcher(arrayLine);
            assert(matcher.matches());

            Vector p0 = new Vector(Double.parseDouble(matcher.group(1)), Double.parseDouble(matcher.group(2)));
            Vector p1 = new Vector(Double.parseDouble(matcher.group(3)), Double.parseDouble(matcher.group(4)));

            obstaclesQuery.addObstacle(new ObstaclesQuery.Obstacle(p0, p1));

            arrayLine = readOneReplyLine();
        }

        return obstaclesQuery;
    }

    public static void main(String args[]) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        /*System.out.print("Input host:");
        String host = br.readLine();*/

        /*System.out.print("Input port num:");
        String portNumStr = br.readLine();
        int portNum = Integer.parseInt(portNumStr);*/

        //BZRFlag agent = new BZRFlag("localhost", portNum);
        BZRFlag agent = new BZRFlag("localhost", 52738);
        agent.sendLine("agent 1");
        agent.readOneReplyLine();

        agent.shoot(1);
        agent.speed(1, -1.0);
        agent.angVel(1, 1.0);
        agent.getObstacles();

        /*Pattern p = Pattern.compile("ack ([0-9]+\\.[0-9]+) angvel 0 1.0");
        Matcher m = p.matcher("ack 148.01157093 angvel 0 1.0");
        boolean b = m.matches();
        String thing = m.group(1);*/
    }
}
