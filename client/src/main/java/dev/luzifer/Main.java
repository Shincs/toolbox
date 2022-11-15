package dev.luzifer;

import dev.luzifer.chat.ChatController;
import dev.luzifer.ui.AppStarter;
import dev.luzifer.updater.Updater;
import javafx.application.Application;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class Main {
    
    public static final File APPDATA_FOLDER =
            new File(System.getenv("APPDATA") + File.separator + "wkjsdbashdu" + File.separator);
    
    static {
        if(!APPDATA_FOLDER.exists())
            APPDATA_FOLDER.mkdirs();
    }
    
    public static void main(String[] args) {
        
        doesUpdaterExist();
        updateIfNeeded();
        
        JOptionPane pane = new JOptionPane();
        
        String ip = pane.showInputDialog("Server IP:");
        int port = Integer.parseInt(pane.showInputDialog("Server Port:"));
        
        ChatController.connect(ip, port);
        Application.launch(AppStarter.class, args);
    }
    
    private static void doesUpdaterExist() {
        
        File updater = new File(APPDATA_FOLDER, "updater.jar");
        
        if(!updater.exists()) {
            try {
                updater.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Updater.update(updater, Updater.DOWNLOAD_URL);
        }
    }
    
    private static void updateIfNeeded() {
    
        File currentJar = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        File updater = new File(APPDATA_FOLDER, "updater.jar");
        
        if(Updater.isUpdateAvailable(Updater.getCurrentVersion(), Updater.VERSION_URL)) {
            try {
                Runtime.getRuntime().exec("java -jar " + updater.getAbsolutePath() + " -spickerLocation=" + currentJar.getAbsolutePath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.exit(0);
        }
    }
}
