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
import org.glassfish.tyrus.server.Server;

import javax.websocket.Session;
import java.awt.event.MouseEvent;
import java.io.IOException;

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
