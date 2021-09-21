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

@ServerEndpoint(value = "/app")
public class ChatServerEndpoint {
    public static Session mySession;


    public static int connectedNum = 0;
    public static SimpleStringProperty connectedNumProperty = new SimpleStringProperty("0");


    @OnOpen
    public void onOpen(Session session) {
        System.out.println ("Connected, sessionID = " + session.getId());
        connectedNum++;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                connectedNumProperty.set(String.valueOf(Integer.parseInt(connectedNumProperty.getValue())+1));
            }
        });
        mySession = session;
        System.out.println("NEW CONNECTION");

        //make session have no max idle timeout on a per session basis
        session.setMaxIdleTimeout(-10);

        //connectedNumLabel.setText(Integer.toString(ChatServerEndpoint.connectedNum));
        //SampleController.connectedNum.setText(Integer.toString(ChatServerEndpoint.connectedNum));
    }

    @OnMessage
    public void onMessage(String message, Session session) {
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
                connectedNumProperty.set(String.valueOf(Integer.parseInt(connectedNumProperty.getValue())-1));
            }
        });
        try {
            for (Session sess : session.getOpenSessions()) {
                try {
                    if(sess != session){
                        mySession = sess;
                        break;
                    }
                    sess.getBasicRemote().sendText("CALL");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }catch (NullPointerException ne){
            System.out.println("NO CONNECTIONS YET!");
        }
        connectedNum--;
        //connectedNumLabel.setText(Integer.toString(ChatServerEndpoint.connectedNum));
    }

    @Deprecated
    public void call(ActionEvent actionEvent) {
        System.out.println("CLICKED!!");
        try {
            for (Session sess : mySession.getOpenSessions()) {
                try {
                    sess.getBasicRemote().sendText("CALL");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }catch (NullPointerException ne){
            System.out.println("NO CONNECTIONS YET!");
        }
    }

}
