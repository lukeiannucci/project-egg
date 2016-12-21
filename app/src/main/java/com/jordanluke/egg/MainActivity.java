package com.jordanluke.egg;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.os.Vibrator;

import com.jordanluke.R;
import com.jordanluke.egg.BigNumber;

import java.math.BigInteger;

/**
 * My thoughts are that we can delete the BigNumber class and implement everything in the Main Activity
 * Not sure how you want the counter to work but we can implement that pretty easily
 * test
 */

public class MainActivity extends AppCompatActivity{
    BigNumber eggCount = new BigNumber();
    Vibrator phoneVibrate;
    int egg_count = 0; //start at 0
    int MAX_INT = Integer.MAX_VALUE; //for testing purposes
    int hello= 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);
        phoneVibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE); //initialize vibrator
        update(); //initial call to update function
    }

    protected void onButtonClick(View egg) {
        if(egg.getId() == R.id.golden_egg) {
            //only do if egg count is g>= MAX_INT (testing purposes)
            if(egg_count >= MAX_INT) {
                //testing purposes
                BigInteger bigNumber = new BigInteger("1234567617181920");
                eggCount.addB(bigNumber);
            } else {
                eggCount.add(egg_count); //increment number
                egg_count = MAX_INT;
            }
            update(); //update count TextView
            phoneVibrate.vibrate(30); //vibrate phone
        }
    }

    public void update() {
        if (egg_count < MAX_INT) {
            //testing purposes
            TextView eggCountDisplay = (TextView) findViewById(R.id.eggCountTextView);
            eggCountDisplay.setText(eggCount.toString() + " eggs");
        } else {
            TextView eggCountDisplay = (TextView) findViewById(R.id.eggCountTextView);
            eggCountDisplay.setText(eggCount.largeCount.toString() + " eggs");
        }
    }
}
