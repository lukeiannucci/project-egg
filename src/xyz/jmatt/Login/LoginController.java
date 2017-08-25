package xyz.jmatt.Login;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

public class LoginController {
    @FXML
    private void OnMouseEntered(MouseEvent event){
        Button myButton = (Button)event.getSource();
        myButton.setUnderline(true);
    }
    @FXML
    private void OnMouseExited(MouseEvent event){
        Button myButton = (Button)event.getSource();
        myButton.setUnderline(false);
    }
}
