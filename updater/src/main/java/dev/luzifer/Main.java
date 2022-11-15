package dev.luzifer;

import dev.luzifer.updater.Updater;

import java.io.File;

public class Main {
    
    public static void main(String[] args) {
        
        if(args.length == 0) {
            System.out.println("Usage: java -jar updater.jar -spickerLocation=<path to jar>");
            return;
        }
        
        for(String arg : args) {
            if(arg.startsWith("-spickerLocation=")) {
                
                System.out.println("Found spicker location: " + arg.substring(17));
                
                String spickerLocation = arg.substring("-spickerLocation=".length());
                File spicker = new File(spickerLocation);
                
                if(!spicker.exists()) {
                    System.out.println("Spicker not found at " + spicker.getAbsolutePath());
                    return;
                }
                
                System.out.println("Updating spicker...");
                
                Updater.update(spicker, Updater.DOWNLOAD_URL);
                System.out.println("Update complete!");
                
                return;
            }
        }
    }
}