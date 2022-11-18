package dev.luzifer.settings;

import com.google.gson.Gson;
import dev.luzifer.Main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Settings {
    
    public static final Settings DEFAULT_SETTINGS = new Settings(1, 10, true);
    
    public static volatile Settings settings; // TODO: static abuse
    
    public static Settings of(double opacity, int imageSwitchInterval, boolean switchImages) {
        return new Settings(opacity, imageSwitchInterval, switchImages);
    }
    
    public static Settings load() {
    
        File settingsFile = new File(Main.APPDATA_FOLDER, "settings.json");
        if(!settingsFile.exists()) {
        
            Settings.settings = Settings.DEFAULT_SETTINGS;
            save();
        
            return Settings.settings;
        }
    
        try(BufferedReader reader = new BufferedReader(new FileReader(settingsFile))) {
            Settings.settings = new Gson().fromJson(reader, Settings.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        return Settings.settings;
    }
    
    public static void save() {
    
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
    
    private final double opacity;
    private final int imageSwitchInterval;
    private final boolean switchImages;
    
    private Settings(double opacity, int imageSwitchInterval, boolean switchImages) {
        this.opacity = opacity;
        this.imageSwitchInterval = imageSwitchInterval;
        this.switchImages = switchImages;
    }
    
    public double getOpacity() {
        return opacity;
    }
    
    public int getImageSwitchInterval() {
        return imageSwitchInterval;
    }
    
    public boolean isSwitchImages() {
        return switchImages;
    }
}
