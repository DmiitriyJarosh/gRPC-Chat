import java.util.Date;

public interface MsgHandler {
    void handle(String msg, Date time, String username);
}
