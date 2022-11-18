package dev.luzifer.ui.overlays;

import dev.luzifer.ui.util.CSSUtil;
import dev.luzifer.ui.util.FXMLUtil;
import dev.luzifer.ui.util.ImageUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

public class MainOverlay extends StackPane implements Initializable {
    
    @FXML
    private Circle controlButton;
    
    @FXML
    private AnchorPane contentHolder;
    
    public MainOverlay() {
        FXMLUtil.loadFXML(this);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    
        Platform.runLater(() -> CSSUtil.applyStyle(getScene()));
        
        setContent(new ImageSwitcherOverlay());
        
        controlButton.setFill(ImageUtil.getImagePattern("inner_icon.gif"));
        controlButton.setOnMouseClicked(this::onControlButtonClicked);
    }
    
    public void setContent(Region content) {
        contentHolder.getChildren().setAll(content);
    }
    
    private void onControlButtonClicked(MouseEvent mouseEvent) {
    
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.focusedProperty()
                .addListener((observableValue, aBoolean, t1) -> {
            if(!t1) contextMenu.hide();
        });
    
        MenuItem imageSwitcher = new MenuItem("Image Switcher");
        imageSwitcher.setOnAction(event -> setContent(new ImageSwitcherOverlay()));
        
        MenuItem browser = new MenuItem("Browser");
        browser.setOnAction(event -> setContent(new BrowserOverlay()));
        
        MenuItem chat = new MenuItem("Chat [WiP]");
        chat.setOnAction(event -> setContent(new ChatOverlay()));
        
        MenuItem settings = new MenuItem("Settings [WiP]");
        settings.setOnAction(event -> setContent(new SettingsOverlay()));
        
        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(event -> System.exit(0));
        
        contextMenu.getItems().addAll(imageSwitcher, browser, chat, settings, exit);
        contextMenu.show(controlButton, mouseEvent.getScreenX(), mouseEvent.getScreenY());
    }
}
