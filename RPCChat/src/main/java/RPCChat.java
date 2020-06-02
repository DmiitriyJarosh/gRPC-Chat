import java.util.Scanner;

public class RPCChat {

    String ip, username;
    int port;

    public RPCChat(String username, String ip, int port) {
        this.ip = ip;
        this.username = username;
        this.port = port;
    }

    public void run() {
        MsgHandler handler = new ConsoleMsgHandler();
        Messager messager;
        if (ip.equals("")) {
            messager = new RPCServer(port, handler);
        } else {
            messager = new RPCClient(ip, port, handler);
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
