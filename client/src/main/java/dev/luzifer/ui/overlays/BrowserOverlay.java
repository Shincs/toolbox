package dev.luzifer.ui.overlays;

import dev.luzifer.ui.util.FXMLUtil;
import dev.luzifer.ui.util.ImageUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

public class BrowserOverlay extends VBox implements Initializable {
    
    private final WebView webView = new WebView();
    
    @FXML
    private Rectangle googleLogo;
    
    public BrowserOverlay() {
        FXMLUtil.loadFXML(this);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    
        googleLogo.setFill(ImageUtil.getRawImagePattern("google_logo.png"));
        googleLogo.setOnMouseClicked(mouseEvent -> {
            WebEngine webEngine = webView.getEngine();
            webEngine.load("https://google.com");
        });
        
        WebEngine webEngine = webView.getEngine();
        webEngine.load("https://google.com");
        
        getChildren().add(webView);
    }
}
