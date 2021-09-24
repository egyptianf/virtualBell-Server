package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.input.DragEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Stage;

public class SettingsController {

    public Slider sliderSize;
    public ColorPicker colorPicker;
    public Slider sliderOpacity;
    public Button btnOk;
    public void initialize(){
        btnOk.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage settingsStage = (Stage) btnOk.getScene().getWindow();
                settingsStage.close();
            }
        });
    }

}
