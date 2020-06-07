package RPCChat;

/**
 * Interface for peer-to-peer chat communication entry
 */
public interface Messager {
    void sendMsg(String msg);
    void logout();
    void start();
}
