package dev.luzifer.updater;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Updater {
    
    public static final String DOWNLOAD_URL = "https://github.com/Shincs/toolbox/releases/download/latest/spicker.jar";
    
    public static void update(File target, String downloadUrl) {
        try {
            URL downloadFrom = new URL(downloadUrl);
            FileUtils.copyURLToFile(downloadFrom, target);
        } catch (IOException ignored) {
        }
    }
    
}
