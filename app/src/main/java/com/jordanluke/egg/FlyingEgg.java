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

public class FlyingEgg extends SurfaceView implements Runnable{
    int randomSize;
    Canvas canvas;
    SurfaceHolder ourHolder;

    Bitmap eggAnimation;
    int x_eggAnimationStart;
    int y_eggAnimationStart;
    int x_dir = 0;
    int y_dir = 12; //variables to move animations x and y

    public FlyingEgg(double scaleFactor, Context context) {
        super(context);
        int randomStart = (int)(Math.random() * (1080 * scaleFactor) - 150);
        randomSize = (int)(Math.random() * 200 + 50);
        x_eggAnimationStart = randomStart * (int)scaleFactor;
        y_eggAnimationStart = -200 * (int)scaleFactor;
        eggAnimation = BitmapFactory.decodeResource(context.getResources(), R.drawable.flying_egg);
        eggAnimation = Bitmap.createScaledBitmap(eggAnimation, (int) (randomSize * scaleFactor), (int) (randomSize * scaleFactor), false);
    }

    public void getSurfaceHolder(SurfaceHolder sv, Canvas c){
        ourHolder = sv;
        canvas = c;
    }

    @Override
    public void run() {
        if(ourHolder.getSurface().isValid()) { //idk what this does but some tutorial said to do it
                x_eggAnimationStart = x_eggAnimationStart + x_dir;
                y_eggAnimationStart = y_eggAnimationStart + y_dir;
                canvas.drawBitmap(eggAnimation, x_eggAnimationStart, y_eggAnimationStart, null);
        }
    }
}
