package sample;

import com.dustinredmond.fxtrayicon.FXTrayIcon;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import org.glassfish.tyrus.server.Server;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.glassfish.tyrus.server.Server;

import java.util.Objects;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        SampleController myController = loader.getController();
        primaryStage.setTitle("Virtual Bell");
        primaryStage.setAlwaysOnTop(true);


        FXTrayIcon fxTrayIcon = new FXTrayIcon(primaryStage, getClass().getResource("bell-icon.png"));
        fxTrayIcon.show();

        primaryStage.setOnCloseRequest(e -> myController.minimize(primaryStage));
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(e -> myController.closeProgram(primaryStage, fxTrayIcon));
        fxTrayIcon.addMenuItem(exitItem);

        primaryStage.setScene(new Scene(root, 170, 110));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
