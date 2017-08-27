package xyz.jmatt.login;

import javafx.animation.*;
import javafx.application.ConditionalFeature;
import javafx.event.ActionEvent;
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
import xyz.jmatt.Main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class LoginController implements Initializable{
    @FXML
    private BorderPane LoginPane;

    //@FXML
    //private BorderPane CreateAccountPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TranslateTransition transition = new TranslateTransition();
        //transition.setDuration(Duration.seconds(4));
        try{
            //BorderPane CreateAccountPane = FXMLLoader.load(getClass().getResource("/xyz/jmatt/createaccount/CreateAccount.fxml"));
            //CreateAccountPane.set
            //transition.setNode(CreateAccountPane);
            //transition.setToX(500);
            //transition.setToY(200);
            //transition.play();
        }catch (Exception e)
        {
            System.out.println(e.toString());
        }

    }
    private void trans(Node e) throws Exception
    {
        FadeTransition x = new FadeTransition(new Duration(500), e);
        x.setFromValue(0);
        x.setToValue(100);
        x.setCycleCount(1);
        x.setOnFinished(event -> {
            try {
                    Main.changeScene("");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            });
    x.setInterpolator(Interpolator.LINEAR);
        x.play();
    }

    @FXML
    private void OnMouseClicked(MouseEvent event){
        if(event.getButton() == MouseButton.PRIMARY){
               try {
                   //trans(LoginPane);
                   Main.changeScene("");
               }
               catch (Exception e){
                   e.printStackTrace();
               }
        }
    }
}
