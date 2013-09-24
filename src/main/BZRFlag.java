package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: ty
 * Date: 9/21/13
 * Time: 7:15 PM
 * To change this template use File | Settings | File Templates.
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
    private String readReply() throws IOException {
        String serverResponse = mIn.readLine();

        if(mDebug)
            System.out.println(serverResponse);

        return serverResponse;
    }

    /**
     * Sends the shoot to the indexed bot
     * @param args
     * @throws IOException
     */
    private void shoot(int botId) {
        Integer.toString(botId);
    }

    public static void main(String args[]) throws IOException {
        BZRFlag agent = new BZRFlag("localhost", 43870);
        agent.sendLine("agent 1");
        agent.readReply();
    }
}
