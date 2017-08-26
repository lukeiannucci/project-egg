package xyz.jmatt.login;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class LoginController {
    @FXML
    private BorderPane LoginPane;

    @FXML
    private void OnMouseClicked(MouseEvent event){
        if(event.getButton() == MouseButton.PRIMARY){
               try {
                   BorderPane CreateAccountPane = FXMLLoader.load(getClass().getResource("/xyz/jmatt/createaccount/CreateAccount.fxml"));
                   //Stage createAccountWindow = new Stage();
                   //createAccountWindow.setScene(new Scene(CreateAccountPane, 600, 400));
                   //createAccountWindow.show();
                   //createAccountWindow.setMinWidth(createAccountWindow.getWidth());
                   //createAccountWindow.setMinHeight(createAccountWindow.getHeight());
                   LoginPane.getChildren().setAll(CreateAccountPane);
                   //((BorderPane)(event.getSource())).;
               }
               catch (Exception e){
                   e.printStackTrace();
               }
        }
    }
}
