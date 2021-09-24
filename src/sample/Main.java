package sample;

import com.dustinredmond.fxtrayicon.FXTrayIcon;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
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

import java.awt.*;
import java.util.Objects;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        SampleController myController = loader.getController();

        FXMLLoader settingsLoader = new FXMLLoader(getClass().getResource("settings.fxml"));
        Parent settingsRoot = settingsLoader.load();
        SettingsController settingsController = settingsLoader.getController();
        Scene settingsScene = new Scene(settingsRoot, 250, 250);
        settingsScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("main.css")).toExternalForm());
        Stage settingsStage = new Stage();
        settingsStage.setTitle("Button Settings");
        settingsStage.setScene(settingsScene);




        primaryStage.setTitle("Virtual Bell");
        primaryStage.setAlwaysOnTop(true);
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        FXTrayIcon fxTrayIcon = new FXTrayIcon(primaryStage, getClass().getResource("bell-icon.png"));
        fxTrayIcon.show();

        primaryStage.setOnCloseRequest(e -> myController.minimize(primaryStage));
        //Settings Item
        MenuItem settingsItem = new MenuItem("Settings");
        settingsItem.setOnAction(e -> {
            settingsStage.show();
        });
        fxTrayIcon.addMenuItem(settingsItem);
        //Exit Item
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
        myController.callButton.setOnMousePressed(pressEvent -> {
            double beforeX = primaryStage.getX(), beforeY = primaryStage.getY();
            myController.callButton.setOnMouseDragged(dragEvent -> {
                primaryStage.setX(dragEvent.getScreenX() - pressEvent.getSceneX());
                primaryStage.setY(dragEvent.getScreenY() - pressEvent.getSceneY());
            });
        });
        primaryStage.xProperty().addListener((obs, oldVal, newVal) -> {
            myController.locationChanged = true;
        });
        primaryStage.yProperty().addListener((obs, oldVal, newVal) -> {
            myController.locationChanged = true;
        });
        // Slider in settingsScene
        // add an event when its value changes
        settingsController.sliderSize.valueProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Slider has changed and we are in main function");
            myController.callButton.setScaleX( (newValue.doubleValue()/100) );
            myController.callButton.setScaleY( (newValue.doubleValue()/100) );
        });
    }


    public static void main(String[] args) {
        launch(args);
    }

}
