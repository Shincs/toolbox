package dev.luzifer.ui.overlays;

import dev.luzifer.Main;
import dev.luzifer.settings.Settings;
import dev.luzifer.ui.util.FXMLUtil;
import dev.luzifer.ui.util.ImageUtil;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ImageSwitcherOverlay extends StackPane implements Initializable {
    
    private static final File IMAGE_PATH_STORAGE =
            new File(Main.APPDATA_FOLDER, "image_paths.txt");
    
    private static final List<String> PICTURE_PATHS = new ArrayList<>();
    private static final IntegerProperty INDEX_PROPERTY = new SimpleIntegerProperty(0);
    
    static {
        Thread thread = new Thread(() -> {
        
            while (true) {
            
                try {
                    Thread.sleep(1000L * Settings.settings.getImageSwitchInterval());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            
                if(Settings.settings.isSwitchImages()) {
                    if(INDEX_PROPERTY.add(1).get() >= PICTURE_PATHS.size()-1)
                        INDEX_PROPERTY.set(0);
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
    
    private static void addImagePath(String path) {
        
        PICTURE_PATHS.add(path);
        
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(IMAGE_PATH_STORAGE, true))) {
            writer.write(path);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void removeImagePath(String path) {
        
        PICTURE_PATHS.remove(path);
        
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(IMAGE_PATH_STORAGE, false))) {
            PICTURE_PATHS.forEach(p -> {
                try {
                    writer.write(p);
                    writer.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void loadImagePaths() {
        if(IMAGE_PATH_STORAGE.exists()) {
            try {
                PICTURE_PATHS.addAll(Files.readAllLines(IMAGE_PATH_STORAGE.toPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    @FXML
    private ImageView imageView;
    
    @FXML
    private Label dropVisualizer;
    
    @FXML
    private Button buttonNext;
    
    @FXML
    private Button buttonPrevious;
    
    public ImageSwitcherOverlay() {
        FXMLUtil.loadFXML(this);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        
        INDEX_PROPERTY.addListener((observableValue, number, t1) -> {
            if(!PICTURE_PATHS.isEmpty())
                imageView.setImage(new Image(PICTURE_PATHS.get(t1.intValue())));
        });
        
        loadImagePaths();
        dropVisualizer.setVisible(PICTURE_PATHS.isEmpty());
        
        buttonNext.setGraphic(ImageUtil.getImageView("left_icon.png"));
        buttonPrevious.setGraphic(ImageUtil.getImageView("right_icon.png"));
        
        buttonNext.setOnAction(event -> {
            if(!PICTURE_PATHS.isEmpty()) {
                try {
                    imageView.setImage(new Image(PICTURE_PATHS.get(INDEX_PROPERTY.get())));
                } catch (Exception e) {
                    removeImagePath(PICTURE_PATHS.get(INDEX_PROPERTY.get()));
                }
            }
            
            if(INDEX_PROPERTY.add(1).get() >= PICTURE_PATHS.size()-1)
                INDEX_PROPERTY.set(0);
        });
        
        buttonPrevious.setOnAction(event -> {
            if(!PICTURE_PATHS.isEmpty()) {
                try {
                    imageView.setImage(new Image(PICTURE_PATHS.get(INDEX_PROPERTY.get())));
                } catch (Exception e) {
                    removeImagePath(PICTURE_PATHS.get(INDEX_PROPERTY.get()));
                }
            }
            
            if(INDEX_PROPERTY.subtract(1).get() <= 0)
                INDEX_PROPERTY.set(PICTURE_PATHS.size()-1);
        });
        
        setOnDragOver(event -> {
            event.acceptTransferModes(event.getTransferMode());
            dropVisualizer.setVisible(true);
        });
        
        setOnDragDropped(event -> {
            event.getDragboard().getFiles().forEach(file -> {
                if (file.getName().endsWith(".png") || file.getName().endsWith(".jpg")) {
                    addImagePath(file.getAbsolutePath());
                }
            });
            dropVisualizer.setVisible(PICTURE_PATHS.isEmpty());
        });
        
        setOnDragExited(event -> dropVisualizer.setVisible(false));
    }
}
