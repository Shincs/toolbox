package dev.luzifer.ui;

import javafx.concurrent.Worker;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class ChromeBite extends BorderPane {
    
    private final WebEngine webEngine;
    
    public ChromeBite() {
        WebView webView = new WebView();
        webEngine = webView.getEngine();
        webView.setPrefHeight(500);
        webView.setPrefWidth(500);
        
        TextField searchField = new TextField();
        searchField.setOnAction(event -> validateURL(searchField.getText()));
        
        setTop(searchField);
        setCenter(webView);
        
        webView.prefWidthProperty().bind(widthProperty());
        webView.prefHeightProperty().bind(heightProperty());
        
        loadURL("https://www.google.com/");
    }
    
    private void loadURL(String url) {
        webEngine.load(url);
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                String currentURL = webEngine.getLocation();
                
                TextField searchField = (TextField) getTop();
                searchField.setText(currentURL);
            }
        });
    }
    
    private void validateURL(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            loadURL("https://" + url);
        else
            loadURL(url);
    }
}
