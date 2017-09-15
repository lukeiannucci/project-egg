package xyz.jmatt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.RadialGradient;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class Main extends Application {
    public static Stage stage;
    //public static void main(String args[]) {
        //launch(args);
    //}

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = FXMLLoader.load(getClass().getResource("/xyz/jmatt/login/Login.fxml"));
        stage  = primaryStage;
        primaryStage.setTitle("Banknotes");
        Scene scene = new Scene(root, 600, 450);
        Font.loadFont(new File("/font/Roboto-Thin.tff").toURI().toURL().toExternalForm(), 10);
        Font.loadFont(new File("/font/Roboto-Black.tff").toURI().toURL().toExternalForm(), 10);
        Font.loadFont(new File("/font/Roboto-Medium.tff").toURI().toURL().toExternalForm(), 10);
        scene.getStylesheets().add("xyz/jmatt/login/Login.css");
        stage.getIcons().add(new Image("/img/icon.png"));
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setMinHeight(primaryStage.getHeight());
        primaryStage.setMinWidth(primaryStage.getWidth());
    }

    //TODO pass is window dimensions to set the scene size
    public static void changeScene(String sceneName, int width, int height) throws IOException {
        Parent scene = FXMLLoader.load(Main.class.getResource(sceneName));
        scene.getStylesheets().add(sceneName.substring(0, sceneName.length() - 5) + ".css"); //will fix
        stage.setScene(new Scene(scene, width, height, Color.valueOf("#666666")));
    }

    public static void launchMain() throws IOException {
        Parent scene = FXMLLoader.load(Main.class.getResource("/xyz/jmatt/MainForm/MainForm.fxml"));
//        scene.getStylesheets().add();
        stage.setScene(new Scene(scene));
//        stage.setMaximized(true);
    }
}
