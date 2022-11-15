package dev.luzifer;

import dev.luzifer.updater.Updater;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Main {
    
    public static void main(String[] args) {
    
        if(args.length == 0)
            return;
        
        String spickerLocation = args[0].split("=")[1];
        
        JFrame jFrame = new JFrame("Updater");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(400, 200);
        jFrame.setLocationRelativeTo(null);
        jFrame.add(new Label("Updating..."));
        jFrame.setVisible(true);
        
        Updater.update(new File(spickerLocation), Updater.DOWNLOAD_URL);
    
        try {
            Runtime.getRuntime().exec("java -jar " + spickerLocation);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.exit(0);
    }
}