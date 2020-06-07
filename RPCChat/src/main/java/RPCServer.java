import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple server that gets/sends a message, time and a username to the client.
 */
public class RPCServer implements Messager {
    private int port;
    private MsgHandler handler;
    private String username;
    private DateFormat formatter = new SimpleDateFormat("HH:mm:ss dd zzz yyyy", Locale.ENGLISH);

    private StreamObserver<Chat.ChatMessage> observer = null;

    private Server server;

    public RPCServer(int port, String username, MsgHandler handler) {
        this.port = port;
        this.username = username;
        this.handler = handler;
    }

    /**
     * Sends messages to client.
     * @param msg - message to be sent.
     */
    @Override
    public void sendMsg(String msg) {
        if (observer != null) {
            observer.onNext(Chat.ChatMessage.newBuilder()
                    .setUser(username)
                    .setText(msg)
                    .setTime(formatter.format(new Date()))
                    .build());
        }
    }

    /**
     * Closes server.
     */
    @Override
    public void logout() {
        server.shutdown();
    }

    /**
     * Starts server.
     */
    @Override
    public void start() {
        try {
            server = ServerBuilder.forPort(port)
                    .addService(new UserImpl())
                    .build()
                    .start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Class, which implements sending messages to client and receiving messages.
     */
    class UserImpl extends userGrpc.userImplBase {

        /**
         * Sends message to the client.
         * @param request - message to be sent.
         * @param responseObserver - notifications from an observable stream of messages.
         */
        @Override
        public void send(Chat.ChatMessage request, StreamObserver<Chat.Empty> responseObserver) {
            try {
                handler.handle(request.getText(), formatter.parse(request.getTime()), request.getUser());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Chat.Empty reply = Chat.Empty.newBuilder().build();
            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        }


        /**
         * Connects to the client to receive messages.
         * @param request - empty message.
         * @param responseObserver - notifications from an observable stream of messages.
         */
        @Override
        public void connect(Chat.Empty request, StreamObserver<Chat.ChatMessage> responseObserver) {
            synchronized (this) {
                observer = responseObserver;
            }
        }
    }
}