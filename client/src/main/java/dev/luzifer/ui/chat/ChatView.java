package dev.luzifer.ui.chat;

import dev.luzifer.chat.ChatController;
import javafx.application.Platform;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ChatView extends VBox {

    private final TextArea textArea = new TextArea();
    private final TextField textField = new TextField();
    
    public ChatView() {
        super();
        
        setPrefSize(500, 500);
        
        textField.setPromptText("Enter message...");
        textField.setOnKeyPressed(event -> {
            if(event.getCode().getName().equals("Enter")) {
                ChatController.chat(textField.getText());
                textField.clear();
            }
        });
        
        textArea.setPromptText("Noch keine Nachrichten empfangen...");
        textArea.setWrapText(true);
        textArea.setEditable(false);
        
        ChatController.onMessage(message -> Platform.runLater(() ->
                textArea.appendText(message + "\n")));
        
        getChildren().addAll(new ScrollPane(textArea), textField);
    }
}
