package xyz.jmatt.createaccount;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import xyz.jmatt.Main;
import xyz.jmatt.Strings;
import xyz.jmatt.models.ClientSingleton;
import xyz.jmatt.models.SimpleResult;
import xyz.jmatt.services.CreateAccountService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CreateAccountController implements Initializable{
    @FXML
    private Button createAccountBtn;
    @FXML
    private TextField nameField;
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
    private Parent scene;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SlideTransitionEnter(CreateAccountPane);
        CreateAccountPane.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER) {
                createAccount();
            }
        });
        usernameField.textProperty().addListener((observable -> {
            removeHighlightStyle(usernameField);
        }));
        passwordField.textProperty().addListener((observable -> {
            removeHighlightStyle(passwordField);
            removeHighlightStyle(passwordConfirmField);
        }));
        passwordConfirmField.textProperty().addListener((observable -> {
            removeHighlightStyle(passwordField);
            removeHighlightStyle(passwordConfirmField);
        }));
    }

    /**
     * Adds the highlighting style to the given textfield
     * @param field the field to highlight
     */
    private void addHighlightStyle(TextField field) {
        field.getStyleClass().add("field-error");
    }

    /**
     * Removes the highlighting style class from the given textfield
     * @param field the field to remove highlighting from
     */
    private void removeHighlightStyle(TextField field) {
        field.getStyleClass().remove("field-error");
    }

    @FXML
    private void KeyReleased()
    {

        if(!passwordField.getText().equals("") && !passwordConfirmField.getText().equals("")){
            if(!passwordField.getText().equals(passwordConfirmField.getText()))
            {
                setMessage(Strings.ERROR_BAD_PASSWORD_CONFIRM);
                if(passwordField.getStyleClass().contains("correct-password")){
                    passwordField.getStyleClass().remove("correct-password");
                    passwordConfirmField.getStyleClass().remove("correct-password");
                }
                passwordField.getStyleClass().add("incorrect-password");
                passwordConfirmField.getStyleClass().add("incorrect-password");
            }
            else if(passwordField.getText().equals(passwordConfirmField.getText()) && messageLabel.getText().equals(Strings.ERROR_BAD_PASSWORD_CONFIRM)){
                setMessage("");
                passwordField.getStyleClass().remove("incorrect-password");
                passwordField.getStyleClass().add("correct-password");
                passwordConfirmField.getStyleClass().remove("incorrect-password");
                passwordConfirmField.getStyleClass().add("correct-password");
            }
        }
        //TODO for some reason it still displays a red x even when i when there is no text, have to click backspace delete nothing, not sure why
        else if(passwordConfirmField.getText().equals("") && passwordConfirmField.getStyleClass().contains("correct-password")){
            passwordField.getStyleClass().remove("correct-password");
            passwordConfirmField.getStyleClass().remove("correct-password");
        }
        else if(passwordConfirmField.getText().equals("") && passwordConfirmField.getStyleClass().contains("incorrect-password")){
            passwordField.getStyleClass().remove("incorrect-password");
            passwordConfirmField.getStyleClass().remove("incorrect-password");
            passwordField.getStyleClass().add("no-icon");
            passwordConfirmField.getStyleClass().add("no-icon");
        }
        if(usernameField.getText().equals("")){
            return;
        }
        if(!usernameField.getText().matches("^([a-zA-Z]|\\d)+$")) {
            addHighlightStyle(usernameField);
            setMessage(Strings.ERROR_BAD_USERNAME);
        } else if(messageLabel.getText().equals(Strings.ERROR_BAD_USERNAME)) {
            setMessage("");
        }
    }
    /*
     * Attempts to create a new account with the information given
     */
    @FXML
    public void createAccount() {
        // Make sure all fields were filled out
        if(nameField.getText().equals("")) {
            addHighlightStyle(nameField);
            setMessage(Strings.ERROR_EMPTY_FIELD);
            return;
        }
        if(usernameField.getText().equals("")) {
            addHighlightStyle(usernameField);
            setMessage(Strings.ERROR_EMPTY_FIELD);
            return;
        }
        if(!usernameField.getText().matches("^([a-zA-Z]|\\d)+$")) {
            addHighlightStyle(usernameField);
            setMessage(Strings.ERROR_BAD_USERNAME);
            return;
        }
        if(passwordField.getText().equals("")) {
            addHighlightStyle(passwordField);
            setMessage(Strings.ERROR_EMPTY_FIELD);
            return;
        }
        if(passwordConfirmField.getText().equals("")) {
            addHighlightStyle(passwordConfirmField);
            setMessage(Strings.ERROR_EMPTY_FIELD);
            return;
        }
        // Make sure confirm passwords match
        if(!passwordField.getText().equals(passwordConfirmField.getText())) {
            setMessage(Strings.ERROR_BAD_PASSWORD_CONFIRM);
            addHighlightStyle(passwordField);
            addHighlightStyle(passwordConfirmField);
            return;
        }

        //proceed with creating a new account
        setMessage("creating account...");
        createAccountBtn.setDisable(true); //disable the button until it's done to prevent duplicates
        //do in separate thread
        Platform.runLater(() -> {
            //send the user's info the create account service for processing
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
            System.out.println(ClientSingleton.getINSTANCE().getUserId());
            System.out.println(ClientSingleton.getINSTANCE().getDbKey());
            Main.changeScene("/xyz/jmatt/MainForm/MainForm.fxml", 1200, 900);
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
        x.setToX(Main.stage.getWidth());
        x.setCycleCount(1);
        x.setOnFinished(event -> {
            try {
                Main.changeScene("/xyz/jmatt/login/Login.fxml", (int)Main.stage.getWidth(), (int)Main.stage.getHeight());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        x.play();
    }

    private void SlideTransitionEnter(Node e)
    {
        TranslateTransition x = new TranslateTransition(new Duration(500), e);
        x.setFromX(Main.stage.getWidth());
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
