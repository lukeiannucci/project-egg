package xyz.jmatt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

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
//        Font.loadFont(getClass().getResource("res/font/Roboto-Thin.ttf").toExternalForm(), 10);
        scene.getStylesheets().add("xyz/jmatt/login/Login.css");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setMinHeight(primaryStage.getHeight());
        primaryStage.setMinWidth(primaryStage.getWidth());
    }

    //for now just hardcode change to create account scene change will come back and fix later
    public static void changeScene(String sceneName, int width, int height) throws IOException {
        Parent scene = FXMLLoader.load(Main.class.getResource(sceneName));
        scene.getStylesheets().add(sceneName.substring(0, sceneName.length() - 5) + ".css"); //will fix
        stage.setScene(new Scene(scene, width, height, Color.valueOf("#666666")));
    }
}
