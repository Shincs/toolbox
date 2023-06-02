package dev.luzifer.ui;

import javafx.concurrent.Worker;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.util.HashMap;
import java.util.Map;

public class ChromeBite extends BorderPane {
    
    private static final Map<String, String> AUTOCOMPLETE_MAP = new HashMap<>();
    
    static {
        AUTOCOMPLETE_MAP.put("google", "google.com");
        AUTOCOMPLETE_MAP.put("moodle", "moodle.oszimt.de");
        AUTOCOMPLETE_MAP.put("chat", "chat.openai.com");
    }
    
    private final WebEngine webEngine;
    
    public ChromeBite() {
        WebView webView = new WebView();
        webEngine = webView.getEngine();
        webView.setPrefHeight(500);
        webView.setPrefWidth(500);
        
        TextField searchField = new TextField();
        searchField.setStyle("-fx-background-color: #f2f2f2; -fx-background-radius: 5; -fx-border-radius: 5; -fx-border-color: #e6e6e6; -fx-border-width: 1;");
        autocomplete(searchField);
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
    
    private void autocomplete(TextField searchField) {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            String[] parts = newValue.split(" ");
            String lastPart = parts[parts.length - 1];
            
            if (AUTOCOMPLETE_MAP.containsKey(lastPart)) {
                String autocomplete = AUTOCOMPLETE_MAP.get(lastPart);
                searchField.setText(newValue.replace(lastPart, autocomplete));
            }
        });
    }
}
