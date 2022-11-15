package dev.luzifer.ui;

import dev.luzifer.Main;
import dev.luzifer.ui.chat.ChatView;
import dev.luzifer.ui.component.CheckBoxLabelComponent;
import dev.luzifer.ui.component.SliderLabelComponent;
import dev.luzifer.ui.util.ImageUtil;
import dev.luzifer.updater.Updater;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class AppStarter extends Application {
    
    private static final File IMAGE_PATH_STORAGE =
            new File(Main.APPDATA_FOLDER, "image_paths.txt");
    
    private final List<String> picturePaths = new ArrayList<>();
    
    private final IntegerProperty indexProperty = new SimpleIntegerProperty(0);
    private final IntegerProperty imageChangeProperty = new SimpleIntegerProperty(5);
    private final DoubleProperty frameOpacityProperty = new SimpleDoubleProperty(0.15);
    private final BooleanProperty switchImagesProperty = new SimpleBooleanProperty(true);
    
    @Override
    public void start(Stage stage) throws Exception {
    
        loadImagePaths();
        
        Platform.setImplicitExit(false);
        
        stage.setTitle("w0rd.3xe v" + Updater.getCurrentVersion());
        stage.setIconified(false);
        stage.initStyle(StageStyle.UTILITY);
    
        Tab tab1 = new Tab("1mAg3 SvviTcH3r");
        tab1.setGraphic(ImageUtil.getImageView("pin_icon.png", ImageUtil.ImageResolution.OKAY));
        tab1.setClosable(false);
        Tab tab2 = new Tab("G00gl3");
        tab2.setGraphic(ImageUtil.getImageView("pin_icon.png", ImageUtil.ImageResolution.OKAY));
        tab2.setClosable(false);
        Tab chatTab = new Tab("Ch4t");
        chatTab.setGraphic(ImageUtil.getImageView("pin_icon.png", ImageUtil.ImageResolution.OKAY));
        chatTab.setClosable(false);
        Tab tab3 = new Tab("Settings");
        tab3.setGraphic(ImageUtil.getImageView("pin_icon.png", ImageUtil.ImageResolution.OKAY));
        tab3.setClosable(false);
        Tab tabOpener = new Tab("+");
        tabOpener.setClosable(false);
        TabPane tabPane = new TabPane(tab1, tab2, chatTab, tab3, tabOpener);
        tabPane.setPrefSize(500, 500);
        
        ImageView imageView = new ImageView();
        ScrollPane scrollPane = new ScrollPane(imageView);
        
        imageView.setOnZoom(e -> {
            imageView.setFitWidth(imageView.getFitWidth() * e.getZoomFactor());
            imageView.setFitHeight(imageView.getFitHeight() * e.getZoomFactor());
        });
        
        tabPane.setOnMouseClicked(event -> {
            if(event.getButton() == MouseButton.PRIMARY)
                stage.setOpacity(frameOpacityProperty.get());
            else
                stage.setOpacity(0.005);
        });
        
        tabPane.setOnDragOver(event -> {
            if(event.getDragboard().hasFiles()) {
                event.acceptTransferModes(event.getTransferMode());
                event.consume();
            }
        });
        
        tabPane.setOnDragDropped(event -> {
            if(event.getDragboard().hasFiles()) {
                event.getDragboard().getFiles().forEach(file -> {
                    if(file.getName().endsWith(".png") || file.getName().endsWith(".jpg") || file.getName().endsWith(".jpeg")) {
                        addImagePath(file.getAbsolutePath());
                    }
                });
                event.consume();
            }
        });
        
        scrollPane.setOnKeyPressed(event -> {
            if(event.getCode().isArrowKey()) {
                if(event.getCode().getName().equals("Right")) {
                    if(indexProperty.get() + 1 >= picturePaths.size())
                        indexProperty.set(0);
                    else
                        indexProperty.set(indexProperty.get() + 1);
                } else if(event.getCode().getName().equals("Left")) {
                    if(indexProperty.get() - 1 < 0)
                        indexProperty.set(picturePaths.size() - 1);
                    else
                        indexProperty.set(indexProperty.get() - 1);
                }
                imageView.setImage(new Image(picturePaths.get(indexProperty.get())));
            }
        });
        
        tab1.setContent(scrollPane);
    
        SliderLabelComponent imageSwitchSlider = new SliderLabelComponent("Picture Switch", 1, 60, 5);
        SliderLabelComponent frameOpacitySlider = new SliderLabelComponent("Frame Opacity", 0.01, 1, 0.15);
        CheckBoxLabelComponent switchImages = new CheckBoxLabelComponent("Auto-Switch Images", true);
        VBox vBox = new VBox(imageSwitchSlider, frameOpacitySlider, switchImages);
        tab3.setContent(vBox);
        
        chatTab.setContent(new ChatView());
        
        imageChangeProperty.bindBidirectional(imageSwitchSlider.getSlider().valueProperty());
        frameOpacityProperty.bindBidirectional(frameOpacitySlider.getSlider().valueProperty());
        switchImagesProperty.bindBidirectional(switchImages.getCheckBox().selectedProperty());
        
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.load("https://google.com/");
        tab2.setContent(webView);
        
        stage.setScene(new Scene(tabPane));
        
        stage.setAlwaysOnTop(true);
        stage.setOpacity(frameOpacityProperty.get());
        stage.show();
        
        Thread thread = new Thread(() -> {
            
            while (true) {
                
                try {
                    Thread.sleep(1000L * imageChangeProperty.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
                if(switchImagesProperty.get()) {
                    
                    Platform.runLater(() -> {
                        if(!picturePaths.isEmpty()) {
                            try {
                                imageView.setImage(new Image(picturePaths.get(indexProperty.get())));
                            } catch (Exception e) {
                                removeImagePath(picturePaths.get(indexProperty.get()));
                            }
                        }
                    });
    
                    if(indexProperty.add(1).get() >= picturePaths.size()-1)
                        indexProperty.set(0);
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    
        stage.setOnCloseRequest(windowEvent -> {
        
            if(!SystemTray.isSupported()) {
                System.out.println("SystemTray is not supported");
                return;
            }
        
            final PopupMenu popup = new PopupMenu();
            final TrayIcon trayIcon;
            java.awt.Image image = new ImageIcon(AppStarter.class.getResource("/icon.png")).getImage();
            trayIcon = new TrayIcon(image, "oohhh baby a tripleeee");
            trayIcon.setImageAutoSize(true);
            final SystemTray tray = SystemTray.getSystemTray();
        
            // Create a pop-up menu components
            MenuItem oeffnenItem = new MenuItem("Öffnen");
            MenuItem exitItem = new MenuItem("Exit");
        
            //Add components to pop-up menu
            popup.add(oeffnenItem);
            popup.addSeparator();
            popup.add(exitItem);
        
            trayIcon.addActionListener(event -> {
                Platform.runLater(() -> {
                    tray.remove(trayIcon);
                    stage.show();
                });
            });
            
            trayIcon.setPopupMenu(popup);
        
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.out.println("TrayIcon could not be added.");
            }
        
            exitItem.addActionListener(e -> {
                System.out.println("Exiting...");
                System.exit(0);
            });
            
            oeffnenItem.addActionListener(e -> {
                Platform.runLater(() -> {
                    tray.remove(trayIcon);
                    stage.show();
                });
            });
        });
    }
    
    private void addImagePath(String path) {
        
        picturePaths.add(path);
        
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(IMAGE_PATH_STORAGE, true))) {
            writer.write(path);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void removeImagePath(String path) {
    
        picturePaths.remove(path);
    
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(IMAGE_PATH_STORAGE, false))) {
            picturePaths.forEach(p -> {
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
    
    private void loadImagePaths() {
        if(IMAGE_PATH_STORAGE.exists()) {
            try {
                picturePaths.addAll(Files.readAllLines(IMAGE_PATH_STORAGE.toPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
