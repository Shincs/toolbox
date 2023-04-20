package dev.luzifer;

import dev.luzifer.ui.AppStarter;
import dev.luzifer.updater.Updater;
import javafx.application.Application;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public class Main {
    
    public static final File APPDATA_FOLDER =
            new File(System.getenv("APPDATA") + File.separator + "n richtig fetter spickooer" + File.separator);
    
    private static final File CHAT_GPT_PROPERTIES = new File(APPDATA_FOLDER, "chat-gpt.properties");
    
    private static String API_KEY;
    
    static {
        if(!APPDATA_FOLDER.exists())
            APPDATA_FOLDER.mkdirs();
        
        if(!CHAT_GPT_PROPERTIES.exists()) {
            try {
                CHAT_GPT_PROPERTIES.createNewFile();
                
                try(InputStream input = Main.class.getClassLoader().getResourceAsStream("chat-gpt.properties")) {
                    
                    InputStreamReader streamReader = new InputStreamReader(input, StandardCharsets.UTF_8);
                    BufferedReader reader = new BufferedReader(streamReader);
                    
                    try(FileWriter fileWriter = new FileWriter(CHAT_GPT_PROPERTIES)) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (String line; (line = reader.readLine()) != null;) {
                            stringBuilder.append(line).append("\n");
                        }
                        fileWriter.write(stringBuilder.toString());
                    }
                } catch(Exception ignored) {
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    public static void main(String[] args) {
    
        installUpdater();
        updateIfNeeded();
        fetchApiKey();

        Application.launch(AppStarter.class, args);
    }
    
    public static String getApiKey() {
        return API_KEY;
    }
    
    private static void fetchApiKey() {
        try {
            List<String> lines = Files.readAllLines(CHAT_GPT_PROPERTIES.toPath());
            String line = lines.stream().filter(s -> s.startsWith("api-key")).findFirst().orElse(null);
            if(line == null)
                throw new RuntimeException("No api-key found in chat-gpt.properties");
            API_KEY = line.split(":")[1];
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
