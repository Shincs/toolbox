package dev.luzifer.ui.overlays;

import dev.luzifer.Main;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatGPTOverlay extends VBox {
    
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final Pattern PATTERN = Pattern.compile("\"content\":\\s*\"([^\"]*)\"");
    
    private final OkHttpClient httpClient = new OkHttpClient();
    
    public ChatGPTOverlay(Runnable callback) {
        super();
        
        setWidth(600);
        setHeight(400);
        
        // Create TextArea for displaying conversation history
        TextArea conversationArea = new TextArea();
        conversationArea.setEditable(false);
        conversationArea.setPrefHeight(300);
        conversationArea.setWrapText(true);
        conversationArea.setFocusTraversable(false);
        conversationArea.setStyle("-fx-border-color: transparent; -fx-background-color: transparent; -fx-focus-color: transparent;");
        
        // Create TextField for user input
        TextField inputField = new TextField();
        inputField.setFocusTraversable(false);
        inputField.setStyle("-fx-border-color: transparent; -fx-focus-color: transparent;");
        inputField.setOnAction(event -> {
            String userInput = inputField.getText();
            
            if(userInput.equals("/google")) {
                callback.run();
                return;
            }
            
            conversationArea.appendText("Du: " + userInput + "\n");
            inputField.clear();
            
            // Call method to process user input and generate AI response
            generateAIResponse(userInput, new AIResponseListener() {
                @Override
                public void onResponse(String aiResponse) {
                    conversationArea.appendText("GPT: " + aiResponse + "\n");
                }
                
                @Override
                public void onFailure(Throwable throwable) {
                    conversationArea.appendText("GPT: Möglicherweise ist Dein Api-Key falsch Du Hurensohn. Überprüfe diesen bitte und versuche es erneut.\n");
                    conversationArea.appendText("GOTT: Gebe /google ein, um Google zu öffnen.\n");
                }
            });
        });
        
        // Add TextArea and TextField to VBox
        this.getChildren().addAll(new ScrollPane(conversationArea), inputField);
        this.setSpacing(10);
        this.setPadding(new Insets(100));
    }
    
    // Method to generate AI response based on user input
    private void generateAIResponse(String userInput, AIResponseListener listener) {
        MediaType mediaType = MediaType.parse("application/json");
        
        String data = "{\n" +
                "  \"model\": \"gpt-3.5-turbo\",\n" +
                "  \"messages\": [{\"role\": \"user\", \"content\": \"" + userInput + "\"}]\n" +
                "}";
        
        RequestBody body = RequestBody.create(data, mediaType);
        
        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + Main.getApiKey())
                .build();
        
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onFailure(e);
            }
            
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                
                String aiResponse = response.body().string();
                
                Matcher matcher = PATTERN.matcher(aiResponse);
                if (matcher.find()) {
                    String textContent = matcher.group(1);
                    aiResponse = textContent;
                }
                
                listener.onResponse(aiResponse);
            }
        });
    }
    
    // Listener interface for handling AI response callbacks
    private interface AIResponseListener {
        void onResponse(String aiResponse);
        
        void onFailure(Throwable throwable);
    }
}
