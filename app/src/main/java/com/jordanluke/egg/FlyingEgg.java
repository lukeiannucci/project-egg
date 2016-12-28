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

    int x_eggAnimationStart;
    int y_eggAnimationStart;
    int x_dir = 0;
    int y_dir = 12; //variables to move animations x and y
    int sizeId;

    public FlyingEgg(double scaleFactor, int size) {
        sizeId = size;
        int randomStart = (int)(Math.random() * (1080 * scaleFactor) - 150);
        randomSize = (int)(Math.random() * 200 + 50);
        x_eggAnimationStart = randomStart * (int)scaleFactor;
        y_eggAnimationStart = -200 * (int)scaleFactor;
    }

    public int getSizeId() {
        return sizeId;
    }

    public int getXPos() {
        x_eggAnimationStart = x_eggAnimationStart + x_dir;
        return x_eggAnimationStart;
    }
    public int getYPos() {
        y_eggAnimationStart = y_eggAnimationStart + y_dir;
        return y_eggAnimationStart;
    }
}
