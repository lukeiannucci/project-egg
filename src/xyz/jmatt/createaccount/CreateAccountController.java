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
import xyz.jmatt.models.SimpleResult;
import xyz.jmatt.services.CreateAccountService;

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

    private final String ERROR_EMPTY_FIELDS = "ERROR: Please fill out all fields before proceeding";
    private final String ERROR_DIFFERENT_PASSWORDS = "ERROR: Passwords did not match";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SlideTransition(CreateAccountPane);
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
            onCreateAccountResult(result);
        });
    }

    /**
     * Called after the create account thread finishes
     * @param result the result of the account creation process
     */
    private void onCreateAccountResult(SimpleResult result) {
        createAccountBtn.setDisable(false); //re-enable button
        if(!result.isError()) {
            //account was made TODO
            setMessage("account created");
        } else {
            setMessage(result.getMessage());
        }
    }

    private void SlideTransition(Node e)
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
