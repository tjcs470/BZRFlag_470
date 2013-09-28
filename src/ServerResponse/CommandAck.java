package ServerResponse;

/**
 * Created with IntelliJ IDEA.
 * User: ty
 * Date: 9/27/13
 * Time: 8:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class CommandAck {
    /**Time stamp of the response*/
    private double mTimeStamp;
    /**Original command that was sent*/
    private String mOrigCmd;

    /**
     * Constructs a new acknowledgment command
     */
    public CommandAck(double timeStamp, String origCmd)
    {
        mTimeStamp = timeStamp;
        mOrigCmd = origCmd;
    }

    /**
     * Getter for the time stamp
     * @return
     */
    double getTimeStamp() {
        return mTimeStamp;
    }

    /**
     * Getter for the original command
     * @return
     */
    String getOrigCmd() {
        return mOrigCmd;
    }
}