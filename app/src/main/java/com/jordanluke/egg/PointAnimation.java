package com.jordanluke.egg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.jordanluke.R;

import java.math.BigInteger;

/**
 * Created by lukei on 12/26/2016.
 */

public class PointAnimation extends SurfaceView implements Runnable{
    int pointSize;
    Canvas canvas;
    SurfaceHolder ourHolder;
    Context c;

    Bitmap pointAnimation;
    int x_pointAnimationStart;
    int y_pointAnimationStart;
    int y_old = 0;
    int x_dir = 1;
    int x_old = 0;
    int y_dir = -10; //variables to move animations x and y
    int y_count = 0;
    BigInteger eggsPerSec = new BigInteger("0");
    double sf = 0;

    public PointAnimation(double scaleFactor, Context context) {
        super(context);
        int randomStartX = (int)(Math.random() * (int)(550 * scaleFactor) + (int)(200 * scaleFactor));
        int randomStartY = (int)(Math.random() * (int)(800 * scaleFactor) + (int)(700 * scaleFactor));
        pointSize = (int)(60 * scaleFactor);
        x_pointAnimationStart = randomStartX * (int)scaleFactor;
        y_pointAnimationStart = randomStartY * (int)scaleFactor;
        y_old = y_pointAnimationStart;
        sf =scaleFactor;
        c = context;


    }

    public void getSurfaceHolder(SurfaceHolder sv, Canvas c){
        ourHolder = sv;
        canvas = c;
    }

    public void getEggsPerSec(BigInteger eps){
        eggsPerSec = eps;
    }

    @Override
    public void run() {
        if(ourHolder.getSurface().isValid()) { //idk what this does but some tutorial said to do it
            x_pointAnimationStart = x_pointAnimationStart + x_dir;
            if(x_dir >= x_old && x_dir <= 10){
                x_old = x_dir;
                x_dir++;

            } else {
                x_old = x_dir;
                if(x_dir == -10){
                    x_dir++;
                } else {
                    x_dir--;
                }
            }

            y_pointAnimationStart = y_pointAnimationStart + y_dir;
            y_count = y_old - y_pointAnimationStart;
            if(y_count >= 270) {
                y_dir = -30;
            }
            if(eggsPerSec.intValue() <= 3 ) {
                pointAnimation = BitmapFactory.decodeResource(c.getResources(), R.drawable.number);
                pointAnimation = Bitmap.createScaledBitmap(pointAnimation, (int) (pointSize * sf), (int) (pointSize * sf), false);
                canvas.drawBitmap(pointAnimation, x_pointAnimationStart, y_pointAnimationStart, null);
            } else {
                pointAnimation = BitmapFactory.decodeResource(c.getResources(), R.drawable.number2);
                pointAnimation = Bitmap.createScaledBitmap(pointAnimation, (int) (pointSize * sf), (int) (pointSize * sf), false);
                canvas.drawBitmap(pointAnimation, x_pointAnimationStart, y_pointAnimationStart, null);
            }
        }
    }
}

