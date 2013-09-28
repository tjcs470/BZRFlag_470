package ServerResponse;

/**
 * Created with IntelliJ IDEA.
 * User: ty
 * Date: 9/27/13
 * Time: 8:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class BoolResponse
{
    /**Was it a success or not*/
    boolean mSuccess;
    /**Extra description*/
    String mDescrip;

    /**
     * Constructor
     */
    public BoolResponse(boolean success, String descrip)
    {
        mSuccess = success;
        mDescrip = descrip;
    }

    /**
     * Getter for success
     */
    boolean getSuccess() {
        return mSuccess;
    }

    /**
     * Getter for description
     */
    String getDescrip() {
        return mDescrip;
    }
}
