package dev.luzifer.ui.overlays;

import dev.luzifer.settings.Settings;
import dev.luzifer.ui.component.CheckBoxLabelComponent;
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
        
        SliderLabelComponent imageSwitchSlider =
                new SliderLabelComponent("Picture Switch", 1, 60, Settings.settings.getImageSwitchInterval());
        SliderLabelComponent frameOpacitySlider =
                new SliderLabelComponent("Frame Opacity", 0.01, 1, Settings.settings.getOpacity());
        CheckBoxLabelComponent switchImages =
                new CheckBoxLabelComponent("Auto-Switch Images", Settings.settings.isSwitchImages());
        
        saveButton.setOnAction(event -> {
            
            Settings.settings = Settings.of(
                    imageSwitchSlider.getSlider().getValue(),
                    frameOpacitySlider.getSlider().getValue() > 0.99 ? 1 : (int) frameOpacitySlider.getSlider().getValue(),
                    switchImages.getCheckBox().isSelected()
            );
            Settings.save();
        });
        
        settingsFlowPane.getChildren().addAll(imageSwitchSlider, frameOpacitySlider, switchImages);
    }
}
