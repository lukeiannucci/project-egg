package com.jordanluke.egg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.jordanluke.R;

/**
 * Created by Luke on 12/21/2016.
 */

public class FlyingEgg {
    int randomSize;

    Bitmap eggAnimation;
    int x_eggAnimationStart;
    int y_eggAnimationStart;
    int x_dir = 0;
    int y_dir = 12; //variables to move animations x and y

    public FlyingEgg(double scaleFactor, Bitmap image) {
        int randomStart = (int)(Math.random() * (1080 * scaleFactor) - 150);
        randomSize = (int)(Math.random() * 200 + 50);
        x_eggAnimationStart = randomStart * (int)scaleFactor;
        y_eggAnimationStart = -200 * (int)scaleFactor;
        eggAnimation = Bitmap.createScaledBitmap(image, (int) (randomSize * scaleFactor), (int) (randomSize * scaleFactor), false);
    }

    public int getXPos() {
        x_eggAnimationStart = x_eggAnimationStart + x_dir;
        return x_eggAnimationStart;
    }
    public int getYPos() {
        y_eggAnimationStart = y_eggAnimationStart + y_dir;
        return y_eggAnimationStart;
    }

    public Bitmap getSizedBitmap() {
        return eggAnimation;
    }
}
