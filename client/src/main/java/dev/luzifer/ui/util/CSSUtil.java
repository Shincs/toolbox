package dev.luzifer.ui.util;

import dev.luzifer.ui.AppStarter;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.net.URL;
import java.text.MessageFormat;

/**
 * A utility class for loading applying CSS.
 */
public final class CSSUtil {
    
    private static final String STYLING_PATH = "styling/";
    private static final String STYLING_EXTENSION = ".css";
    
    /**
     * Applies the CSS file with the same name as the class to the given node.
     * @param node The node to apply the CSS to.
     *             The node must have a class with the same name as the CSS file.
     *             The CSS file must be located in the {@link #STYLING_PATH}.
     */
    public static void applyStyle(Scene node) {
        
        URL resource = AppStarter.class.getResource(getStylePath(AppStarter.class));
        
        if(resource != null)
            node.getStylesheets().add(resource.toExternalForm());
        else
            throw new IllegalStateException(MessageFormat.format("CSS file could not get loaded for class: {0}", AppStarter.class.getSimpleName()));
    }
    
    private static String getStylePath(Class<?> clazz) {
        return STYLING_PATH + clazz.getSimpleName() + STYLING_EXTENSION;
    }
    
    private CSSUtil() {
    }
}
