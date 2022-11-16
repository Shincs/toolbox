package dev.luzifer;

import dev.luzifer.updater.Updater;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class Main {
    
    public static void main(String[] args) {
    
        JFrame frame = new JFrame("Updater");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        JLabel label = new JLabel("Checking for updates...");
        frame.add(label);
        
        if(args.length == 0) {
            label.setText("Usage: java -jar updater.jar -spickerLocation=<path to jar>");
            return;
        }
        
        for(String arg : args) {
            if(arg.startsWith("-spickerLocation=")) {
                
                label.setText("Found spicker location: " + arg.substring(17));
                
                String spickerLocation = arg.substring("-spickerLocation=".length());
                File spicker = new File(spickerLocation);
                
                if(!spicker.exists()) {
                    label.setText("Spicker not found at " + spickerLocation);
                    return;
                }
                
                label.setText("Updating spicker...");
                Updater.update(spicker, Updater.DOWNLOAD_URL);
                label.setText("Update complete!");
    
                try {
                    Runtime.getRuntime().exec("java -jar " + spicker.getAbsolutePath());
                    System.exit(0);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
    
                return;
            }
        }
    }
}