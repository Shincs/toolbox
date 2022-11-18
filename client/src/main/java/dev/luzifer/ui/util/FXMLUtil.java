package dev.luzifer.ui.util;

import dev.luzifer.ui.AppStarter;
import javafx.fxml.FXMLLoader;

public final class FXMLUtil {
    
    public static final String FXML_EXTENSION = ".fxml";
    
    public static void loadFXML(Object controller) {
        
        try {
            
            FXMLLoader loader =
                    new FXMLLoader(AppStarter.class.getResource(controller.getClass().getSimpleName() + FXML_EXTENSION));
           
            loader.setController(controller);
            loader.setRoot(controller);
            
            loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private FXMLUtil() {
        throw new UnsupportedOperationException("This class cannot be instantiated!");
    }
}
