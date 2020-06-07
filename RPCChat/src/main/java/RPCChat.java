import java.util.Scanner;

/**
 * Implementation of chat with gRPC framework
 */
public class RPCChat {

    String ip, username;
    int port;

    /**
     * Creates chat with specified parameters
     * @param username name of user
     * @param ip ip for connection, if provided than client mode is activated
     * @param port port for connection
     */
    public RPCChat(String username, String ip, int port) {
        this.ip = ip;
        this.username = username;
        this.port = port;
    }

    /**
     * Runs chat and waits for new lines from user to send
     */
    public void run() {
        MsgHandler handler = new ConsoleMsgHandler();
        Messager messager;
        if (ip.equals("")) {
            System.out.println("Server started!");
            messager = new RPCServer(port, username, handler);
        } else {
            System.out.println("Client started");
            messager = new RPCClient(ip, port, username, handler);
        }

        messager.start();

        Scanner sc = new Scanner(System.in);
        while (true) {
            String msg = sc.nextLine();
            if (msg.equals("\\quit")) {
                messager.logout();
                break;
            } else {
                messager.sendMsg(msg);
            }
        }
    }

    public static void main(String[] args) {
        boolean isServer = true;
        if (args.length != 1 && args.length != 2) {
            System.out.println("Usage: RPCChat.jar <port> only for client: <ip>");
        }
        // <port> only for client: <ip>
        if (args.length > 1) {
            isServer = false;
        }
        System.out.println("Enter username:");
        Scanner sc = new Scanner(System.in);

        String username = sc.nextLine();
        int port = 0;

        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        String ip = isServer ? "" : args[1];

        new RPCChat(username, ip, port).run();
    }
}
