package sample;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Slider;
import javafx.scene.input.DragEvent;
import javafx.scene.input.ScrollEvent;

public class SettingsController {

    public Slider sliderSize;
    public void initialize(){
        sliderSize.valueProperty().addListener((observable, oldValue, newValue) ->
                //System.out.println("sliderNameLabel: " + newValue));
                System.out.println("Listener 1"));
    }

}
