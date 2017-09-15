package xyz.jmatt.login;

import javafx.application.Platform;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import xyz.jmatt.globals.Globals;
import xyz.jmatt.Strings;
import xyz.jmatt.models.ClientSingleton;
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
    @FXML
    private Pane SlidePane;

    /**
     * Attempts to log in the user with the given credentials
     */
    @FXML
    private void login() {
        //Make sure all fields were filled out
        if(usernameField.getText().equals("")) {
            addHighlightStyle(usernameField);
            setMessage(Strings.ERROR_EMPTY_FIELD);
            return;
        }
        if(passwordField.getText().equals("")) {
            addHighlightStyle(passwordField);
            setMessage(Strings.ERROR_EMPTY_FIELD);
            return;
        }

        //proceed with login
        setMessage("logging in...");
        loginBtn.setDisable(true);
        //do in separate thread
        Platform.runLater(() -> {
            LoginService loginService = new LoginService();
            SimpleResult result = loginService.login(usernameField.getText(), passwordField.getText().toCharArray());
            try {
                onLoginResult(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
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

    private void onLoginResult(SimpleResult result) throws IOException {
        loginBtn.setDisable(false);
        if (!result.isError()) {
            //logged in TODO
            System.out.println(ClientSingleton.getINSTANCE().getUserId());
            System.out.println(ClientSingleton.getINSTANCE().getDbKey());
            Main.launchMain();
        } else {
            setMessage(result.getMessage());
            passwordField.clear();
        }
    }

    //@FXML
    //private BorderPane CreateAccountPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(Globals.NeedsTransition){
            Globals.SlideTransition(SlidePane, -(Main.stage.getWidth()), 0, null, 0, 0);
            Globals.NeedsTransition = false;
        }
        LoginPane.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER) {
                login();
            }
        });
        usernameField.textProperty().addListener((observable -> {
            removeHighlightStyle(usernameField);
        }));
        passwordField.textProperty().addListener((observable -> {
            removeHighlightStyle(passwordField);
        }));
    }

    private void trans(Node e) throws Exception
    {
        TranslateTransition x = new TranslateTransition(new Duration(500), e);
        x.setFromX(0);
        x.setToX(-Main.stage.getWidth());
        x.setCycleCount(1);
        //FadeTransition x = new FadeTransition(new Duration(500), e);
        //x.setFromValue(100);
        //x.setToValue(0);
        //x.setCycleCount(1);
        x.setOnFinished(event -> {
            try {
                    Main.changeScene("/xyz/jmatt/createaccount/CreateAccount.fxml", 600, 625);
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
           Globals.SlideTransition(SlidePane,0, -(Main.stage.getWidth()),"/xyz/jmatt/createaccount/CreateAccount.fxml", 600, 625);
           //Main.changeScene("/xyz/jmatt/createaccount/CreateAccount.fxml", 600, 625);
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
