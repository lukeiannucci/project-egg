package xyz.jmatt.createaccount;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import xyz.jmatt.Main;
import xyz.jmatt.models.SimpleResult;
import xyz.jmatt.services.CreateAccountService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CreateAccountController implements Initializable{
    @FXML
    private Button createAccountBtn;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField passwordConfirmField;
    @FXML
    private Label messageLabel;
    @FXML
    private BorderPane CreateAccountPane;
    @FXML
    private Button backButton;

    private final String ERROR_EMPTY_FIELDS = "ERROR: Please fill out all fields before proceeding";
    private final String ERROR_DIFFERENT_PASSWORDS = "ERROR: Passwords did not match";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SlideTransitionEnter(CreateAccountPane);
    }

    /*
     * Attempts to create a new account with the information given
     */
    @FXML
    public void createAccount() {
        // Make sure all fields were filled out
        if(usernameField.getText().equals("")
            || passwordField.getText().equals("")
            || passwordConfirmField.getText().equals("")) {
            setMessage(ERROR_EMPTY_FIELDS);
            return;
        }
        // Make sure confirm passwords match
        if(!passwordField.getText().equals(passwordConfirmField.getText())) {
            setMessage(ERROR_DIFFERENT_PASSWORDS);
            return;
        }

        //proceed with creating a new account
        setMessage("creating account...");
        createAccountBtn.setDisable(true); //disable the button until it's done to prevent duplicates
        //do in separate thread
        Platform.runLater(() -> {
            CreateAccountService accountService = new CreateAccountService();
            SimpleResult result = accountService.createAccount(usernameField.getText(), passwordField.getText().toCharArray());
            try {
                onCreateAccountResult(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Called after the create account thread finishes
     * @param result the result of the account creation process
     */
    private void onCreateAccountResult(SimpleResult result) throws IOException {
        createAccountBtn.setDisable(false); //re-enable button
        if(!result.isError()) {
            //account was made TODO
            //TODO login them in or make them retype credentials??
            setMessage("account created");
            SlideTransitionExit(CreateAccountPane);
        } else {
            setMessage(result.getMessage());
        }
    }
    @FXML
    private void BackButtonPressed() throws IOException
    {
        SlideTransitionExit(CreateAccountPane);
    }

    private void SlideTransitionExit(Node e)
    {
        TranslateTransition x = new TranslateTransition(new Duration(500), e);
        x.setFromX(0.0);
        x.setToX(600.0);
        x.setCycleCount(1);
        x.setOnFinished(event -> {
            try {
                Main.changeScene("/xyz/jmatt/login/Login.fxml");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        x.play();
    }

    private void SlideTransitionEnter(Node e)
    {
        TranslateTransition x = new TranslateTransition(new Duration(500), e);
        x.setFromX(600.0);
        x.setToX(0);
        x.setCycleCount(1);
        x.play();
    }

    /**
     * Sets the on screen  message label to the given message
     * @param message The message to display to the user
     */
    private void setMessage(String message) {
        messageLabel.setText(message);
    }
}
