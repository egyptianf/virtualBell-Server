package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class SettingsController {

    public Slider sliderSize;
    public ColorPicker colorPicker;
    public Slider sliderOpacity;

    public Button btnClose;
    public void handleCloseButtonAction(ActionEvent event) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    public Button btnSize;
    public Button btnColor;
    public Button btnOpacity;

    @FXML
    public AnchorPane rootPane;
    public AnchorPane pnColor;
    public AnchorPane pnOpacity;
    public AnchorPane paneMaster;

    @FXML
    public void min(MouseEvent event){
        Stage S = (Stage) ((Node)event.getSource()).getScene().getWindow();
        S.setIconified(true);
    }







    public void initialize(){
        pnColor.setVisible(false);
        pnOpacity.setVisible(false);
        btnSize.setStyle("-fx-background-color:" + "#FF2626");
        paneMaster.setOnMousePressed(pressEvent -> {
            Stage settingsStage = (Stage) paneMaster.getScene().getWindow();
            paneMaster.setOnMouseDragged(dragEvent -> {
                settingsStage.setX(dragEvent.getScreenX() - pressEvent.getSceneX());
                settingsStage.setY(dragEvent.getScreenY() - pressEvent.getSceneY());
            });
        });

    }


}
