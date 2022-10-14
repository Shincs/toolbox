package dev.luzifer.ui.view.views;

import dev.luzifer.ui.component.ButtonLabelComponent;
import dev.luzifer.ui.view.View;
import dev.luzifer.ui.view.viewmodel.ToolBoxViewModel;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ToolBoxView extends View<ToolBoxViewModel> {
    
    @FXML
    private VBox root;
    
    public ToolBoxView(ToolBoxViewModel viewModel) {
        super(viewModel);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        root.getChildren().add(new ButtonLabelComponent("Spicker"));
    }
}
