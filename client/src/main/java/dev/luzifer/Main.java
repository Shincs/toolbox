package dev.luzifer;

import dev.luzifer.chat.ChatController;
import dev.luzifer.ui.AppStarter;
import javafx.application.Application;

import javax.swing.*;

public class Main {
    
    public static void main(String[] args) {
        
        JOptionPane pane = new JOptionPane();
        
        String ip = pane.showInputDialog("Server IP:");
        int port = Integer.parseInt(pane.showInputDialog("Server Port:"));
        
        
        ChatController.connect(ip, port);
        Application.launch(AppStarter.class, args);
    }
}
