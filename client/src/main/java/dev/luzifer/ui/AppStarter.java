package dev.luzifer.ui;

import com.google.gson.Gson;
import dev.luzifer.Main;
import dev.luzifer.settings.Settings;
import dev.luzifer.ui.overlays.MainOverlay;
import dev.luzifer.updater.Updater;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class AppStarter extends Application {
    
    private static final File SCREENSHOT_FOLDER = new File(Main.APPDATA_FOLDER, "screenshots");
    
    static {
        if(!SCREENSHOT_FOLDER.exists())
            SCREENSHOT_FOLDER.mkdirs();
    }
    
    @Override
    public void start(Stage stage) {
    
        MainOverlay overlay = new MainOverlay();
    
        stage.setScene(new Scene(overlay));
        stage.setTitle("w0rd.3xe v" + Updater.getCurrentVersion());
        stage.setIconified(false);
        stage.setResizable(false);
        stage.initStyle(StageStyle.UTILITY);
        
        stage.getScene().setOnMouseClicked(event -> {
            if(event.getButton().name().equals("SECONDARY")) {
                setupTray(stage);
                stage.close();
            }
        });
    
        Platform.setImplicitExit(false);
        Settings.settings = loadSettings();
    
        stage.setAlwaysOnTop(true);
        stage.setOpacity(Settings.settings.getOpacity());
        stage.setOnCloseRequest(windowEvent -> setupTray(stage));
        
        stage.show();
    }
    
    private void setupTray(Stage stage) {
        
        if(!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }
        
        final PopupMenu popup = new PopupMenu();
        final TrayIcon trayIcon;
        java.awt.Image image = new ImageIcon(AppStarter.class.getResource("/icon.png")).getImage();
        trayIcon = new TrayIcon(image, "oohhh baby a tripleeee");
        trayIcon.setImageAutoSize(true);
        final SystemTray tray = SystemTray.getSystemTray();
        
        MenuItem oeffnenItem = new MenuItem("Öffnen");
        MenuItem screenshotItem = new MenuItem("Screenshot");
        MenuItem exitItem = new MenuItem("Exit");
        
        popup.add(oeffnenItem);
        popup.add(screenshotItem);
        popup.addSeparator();
        popup.add(exitItem);
        
        trayIcon.addActionListener(event -> {
            Platform.runLater(() -> {
                tray.remove(trayIcon);
                stage.show();
            });
        });
        
        trayIcon.setPopupMenu(popup);
        
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }
        
        exitItem.addActionListener(e -> {
            System.out.println("Exiting...");
            System.exit(0);
        });
        
        oeffnenItem.addActionListener(e -> {
            Platform.runLater(() -> {
                tray.remove(trayIcon);
                stage.show();
            });
        });
        
        screenshotItem.addActionListener(e -> {
            Platform.runLater(() -> takeScreenshot());
        });
    }
    
    private void takeScreenshot() {
        try {
            
            Robot robot = new Robot();
            
            Rectangle rectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage image = robot.createScreenCapture(rectangle);
            
            File file = new File(SCREENSHOT_FOLDER, UUID.randomUUID() + ".png");
            ImageIO.write(image, "png", file);
            
            file.createNewFile();
        } catch (AWTException | IOException e) {
            e.printStackTrace();
        }
    }
    
    private Settings loadSettings() {
        
        File settingsFile = new File(Main.APPDATA_FOLDER, "settings.json");
        if(!settingsFile.exists()) {
            
            Settings.settings = Settings.DEFAULT_SETTINGS;
            saveSettings();
            
            return Settings.settings;
        }
        
        try(BufferedReader reader = new BufferedReader(new FileReader(settingsFile))) {
            Settings.settings = new Gson().fromJson(reader, Settings.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return Settings.settings;
    }
    
    private void saveSettings() {
        
        File settingsFile = new File(Main.APPDATA_FOLDER, "settings.json");
        if(!settingsFile.exists()) {
            try {
                settingsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(settingsFile))) {
            writer.write(new Gson().toJson(Settings.settings));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
