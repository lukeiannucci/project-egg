package xyz.jmatt.login;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


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

    @FXML
    private void OnMouseClicked(MouseEvent event){
        if(event.getButton() == MouseButton.PRIMARY){
               try {
                   BorderPane CreateAccountPane = FXMLLoader.load(getClass().getResource("/xyz/jmatt/createaccount/CreateAccount.fxml"));
                   Stage createAccountWindow = new Stage();
                   createAccountWindow.setScene(new Scene(CreateAccountPane, 600, 400));
                   createAccountWindow.show();
                   createAccountWindow.setMinWidth(createAccountWindow.getWidth());
                   createAccountWindow.setMinHeight(createAccountWindow.getHeight());
                   //((Node) (event.getSource())).getScene().getWindow().;
               }
               catch (Exception e){
                   e.printStackTrace();
               }
        }
    }
}
