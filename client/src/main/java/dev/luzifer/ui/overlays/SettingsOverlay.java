package dev.luzifer.ui.overlays;

import dev.luzifer.settings.Settings;
import dev.luzifer.ui.component.SliderLabelComponent;
import dev.luzifer.ui.util.FXMLUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsOverlay extends StackPane implements Initializable {
    
    @FXML
    private FlowPane settingsFlowPane;
    
    @FXML
    private Button saveButton;
    
    public SettingsOverlay() {
        FXMLUtil.loadFXML(this);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        
        SliderLabelComponent frameOpacitySlider =
                new SliderLabelComponent("Frame Opacity", 0.01, 1, Settings.settings.getOpacity());
        
        saveButton.setOnAction(event -> {
            
            Settings.settings = Settings.of(frameOpacitySlider.getSlider().getValue());
            Settings.save();
        });
        
        settingsFlowPane.getChildren().addAll(frameOpacitySlider);
    }
}
