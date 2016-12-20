package com.jordanluke.egg;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.os.Vibrator;

import com.jordanluke.R;
import com.jordanluke.egg.BigNumber;

/**
 * Created by Luke on 12/19/2016.
 */

public class MainActivity extends AppCompatActivity{
    BigNumber eggCount = new BigNumber();
    Vibrator phoneVibrate;
    int egg_count = 1;
    int multiplier = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);
        phoneVibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE); //initialize vibrator
        update(); //initial call to update function
    }

    protected void onButtonClick(View egg) {
        if(egg.getId() == R.id.golden_egg) {


            //eggCount.add(1); //increment number
            eggCount.add(egg_count);
            egg_count *= multiplier;
            update(); //update count TextView
            phoneVibrate.vibrate(30); //vibrate phone
        }
    }

    public void update() {
        TextView eggCountDisplay = (TextView) findViewById(R.id.eggCountTextView);
        eggCountDisplay.setText(eggCount.toString() + " eggs");
    }
}
