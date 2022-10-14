package dev.luzifer.ui.component;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class CheckBoxLabelComponent extends HBox {
    
    private final CheckBox checkBox = new CheckBox();
    
    private final Label label = new Label();
    
    public CheckBoxLabelComponent(String title, boolean selected) {
        super();
        
        checkBox.setSelected(selected);
        label.setText(title);
        
        getChildren().addAll(checkBox, label);
    }
    
    public CheckBox getCheckBox() {
        return checkBox;
    }
}
