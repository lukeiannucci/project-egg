package com.jordanluke.egg;

import android.app.Activity;
import android.os.Bundle;

import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.jordanluke.R;

/**
 * Created by Luke on 12/21/2016.
 *

public class Animation extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);

        //ImageView img_animation = (ImageView) findViewById(R.id.img_animation);

        TranslateAnimation animation = new TranslateAnimation(0.0f, 400.0f,
                0.0f, 0.0f);
        animation.setDuration(5000);
        animation.setRepeatCount(5);
        animation.setRepeatMode(2);
        animation.setFillAfter(true);
        //img_animation.startAnimation(animation);
}
*/