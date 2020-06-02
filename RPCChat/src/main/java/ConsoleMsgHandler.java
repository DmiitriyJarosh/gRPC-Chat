import java.util.Date;

public class ConsoleMsgHandler implements MsgHandler {

    @Override
    public void handle(String msg, Date time, String username) {
        System.out.println(time + " " + username + ": " + msg);
    }
}
