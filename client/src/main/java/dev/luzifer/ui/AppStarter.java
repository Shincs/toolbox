package dev.luzifer.ui;

import dev.luzifer.Main;
import dev.luzifer.ui.util.ImageUtil;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppStarter extends Application {
    
    @Override
    public void start(Stage stage) {
        ChromeBite chromeBite = new ChromeBite();
        Scene scene = new Scene(chromeBite);
        
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setTitle("ChromeBite");
        stage.getIcons().add(ImageUtil.getImage("chrome_icon.png"));
        
        stage.show();
        
        Main.updateIfNeeded();
    }
}
