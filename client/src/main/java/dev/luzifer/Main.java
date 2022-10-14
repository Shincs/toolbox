package dev.luzifer;

import dev.luzifer.ui.AppStarter;
import javafx.application.Application;

public class Main {
    
    // TODO: For later chat implementation
    private static final String IP = "localhost";
    private static final int PORT = 1337;
    
    public static void main(String[] args) {
        Application.launch(AppStarter.class, args);
    }
}
