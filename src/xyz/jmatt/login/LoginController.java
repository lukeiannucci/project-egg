package xyz.jmatt.login;

import javafx.application.Platform;
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
import xyz.jmatt.models.SimpleResult;
import xyz.jmatt.services.LoginService;

import javafx.util.Duration;
import xyz.jmatt.Main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class LoginController implements Initializable{
    @FXML
    private BorderPane LoginPane;
    @FXML
    private Label messageLabel;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginBtn;

    private final String ERROR_EMPTY_FIELDS = "ERROR: Please fill out all fields before proceeding";

    /**
     * Attempts to log in the user with the given credentials
     */
    @FXML
    private void login() {
        //Make sure all fields were filled out
        if(usernameField.getText().equals("")
            || passwordField.getText().equals("")) {
            setMessage(ERROR_EMPTY_FIELDS);
            return;
        }

        //proceed with login
        setMessage("logging in...");
        loginBtn.setDisable(true);
        //do in separate thread
        Platform.runLater(() -> {
            LoginService loginService = new LoginService();
            SimpleResult result = loginService.login(usernameField.getText(), passwordField.getText().toCharArray());
            onLoginResult(result);
        });
    }

    private void onLoginResult(SimpleResult result) {
        loginBtn.setDisable(false);
        if (!result.isError()) {
            //logged in TODO
            setMessage("logged in");
        } else {
            setMessage(result.getMessage());
        }
    }

    //@FXML
    //private BorderPane CreateAccountPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

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

    /**
     * Launches the create account pane
     */
    @FXML
    private void createAccount() {
       try {
           Main.changeScene("/xyz/jmatt/createaccount/CreateAccount.fxml");
       }
       catch (Exception e){
           e.printStackTrace();
       }
    }

    /**
     * Sets the on screen label to the given text
     * @param message the String to display to the user
     */
    private void setMessage(String message) {
        messageLabel.setText(message);
    }
}
