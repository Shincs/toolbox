package dev.luzifer.updater;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Updater {
    
    public static final String DOWNLOAD_URL = "https://github.com/Shincs/toolbox/releases/download/latest/spicker.jar";
    public static final String VERSION_URL = "https://raw.githubusercontent.com/Shincs/toolbox/master/client/src/main/resources/version.txt";
    
    public static void update(File target, String downloadUrl) {
        try {
            URL downloadFrom = new URL(downloadUrl);
            FileUtils.copyURLToFile(downloadFrom, target);
        } catch (IOException ignored) {
        }
    }
    
    public static boolean isUpdateAvailable(File toUpdate, String versionUrl) {
        
        if(!toUpdate.exists())
            return true;
        
        try (ZipFile zipFile = new ZipFile(toUpdate)) {
            ZipEntry versionEntry = zipFile.getEntry("version.txt");
            if(versionEntry == null)
                return true;
            
            String version = readLineFromInputStream(zipFile.getInputStream(versionEntry));
            return isUpdateAvailable(version, versionUrl);
        } catch (IOException ignored) {
        }
        return true;
    }
    
    public static boolean isUpdateAvailable(String version, String versionUrl) {
        
        UpdateChecker updateChecker = new UpdateChecker(versionUrl);
        updateChecker.checkUpdate(version);
        
        return updateChecker.isUpdateAvailable();
    }
    
    public static String getCurrentVersion() {
        return readLineFromInputStream(getInputStream("version.txt"));
    }
    
    private static InputStream getInputStream(String fileName) {
        
        InputStream resource = Updater.class.getResourceAsStream("/" + fileName);
        
        if (resource == null)
            throw new IllegalStateException("Probably corrupted JAR file, missing " + fileName);
        
        return resource;
    }
    
    private static String readLineFromInputStream(InputStream inputStream) {
        
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            return bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return "INVALID";
    }
    
    private static boolean isEmpty(File file) {
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(FileUtils.openInputStream(file), StandardCharsets.UTF_8))) {
            return bufferedReader.readLine() == null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
}
