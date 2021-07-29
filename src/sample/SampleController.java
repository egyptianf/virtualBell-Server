package sample;

import com.dustinredmond.fxtrayicon.FXTrayIcon;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.glassfish.tyrus.server.Server;

import javax.websocket.Session;
import java.io.IOException;

public class SampleController {
    public Label clicked;
    Server server;
    Stage window;

    public SampleController(){
        server = new Server("localhost", 8025, "/folder", ChatServerEndpoint.class);
        try {
            server.start();
            System.out.println("--- server is running");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        //window = (Stage) connectedNum.getScene().getWindow();
        //window.setOnCloseRequest(e -> closeProgram());
    }
    public void call(ActionEvent actionEvent) {
        // Will send a text the connected clients
        try {
            for (Session sess : ChatServerEndpoint.mySession.getOpenSessions()) {
                try {
                    sess.getBasicRemote().sendText("CALL");
                } catch (IOException e) {
                    System.out.println("SOME ERROR HAPPENED DURING THE CONNECTION!");
                }
            }
        }catch (NullPointerException ne){
            System.out.println("NO CONNECTIONS YET!");

        }catch (Exception e){
            System.out.println("Some error occurred or may be no connections at the moment.");
        }



    }





    public void closeProgram(Stage window, FXTrayIcon icon){
        System.out.println("--- Server closed.");
        server.stop();
        window.close();
        icon.hide();
    }
    public void minimize(Stage window){
        System.out.println("Minimized!");
        window.close();
    }
}
