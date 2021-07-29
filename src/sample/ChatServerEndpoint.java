package sample;

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


    @OnOpen
    public void onOpen(Session session) {
        System.out.println ("Connected, sessionID = " + session.getId());
        connectedNum++;
        mySession = session;
        System.out.println("NEW CONNECTION");

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
