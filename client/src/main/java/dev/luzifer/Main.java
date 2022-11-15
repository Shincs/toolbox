package dev.luzifer;

import dev.luzifer.ui.AppStarter;
import dev.luzifer.updater.Updater;
import javafx.application.Application;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;

public class Main {
    
    public static final File APPDATA_FOLDER =
            new File(System.getenv("APPDATA") + File.separator + "wkjsdbashdu" + File.separator);
    
    static {
        if(!APPDATA_FOLDER.exists())
            APPDATA_FOLDER.mkdirs();
    }
    
    public static void main(String[] args) {
        
        cleanupIfHadUpdate();
        updateIfNeeded();

        Application.launch(AppStarter.class, args);
    }
    
    private static void cleanupIfHadUpdate() {
    
        File jarPath = null;
        try {
            jarPath = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    
        File oldJar = new File(jarPath.getParentFile(), "spicker_old.jar");
        if(oldJar.exists())
            oldJar.delete();
    }
    
    private static void updateIfNeeded() {
    
        File jarPath = null;
        try {
            jarPath = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        
        if(Updater.isUpdateAvailable(Updater.getCurrentVersion(), Updater.VERSION_URL)) {
            
            if(jarPath.getName().equals("spicker.jar"))
                jarPath.renameTo(new File(jarPath.getParentFile(), "spicker_old.jar"));
            
            int result = JOptionPane.showOptionDialog(null,
                    "Es ist ein Update verfügbar. Möchtest du es jetzt installieren?",
                    "Update verfügbar - " + Updater.getCurrentVersion() + " -> " + Updater.getRemoteVersion(),
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new String[]{"Ja", "Nein"},
                    "Ja");
            
            if(result == JOptionPane.YES_OPTION) {
                
                File spicker = new File(jarPath.getParent(), "spicker.jar");
                Updater.update(spicker, Updater.DOWNLOAD_URL);
                
                try {
                    Runtime.getRuntime().exec("java -jar " + spicker.getAbsolutePath());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                
                System.exit(0);
            }
        }
    }
}
