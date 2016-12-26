package com.jordanluke.egg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.jordanluke.R;

/**
 * Created by lukei on 12/26/2016.
 */

public class PointAnimation extends SurfaceView implements Runnable{
    int pointSize;
    Canvas canvas;
    SurfaceHolder ourHolder;

    Bitmap pointAnimation;
    int x_pointAnimationStart;
    int y_pointAnimationStart;
    int x_dir = 0;
    int y_dir = -4; //variables to move animations x and y

    public PointAnimation(double scaleFactor, Context context) {
        super(context);
        int randomStart = (int)(Math.random() * (int)(1300 * scaleFactor));
        pointSize = 120;
        x_pointAnimationStart = randomStart * (int)scaleFactor;
        y_pointAnimationStart = 1000 * (int)scaleFactor;
        pointAnimation = BitmapFactory.decodeResource(context.getResources(), R.drawable.number);
        pointAnimation = Bitmap.createScaledBitmap(pointAnimation, (int) (pointSize * scaleFactor), (int) (pointSize * scaleFactor), false);
    }

    public void getSurfaceHolder(SurfaceHolder sv, Canvas c){
        ourHolder = sv;
        canvas = c;
    }

    @Override
    public void run() {
        if(ourHolder.getSurface().isValid()) { //idk what this does but some tutorial said to do it
            x_pointAnimationStart = x_pointAnimationStart + x_dir;
            y_pointAnimationStart = y_pointAnimationStart + y_dir;
            canvas.drawBitmap(pointAnimation, x_pointAnimationStart, y_pointAnimationStart, null);
        }
    }
}

