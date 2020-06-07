package RPCChat.client;

import RPCChat.Messager;
import RPCChat.MsgHandler.MsgHandler;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;

/**
 * A simple RPCChat.client that requests/sends a message, time and a username from the RPCChat.server.
 */
public class RPCClient implements Messager {
    private MsgHandler handler;
    private String username;
    private final userGrpc.userBlockingStub blockingStub;
    private ManagedChannel channel;
    private final DateFormat formatter = new SimpleDateFormat("HH:mm:ss dd zzz yyyy", Locale.ENGLISH);

    /** Constructs RPCChat.client for accessing RPC RPCChat.server using a communication channel
     * (to the RPCChat.server), known as a Channel(they are thread-safe and reusable). It is common to create channels
     * at the beginning of application and reuse them until the application shuts down.
     */
    public RPCClient(String ip, int port, String username, MsgHandler handler) {
        this.handler = handler;
        this.username = username;
        String target = ip + ":" + port;
        channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();
        blockingStub = userGrpc.newBlockingStub(channel);
    }

    /**
     * Sends message to the RPCChat.server.
     * @param msg - message to be sent.
     */
    @Override
    public void sendMsg(String msg) {
        Chat.ChatMessage request = Chat.ChatMessage.newBuilder()
                .setUser(username)
                .setTime(formatter.format(new Date()))
                .setText(msg)
                .build();
        blockingStub.send(request);
    }

    /**
     * Receives messages from RPC RPCChat.server.
     */
    public void listenMsgs(){
        Iterator<Chat.ChatMessage> iterator = blockingStub.connect(Chat.Empty.getDefaultInstance());
        iterator.forEachRemaining(this::accept);
        throw new RuntimeException("Smth wrong with listenMsgs iterator");
    }

    /**
     * Closes RPCChat.client.
     */
    @Override
    public void logout() {
        channel.shutdownNow();
    }

    /**
     * Starts the RPCChat.client.
     */
    @Override
    public void start() {
        new Thread(this::listenMsgs).start();
    }


    /**
     * Parses message to preferred format: text, user, date.
     * @param msg - message to parse
     */
    private void accept(Chat.ChatMessage msg) {
        if (Objects.equals(msg.getUser(), username)) return;
        try {
            handler.handle(
                    msg.getText(),
                    formatter.parse(msg.getTime()),
                    msg.getUser()
            );
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}