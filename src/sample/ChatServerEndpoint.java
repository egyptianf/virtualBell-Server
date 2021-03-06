package sample;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.glassfish.tyrus.server.Server;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@ServerEndpoint(value = "/app")
public class ChatServerEndpoint {

    public static BlockingQueue<Session> openSessions = new LinkedBlockingQueue<>();
    public static SimpleStringProperty connectedNumProperty = new SimpleStringProperty("0"+"\nCONNECTED");


    @OnOpen
    public void onOpen(Session session) {
        System.out.println ("Connected, sessionID = " + session.getId());
        System.out.println (session.getId().length());

        openSessions.add(session);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                connectedNumProperty.set(openSessions.size() +"\nCONNECTED");
            }
        });
        System.out.println("NEW CONNECTION");

        //make session have no max idle timeout on a per session basis
        session.setMaxIdleTimeout(-10);

    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // Not doing any logic because the server only sends messages; it does not receive
        if (message.equals("quit")) {
            try {
                session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Bye!"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @OnMessage
    public void healthCheckCallback(PongMessage pong, Session session) throws IOException {
        System.out.println("Received PONG from client, thus this session is valid");
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println("Session " + session.getId() +
                " closed because " + closeReason);
        openSessions.remove(session);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                connectedNumProperty.set(openSessions.size() +"\nCONNECTED");
            }
        });
    }

    @OnError
    public void onError(Session session, Throwable throwable) throws IOException {
        throwable.printStackTrace();
        System.out.println("Some error occurred");
        session.close();
    }


}
