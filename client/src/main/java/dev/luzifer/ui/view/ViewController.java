package dev.luzifer.ui.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class ViewController {
    
    private final Map<String, View> viewMap = new HashMap<>();
    
    public void showView(View view) {
        
        String name = view.getClass().getSimpleName().substring(0, view.getClass().getSimpleName().length() - 4);
        if(viewMap.containsKey(name)) {
            viewMap.get(name).requestFocus();
        } else {
            viewMap.put(name, view);
            loadAndShowView(view, (Class<?> param) -> view, name);
        }
    }
    
    private void loadAndShowView(View view, Callback<Class<?>, Object> controllerFactory, String title) {
        
        Parent root = loadView(view.getClass(), controllerFactory);
        Scene scene = new Scene(root);
        
        view.setScene(scene);
        view.setTitle(title);
        view.setOnHiding(event -> {
            view.onClose();
            viewMap.remove(title);
        });
        view.setResizable(true);
        view.show();
    }
    
    private <T> Parent loadView(Class<T> clazz, Callback<Class<?>, Object> controllerFactory) {
        
        FXMLLoader fxmlLoader = new FXMLLoader();
        
        URL fxmlLocation = clazz.getResource(clazz.getSimpleName() + ".fxml");
        
        fxmlLoader.setLocation(fxmlLocation);
        fxmlLoader.setControllerFactory(controllerFactory);
        
        try {
            return fxmlLoader.load();
        } catch (IOException e) {
            throw new IllegalStateException(MessageFormat.format("FXML could not get loaded for class: {0}", clazz), e);
        }
    }
    
}
