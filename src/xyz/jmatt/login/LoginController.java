package xyz.jmatt.login;

import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;


public class LoginController implements Initializable{
    @FXML
    private BorderPane LoginPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(4));
        try{
            BorderPane CreateAccountPane = FXMLLoader.load(getClass().getResource("/xyz/jmatt/createaccount/CreateAccount.fxml"));
            transition.setNode(CreateAccountPane);
            transition.setToX(500);
            transition.setToY(200);
            transition.play();
        }catch (Exception e)
        {

        }

    }

    @FXML
    private void OnMouseClicked(MouseEvent event){
        if(event.getButton() == MouseButton.PRIMARY){
               try {
                   //BorderPane CreateAccountPane = FXMLLoader.load(getClass().getResource("/xyz/jmatt/createaccount/CreateAccount.fxml"));
                   //TranslateTransition slide = new TranslateTransition(new Duration(5000), CreateAccountPane);
                   //slide.setToX(0);
                   //slide.play();
                   //Stage createAccountWindow = new Stage();
                   //createAccountWindow.setScene(new Scene(CreateAccountPane, 600, 400));
                   //createAccountWindow.show();
                   //createAccountWindow.setMinWidth(createAccountWindow.getWidth());
                   //createAccountWindow.setMinHeight(createAccountWindow.getHeight());
                   //LoginPane.getChildren().setAll(CreateAccountPane);
               }
               catch (Exception e){
                   e.printStackTrace();
               }
        }
    }
}
