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
import java.util.LinkedList;
import java.util.Queue;

@ServerEndpoint(value = "/app")
public class ChatServerEndpoint {

    public static Queue<Session> openSessions = new LinkedList<>();
    public static SimpleStringProperty connectedNumProperty = new SimpleStringProperty("0"+"\nCONNECTED");


    @OnOpen
    public void onOpen(Session session) {
        System.out.println ("Connected, sessionID = " + session.getId());
        openSessions.add(session);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                connectedNumProperty.set(String.valueOf(Integer.parseInt(connectedNumProperty.getValue().split("\\n")[0])+1)+"\nCONNECTED");
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

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println("Session " + session.getId() +
                " closed because " + closeReason);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                connectedNumProperty.set(String.valueOf(Integer.parseInt(connectedNumProperty.getValue().split("\\n")[0])-1)+"\nCONNECTED");
            }
        });
        openSessions.remove(session);
        //connectedNumLabel.setText(Integer.toString(ChatServerEndpoint.connectedNum));
    }


}
