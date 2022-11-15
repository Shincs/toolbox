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
                            
                            textArea.appendText(">> Connecting to " + ip + ":" + port + "...\n");
                            ChatController.connect(ip, port).thenAccept(connected -> {
                                if (Boolean.TRUE.equals(connected)) {
                                    textArea.appendText(">> Connected!\n");
                                    ChatController.onMessage(message -> Platform.runLater(() -> textArea.appendText(message + "\n")));
                                } else {
                                    textArea.appendText(">> Connection failed!\n");
                                }
                            });
                        }
                    } else {
                        textArea.appendText("Du bist zu keinem Chat verbunden. \nVerbinde dich mit /connect <host> <port> \n");
                    }
                } else {
                    ChatController.chat(textField.getText());
                }
                
                textField.clear();
            }
        });
        
        textArea.setPromptText("Noch keine Nachrichten empfangen...");
        textArea.setWrapText(true);
        textArea.setEditable(false);
        
        getChildren().addAll(new ScrollPane(textArea), textField);
    }
}
