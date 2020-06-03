import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;

public class RPCClient implements Messager {
    private MsgHandler handler;
    private String username;
    private final userGrpc.userBlockingStub blockingStub;
    private ManagedChannel channel;
    private final DateFormat formatter = new SimpleDateFormat("HH:mm:ss dd zzz yyyy", Locale.ENGLISH);

    public RPCClient(String ip, int port, String username, MsgHandler handler) {
        this.handler = handler;
        this.username = username;
        String target = ip + ":" + port;
        channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();
        blockingStub = userGrpc.newBlockingStub(channel);
    }

    @Override
    public void sendMsg(String msg) {
        Chat.ChatMessage request = Chat.ChatMessage.newBuilder()
                .setUser(username)
                .setTime(formatter.format(new Date()))
                .setText(msg)
                .build();
        blockingStub.send(request);
    }

    public void listenMsgs(){
        Iterator<Chat.ChatMessage> iterator = blockingStub.connect(Chat.Empty.getDefaultInstance());
        iterator.forEachRemaining(this::accept);
        throw new RuntimeException("Smth wrong with listenMsgs iterator");
    }

    @Override
    public void logout() {
        channel.shutdownNow();
    }

    @Override
    public void start() {
        new Thread(this::listenMsgs).start();
    }

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