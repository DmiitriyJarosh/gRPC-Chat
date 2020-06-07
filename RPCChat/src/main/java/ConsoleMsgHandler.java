import java.util.Date;

/**
 * Handler for printing messages to console
 */
public class ConsoleMsgHandler implements MsgHandler {

    @Override
    public void handle(String msg, Date time, String username) {
        System.out.println(time + " " + username + ": " + msg);
    }
}
