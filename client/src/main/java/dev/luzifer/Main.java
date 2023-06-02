package dev.luzifer;

import dev.luzifer.ui.AppStarter;
import dev.luzifer.updater.Updater;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
    
    public static final File APPDATA_FOLDER =
            new File(System.getenv("APPDATA") + File.separator + "chromebite" + File.separator);
    
    static {
        if(!APPDATA_FOLDER.exists())
            APPDATA_FOLDER.mkdirs();
    }
    
    public static void main(String[] args) {
    
        installUpdater();
        
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
    
    public static void updateIfNeeded() {
        if (Updater.isUpdateAvailable(Updater.getCurrentVersion(), Updater.VERSION_URL)) {
            Platform.runLater(() -> {
                javafx.scene.control.Alert alert = new Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
                alert.setTitle("Update verfügbar");
                alert.setHeaderText("Es ist ein Update verfügbar - " + Updater.getCurrentVersion() + " -> " + Updater.getRemoteVersion());
                alert.setContentText("Möchtest du es jetzt installieren?");
                alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
                
                alert.showAndWait().ifPresent(buttonType -> {
                    if (buttonType == ButtonType.YES) {
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
                });
            });
        }
    }
}
