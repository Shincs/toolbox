package dev.luzifer.ui.chat;

import dev.luzifer.chat.ChatController;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ChatView extends VBox {

    private final VBox textArea = new VBox();
    private final TextField textField = new TextField();
    
    public ChatView() {
        super();
        
        setPrefSize(500, 500);
        textArea.setPrefSize(500, 450);
        textField.setPrefSize(500, 50);
        
        textField.setPromptText("Enter message...");
        textField.setOnKeyPressed(event -> {
            if(event.getCode().getName().equals("Enter")) {
                
                if(!ChatController.isConnected()) {
    
                    String input = textField.getText();
                    if(input.startsWith("/")) {
                        
                        String[] args = input.replace("/", "").split(" ");
                        
                        if(args.length == 3 && args[0].equalsIgnoreCase("connect")) {
                            
                            String ip = args[1];
                            int port = Integer.parseInt(args[2]);
                            
                            textArea.getChildren().add(new Label(">> Connecting to " + ip + ":" + port + "...\n"));
                            ChatController.connect(ip, port).thenAccept(connected -> {
                                Platform.runLater(() -> {
                                    if (Boolean.TRUE.equals(connected)) {
                                        textArea.getChildren().add(new Label(">> Connected!\n"));
                                        ChatController.onMessage(message -> Platform.runLater(() ->
                                                textArea.getChildren().add(new Label(message + "\n"))));
                                    } else {
                                        textArea.getChildren().add(new Label(">> Connection failed!\n"));
                                    }
                                });
                            });
                        }
                    } else {
                        textArea.getChildren().add(new Label("Du bist zu keinem Chat verbunden. \nVerbinde dich mit /connect <host> <port> \n"));
                    }
                } else {
                    ChatController.chat(textField.getText());
                }
                
                textField.clear();
            }
        });
        
        getChildren().addAll(new ScrollPane(textArea), textField);
    }
}
