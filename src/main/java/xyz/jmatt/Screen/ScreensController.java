package xyz.jmatt.Screen;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.util.Duration;
import xyz.jmatt.Main;

import java.io.File;
import java.util.HashMap;

public class ScreensController extends BorderPane{
    private HashMap<String, Node> screens = new HashMap<>();

    public ScreensController() {
        super();
    }

    //Add the screen to the collection
    public void addScreen(String name, Node screen) {
        screens.put(name, screen);
    }

    //Returns the Node with the appropriate name
    public Node getScreen(String name) {
        return screens.get(name);
    }

    public boolean loadLoginScreen(String name, String resource) {
        try {
            FXMLLoader root = new FXMLLoader(getClass().getResource("/xyz/jmatt/login/Login.fxml"));
            //Font.loadFont(new File("/font/Roboto-Thin.tff").toURI().toURL().toExternalForm(), 10);
            //Font.loadFont(new File("/font/Roboto-Black.tff").toURI().toURL().toExternalForm(), 10);
            //Font.loadFont(new File("/font/Roboto-Medium.tff").toURI().toURL().toExternalForm(), 10);
            //getStylesheets().add("xyz/jmatt/login/Login.css");
            //Main.stage.getIcons().add(new Image("/img/icon.png"));
            Parent loadScreen = (Parent)root.load();
            loadScreen.getStylesheets().add("/xyz/jmatt/login/Login.css");
            ControlledScreen myScreenController = ((ControlledScreen)root.getController());
            myScreenController.setScreenParent(this);
            addScreen(name, loadScreen);

            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean setScreen(final String name) {
        if (screens.get(name) != null) {   //screen loaded
            final DoubleProperty opacity = opacityProperty();

            if (!getChildren().isEmpty()) {    //if there is more than one screen
                Timeline fade = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1.0)),
                        new KeyFrame(new Duration(1000), new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent t) {
                                getChildren().remove(0);                    //remove the displayed screen
                                getChildren().add(0, screens.get(name));     //add the screen
                                Timeline fadeIn = new Timeline(
                                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                                        new KeyFrame(new Duration(800), new KeyValue(opacity, 1.0)));
                                fadeIn.play();
                            }
                        }, new KeyValue(opacity, 0.0)));
                fade.play();

            } else {
                setOpacity(0.0);
                getChildren().add(screens.get(name));       //no one else been displayed, then just show
                Timeline fadeIn = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                        new KeyFrame(new Duration(2500), new KeyValue(opacity, 1.0)));
                fadeIn.play();
            }
            return true;
        } else {
            System.out.println("screen hasn't been loaded!!! \n");
            return false;
        }
    }
}
