package sample;

import com.dustinredmond.fxtrayicon.FXTrayIcon;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.glassfish.tyrus.core.RemoteEndpointWrapper;
import org.glassfish.tyrus.server.Server;

import javax.websocket.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

public class SampleController {
    public Label clicked;
    public Button callButton;
    public boolean locationChanged=false;
    Server server;
    Stage window;

    public SampleController(){
        server = new Server("localhost", 8025, "/folder", ChatServerEndpoint.class);
        try {
            server.start();
            //server.getServerContainer().setDefaultMaxSessionIdleTimeout(-10);
            System.out.println("--- server is running");
        } catch (Exception e) {
            e.printStackTrace();
        }


        int SECS = 10;
        Timer heartbeat = new Timer();
        heartbeat.schedule(new TimerTask() {
            @Override
            public void run() {
                for (Session s: ChatServerEndpoint.openSessions){
                    if (s.isOpen()){
                        RemoteEndpoint.Async asyncHandle = s.getAsyncRemote();
                        asyncHandle.setSendTimeout(1000);
                        asyncHandle.sendText("check-health", new SendHandler() {
                            @Override
                            public void onResult(SendResult sendResult) {
                                if(!sendResult.isOK()){
                                    System.out.println("Async send failure: " + sendResult.getException());
                                    ChatServerEndpoint.openSessions.remove(s);
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            ChatServerEndpoint.connectedNumProperty.set(ChatServerEndpoint.openSessions.size() +"\nCONNECTED");
                                        }
                                    });
                                }
                            }
                        });//will timeout after 2 seconds
                    }

                }

            }

        }, 0, 1000 * SECS );


    }

    public void initialize() {
        callButton.textProperty().bind(ChatServerEndpoint.connectedNumProperty);
        //window = (Stage) connectedNum.getScene().getWindow();
        //window.setOnCloseRequest(e -> closeProgram());

    }

    //Callback function when the button is clicked
    public void call(ActionEvent event) {
        // Will send a text the connected clients
        System.out.println(locationChanged);

        try {
            if(!locationChanged) {
                for (Session sess : ChatServerEndpoint.openSessions) {
                    try {
                        sess.getBasicRemote().sendText("CALL");
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("SOME ERROR HAPPENED DURING THE CONNECTION!");
                    }
                }
            }
        } catch (NullPointerException ne){
            System.out.println("NO CONNECTIONS YET!");
        }catch (Exception e){
            System.out.println("Some error occurred or may be no connections at the moment.");
        }
        locationChanged = false;



    }





    public void closeProgram(Stage window, FXTrayIcon icon){
        System.out.println("--- Server closed.");
        server.stop();
        window.close();
        icon.hide();
        Platform.exit();
    }
    public void minimize(Stage window){
        System.out.println("Minimized!");
        window.close();
    }
}
