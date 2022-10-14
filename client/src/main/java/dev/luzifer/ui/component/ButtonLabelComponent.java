package dev.luzifer.ui.component;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class ButtonLabelComponent extends HBox {
    
    private final Label label = new Label();
    
    private final Button button = new Button();
    
    public ButtonLabelComponent(String title) {
        
        this.label.setText(title);
        
        setSpacing(100);
        
        getChildren().addAll(label, button);
    }
    
    public Button getButton() {
        return button;
    }
    
    public Label getLabel() {
        return label;
    }
}
