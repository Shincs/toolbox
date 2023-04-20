package dev.luzifer.ui.overlays;

import dev.luzifer.settings.Settings;
import dev.luzifer.ui.util.CSSUtil;
import dev.luzifer.ui.util.FXMLUtil;
import dev.luzifer.ui.util.ImageUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MainOverlay extends StackPane implements Initializable {
    
    private final Stage stage;
    
    @FXML
    private Circle controlButton;
    
    @FXML
    private AnchorPane contentHolder;
    
    public MainOverlay(Stage stage) {
        this.stage = stage;
        FXMLUtil.loadFXML(this);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    
        Platform.runLater(() -> {
            CSSUtil.applyStyle(getScene());
            controlButton.setOpacity(Settings.settings.getOpacity());
            
            moveWindowOnDrag();
            
            // increase opacity on ctrl and +
            getScene().setOnKeyPressed(keyEvent -> {
                if(keyEvent.isControlDown() && keyEvent.getCode().getName().equals("Plus")) {
                    stage.setOpacity(Math.min(1.0, stage.getOpacity() + 0.01));
                } else if(keyEvent.isControlDown() && keyEvent.getCode().getName().equals("Minus")) {
                    stage.setOpacity(Math.max(0.01, stage.getOpacity() - 0.01));
                }
            });
        });
        
        setContent(new ChatGPTOverlay(() -> setContent(new BrowserOverlay())));
        
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
        
        
        MenuItem browser = new MenuItem("Browser");
        browser.setOnAction(event -> setContent(new BrowserOverlay()));
        
        MenuItem chat = new MenuItem("Chat");
        chat.setOnAction(event -> setContent(new ChatGPTOverlay(() -> setContent(new BrowserOverlay()))));
        
        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(event -> System.exit(0));
        
        contextMenu.getItems().addAll(browser, chat, exit);
        contextMenu.show(controlButton, mouseEvent.getScreenX(), mouseEvent.getScreenY());
    }
    
    private void moveWindowOnDrag() {
        getScene().setOnMouseDragged(mouseEvent -> {
            if(mouseEvent.isPrimaryButtonDown()) {
                getScene().getWindow().setX(mouseEvent.getScreenX() - getScene().getWindow().getWidth() / 2);
                getScene().getWindow().setY(mouseEvent.getScreenY() - getScene().getWindow().getHeight() / 2);
            }
        });
    }
}
