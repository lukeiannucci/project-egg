package xyz.jmatt.login;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import xyz.jmatt.models.SimpleResult;
import xyz.jmatt.services.LoginService;


public class LoginController {
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
        if(!result.isError()) {
            //logged in TODO
            setMessage("logged in");
        } else {
            setMessage(result.getMessage());
        }
    }

    /**
     * Launches the create account pane
     */
    @FXML
    private void createAccount(){
       try {
           BorderPane CreateAccountPane = FXMLLoader.load(getClass().getResource("/xyz/jmatt/createaccount/CreateAccount.fxml"));
           LoginPane.getChildren().setAll(CreateAccountPane);
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
