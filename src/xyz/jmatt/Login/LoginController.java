package xyz.jmatt.Login;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
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
                   BorderPane CreateAccountPane = FXMLLoader.load(getClass().getResource("/xyz/jmatt/CreateAccount/CreateAccount.fxml"));
                   Stage createAccountWindow = new Stage();
                   createAccountWindow.setScene(new Scene(CreateAccountPane, 960, 540));
                   createAccountWindow.show();
                   //((Node) (event.getSource())).getScene().getWindow().;
               }
               catch (Exception e){
                   e.printStackTrace();
               }
        }
    }
}
