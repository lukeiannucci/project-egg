package com.jordanluke.egg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.jordanluke.R;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by lukei on 12/26/2016.
 */


public class PointAnimation {
    int x_pointAnimationStart;
    int y_pointAnimationStart;
    int y_old = 0;
    int x_dir = 1;
    int x_old = 0;
    int y_dir = -10; //variables to move animations x and y
    int y_count = 0;
    BigDecimal eggsPerSec = new BigDecimal("0.0");
    double sf = 0;
    Bitmap bitImage;
    int counter = 0;

    public PointAnimation(double scaleFactor) {
        int randomStartX = (int)(Math.random() * (int)(550 * scaleFactor) + (int)(200 * scaleFactor));
        int randomStartY = (int)(Math.random() * (int)(800 * scaleFactor) + (int)(700 * scaleFactor));
        x_pointAnimationStart = randomStartX * (int)scaleFactor;
        y_pointAnimationStart = randomStartY * (int)scaleFactor;
        y_old = y_pointAnimationStart;
        sf = scaleFactor;
    }

    public void getEggsPerSec(BigDecimal eps){
        eggsPerSec = eps;
    }

    public void setBitMapImageAndCount(Bitmap image, int count){
        bitImage = image;
        counter = count;
    }

    public int getXPos() {
        x_pointAnimationStart = x_pointAnimationStart + x_dir;
        if(x_dir >= x_old && x_dir <= 10){
            x_old = x_dir;
            x_dir++;
        }
        else {
            x_old = x_dir;
            if(x_dir == -10){
                x_dir++;
            } else {
                x_dir--;
            }
        }
        return x_pointAnimationStart;
    }
    public int getYPos() {
        y_pointAnimationStart = y_pointAnimationStart + y_dir;
        y_count = y_old - y_pointAnimationStart;
        if(y_count >= (sf *270)) {
            y_dir = -25;
        }
        return y_pointAnimationStart;
    }
}

