package xyz.jmatt.createaccount;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import xyz.jmatt.models.SimpleResult;
import xyz.jmatt.services.CreateAccountService;

public class CreateAccountController {
    @FXML
    private Button createAccountBtn;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField passwordConfirmField;
    @FXML
    private Label errorMessageLabel;

    private final String ERROR_EMPTY_FIELDS = "ERROR: Please fill out all fields before proceeding";
    private final String ERROR_DIFFERENT_PASSWORDS = "ERROR: Passwords did not match";

    /*
     * Attempts to create a new account with the information given
     */
    @FXML
    public void createAccount() {
        // Make sure all fields were filled out
        if(usernameField.getText().equals("")
            || passwordField.getText().equals("")
            || passwordConfirmField.getText().equals("")) {
            setErrorMessage(ERROR_EMPTY_FIELDS);
            return;
        }
        // Make sure confirm passwords match
        if(!passwordField.getText().equals(passwordConfirmField.getText())) {
            setErrorMessage(ERROR_DIFFERENT_PASSWORDS);
            return;
        }

        //proceed with creating a new account
        //do in separate thread
        Platform.runLater(() -> {
            createAccountBtn.setDisable(true); //disable the button until it's done to prevent duplicates
            CreateAccountService accountService = new CreateAccountService();
            SimpleResult result = accountService.createAccount(usernameField.getText(), passwordField.getText().toCharArray());
            handleCreateAccountResult(result);
        });
    }

    /**
     * Called after the create account thread finishes
     * @param result
     */
    private void handleCreateAccountResult(SimpleResult result) {
        createAccountBtn.setDisable(false); //re-enable button
        if(!result.isError()) {
            //account was made TODO
            System.out.println("account created");
        } else {
            setErrorMessage(result.getMessage());
        }
    }

    /**
     * Sets the on screen error message label to the given message
     * @param message The message to display to the user
     */
    private void setErrorMessage(String message) {
        errorMessageLabel.setText(message);
    }
}
