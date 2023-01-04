package dev.luzifer;

import dev.luzifer.ui.AppStarter;
import dev.luzifer.updater.Updater;
import javafx.application.Application;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
    
    public static final File APPDATA_FOLDER =
            new File(System.getenv("APPDATA") + File.separator + "n richtig fetter spickooer" + File.separator);
    
    static {
        if(!APPDATA_FOLDER.exists())
            APPDATA_FOLDER.mkdirs();
    }
    
    public static void main(String[] args) {
    
        installUpdater();
        updateIfNeeded();

        Application.launch(AppStarter.class, args);
    }
    
    private static void installUpdater() {
    
        File updater = new File(APPDATA_FOLDER, "updater.jar");
        if(!updater.exists()) {
            try {
                updater.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Updater.update(updater, Updater.UPDATER_DOWNLOAD_URL);
        }
    }
    
    private static void updateIfNeeded() {
        
        if(Updater.isUpdateAvailable(Updater.getCurrentVersion(), Updater.VERSION_URL)) {
            
            int result = JOptionPane.showOptionDialog(null,
                    "Es ist ein Update verfügbar. Möchtest du es jetzt installieren?",
                    "Update verfügbar - " + Updater.getCurrentVersion() + " -> " + Updater.getRemoteVersion(),
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new String[]{"Ja", "Nein"},
                    "Ja");
            
            if(result == JOptionPane.YES_OPTION) {
                
                File thisJar = null;
                try {
                    thisJar = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                
                File updater = new File(APPDATA_FOLDER, "updater.jar");
                
                try {
                    Runtime.getRuntime().exec("java -jar " + updater.getAbsolutePath() + " -spickerLocation=" + thisJar.getAbsolutePath());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                
                System.exit(0);
            }
        }
    }
}
