package project_egg.project_egg;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import project_egg.project_egg.BigNumber;

/**
 * Created by Luke on 12/19/2016.
 */

public class MainActivity extends AppCompatActivity{
    BigNumber eggCount = new BigNumber();
    int egg_count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);
        update();
    }

    protected void onButtonClick(View egg) {
        if(egg.getId() == R.id.golden_egg) {
            eggCount.add(1);
            update();
        }
    }

    public void update() {
        TextView eggCountDisplay = (TextView) findViewById(R.id.eggCountTextView);
        eggCountDisplay.setText(eggCount.toString() + " eggs");
    }
}
