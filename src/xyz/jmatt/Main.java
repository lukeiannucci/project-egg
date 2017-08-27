package xyz.jmatt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOError;
import java.io.IOException;

public class Main extends Application {
    static Stage stage;
    //public static void main(String args[]) {
        //launch(args);
    //}

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = FXMLLoader.load(getClass().getResource("/xyz/jmatt/login/Login.fxml"));
        stage  = primaryStage;
        primaryStage.setTitle("login");
        Scene scene = new Scene(root, 600, 450);
        scene.getStylesheets().add("xyz/jmatt/login/Login.css");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setMinHeight(primaryStage.getHeight());
        primaryStage.setMinWidth(primaryStage.getWidth());
    }
    //for now just hardcode change to create account scene change will come back and fix later
    public static void changeScene(String scene) throws IOException {
        Parent CreateAccountPane = FXMLLoader.load(Main.class.getResource("/xyz/jmatt/createaccount/CreateAccount.fxml"));
        stage.setScene(new Scene(CreateAccountPane, 600, 450));

    }
}
