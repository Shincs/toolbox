package dev.luzifer;

import dev.luzifer.chat.ChatController;
import dev.luzifer.ui.AppStarter;
import javafx.application.Application;

import javax.swing.*;
import java.io.File;

public class Main {
    
    public static final File APPDATA_FOLDER =
            new File(System.getenv("APPDATA") + File.separator + "wkjsdbashdu" + File.separator);
    
    static {
        if(!APPDATA_FOLDER.exists())
            APPDATA_FOLDER.mkdirs();
    }
    
    public static void main(String[] args) {
        
        JOptionPane pane = new JOptionPane();
        
        String ip = pane.showInputDialog("Server IP:");
        int port = Integer.parseInt(pane.showInputDialog("Server Port:"));
        
        ChatController.connect(ip, port);
        Application.launch(AppStarter.class, args);
    }
    
    private static void doesUpdaterExist() {
        File updater = new File(APPDATA_FOLDER, "updater.jar");
    }
}
