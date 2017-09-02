package xyz.jmatt.globals;

import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;
import xyz.jmatt.Main;

import java.io.IOException;

public final class Globals {
    public static boolean NeedsTransition = false;
    public static void SlideTransition(Node e, double FromX, double ToX, String sceneName, int width, int height)
    {
        TranslateTransition x = new TranslateTransition(new Duration(500), e);
        x.setFromX(FromX);
        x.setToX(ToX);
        x.setCycleCount(1);
        if(sceneName != null) {
            x.setOnFinished(event -> {
                try {
                    Main.changeScene(sceneName, width, height);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            });
        }
        x.play();
    }
}
