package RPCChat.MsgHandler;

import java.util.Date;

/**
 * Interface for message handling - way to do smth with received message
 * as an example - print to console
 */
public interface MsgHandler {
    void handle(String msg, Date time, String username);
}
