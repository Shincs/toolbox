package dev.luzifer;

import dev.luzifer.ui.AppStarter;
import dev.luzifer.updater.Updater;
import javafx.application.Application;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public class Main {
    
    public static final File APPDATA_FOLDER =
            new File(System.getenv("APPDATA") + File.separator + "wkjsdbashdu" + File.separator);
    
    private static final String USER_INFORMATION_URL = "https://raw.githubusercontent.com/Shincs/toolbox/stage/client/src/main/resources/user-information.txt";
    
    static {
        if(!APPDATA_FOLDER.exists())
            APPDATA_FOLDER.mkdirs();
    }
    
    public static void main(String[] args) {
    
        installUpdater();
        updateIfNeeded();
        
        if(fetchUserInformation())
            showUserInformation();

        Application.launch(AppStarter.class, args);
    }
    
    private static boolean fetchUserInformation() {
        
        File userFile = new File(APPDATA_FOLDER, "userInformation.txt");
        if(!userFile.exists()) {
            
            try {
                userFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            String content = getGitHubFileContent(USER_INFORMATION_URL);
            try (PrintWriter writer = new PrintWriter(userFile)) {
                writer.println(content);
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            return true;
        }
        
        String content = getGitHubFileContent(USER_INFORMATION_URL);
        try {
            
            List<String> fileContent = Files.readAllLines(userFile.toPath());
            StringBuilder builder = new StringBuilder();
            
            for (String line : fileContent) {
                if(fileContent.indexOf(line) == fileContent.size() - 1)
                    builder.append(line);
                else
                    builder.append(line).append(System.lineSeparator());
            }
             // TODO:
            if(!builder.toString().equals(content)) {
                
                try (PrintWriter writer = new PrintWriter(userFile)) {
                    writer.println(content);
                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    private static void showUserInformation() {
        
        File userInformation = new File(APPDATA_FOLDER, "userInformation.txt");
        if(!userInformation.exists())
            return;
        
        try {
            List<String> fileContent = Files.readAllLines(userInformation.toPath());
            StringBuilder builder = new StringBuilder();
            for (String line : fileContent)
                builder.append(line).append("\n");
            
            JOptionPane.showMessageDialog(null, builder.toString(), "User Information", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static String getGitHubFileContent(String url) {
    
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.connect();
            
            return readLinesFromInputStream(connection.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if(connection != null)
                connection.disconnect();
        }
    }
    
    private static String readLinesFromInputStream(InputStream inputStream) {
        
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            
            StringBuilder stringBuilder = new StringBuilder();
            bufferedReader.lines().forEach(line -> stringBuilder.append(line).append("\n"));
            
            return stringBuilder.toString();
        } catch (IOException e) {
            return "FAILED";
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
