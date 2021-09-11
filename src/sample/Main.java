package sample;

import com.dustinredmond.fxtrayicon.FXTrayIcon;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.StageStyle;
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
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        FXTrayIcon fxTrayIcon = new FXTrayIcon(primaryStage, getClass().getResource("bell-icon.png"));
        fxTrayIcon.show();

        primaryStage.setOnCloseRequest(e -> myController.minimize(primaryStage));
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(e -> myController.closeProgram(primaryStage, fxTrayIcon));
        fxTrayIcon.addMenuItem(exitItem);
        Scene scene = new Scene(root, 220, 220);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("main.css")).toExternalForm());
        scene.setFill(Color.TRANSPARENT);

        primaryStage.setScene(scene);
        primaryStage.show();
        //Changing the location of the main window
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        double x = bounds.getMinX() + (bounds.getWidth() - scene.getWidth()) * 1;
        double y = bounds.getMinY() + (bounds.getHeight() - scene.getHeight()) * 0.1;
        primaryStage.setX(x);
        primaryStage.setY(y);
    }


    public static void main(String[] args) {
        launch(args);
    }

}
