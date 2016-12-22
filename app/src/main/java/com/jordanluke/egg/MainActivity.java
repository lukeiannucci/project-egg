package com.jordanluke.egg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.os.Vibrator;
import android.view.WindowManager;

import com.jordanluke.R;

import java.math.BigInteger;

/**
 * My thoughts are that we can delete the BigNumber class and implement everything in the Main Activity
 * Not sure how you want the counter to work but we can implement that pretty easily
 */

/**
 * BIG OVERHAUL - I was starting work on the eggs that float down in the background but I was running into some serious
 * performance problems already by trying create and draw a new ImageView during each frame so I restructured how the whole thing works.
 * I looked at a lot of game stuff online and this is supposed to be a bit cloer to how bigger game engines work.
 *
 * Instead of doing everything inside of one update() function, I split it up into 2, update() and draw(), where update() does all the
 * calculations of values and positions of stuff and then draw() just puts everything on screen.  I think this will help make things easier
 * in the long run.
 *
 * I also placed everything inside of a big loop that will run until the game is paused or the app is closed.  Each time the loop runs is a new
 * frame, so we can use the current time at the beginning and end of the loop on calculate the time it takes for each frame to display
 * so we can get a simple FPS counter to monitor performance as we add more and more stuff.
 *
 * There's also some pause() & resume() functions and some stuff with threads that I mostly just copied them from some tutorial so idk what
 * they do but I think they're important lmao.
 *
 * All of the graphics are handled by a Canvas now, which is what most people seem to be using for stuff like this and should give better
 * performance than trying to drag around a bunch of TextViews and ImageViews.
 *
 * Also the old stuff is still commented out so the counter isn't doing anything currently.  I'll try to fix it tomorrow
 */

public class MainActivity extends AppCompatActivity{
    Game theGame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        theGame = new Game(this);
        setContentView(theGame);
    }
    Context context = this;
    Display display;



    class Game extends SurfaceView implements Runnable {
        Thread gameThread = null;
        SurfaceHolder ourHolder;

        volatile boolean playing;

        Canvas canvas;
        Paint paint;

        long fps; //fps counter
        private long timeThisFrame; //current frame time

        Bitmap mainEggGraphic;

        Bitmap eggAnimation;

        Vibrator phoneVibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE); //initialize vibrator

        WindowManager wm = ((WindowManager)context.getSystemService(context.WINDOW_SERVICE));
        BigInteger counter = new BigInteger("0"); //start at zero
        BigInteger addToCounter = new BigInteger("1"); //not this will change depending on how fast they are clicking
        Display display = wm.getDefaultDisplay();
        int screenWidthActual = display.getWidth();
        int screenHeightActual = display.getHeight();
        int screenWidthTarget = 1080;
        int screenHeightTarget = 1920;
        double scaleFactor = screenWidthActual * 1.0 / screenWidthTarget;

        int x_eggAnimationStart ;
        int y_eggAnimationStart;
        int x_dir, y_dir = 2; //variables to move animations x and y
        /**
         * Game constructor
         */
        public Game(Context context) {
            super(context);
            //initialize stuff
            ourHolder = getHolder();
            paint = new Paint();
            //initialize graphics
            mainEggGraphic = BitmapFactory.decodeResource(this.getResources(), R.drawable.main_egg_down); //get image file
            mainEggGraphic = Bitmap.createScaledBitmap(mainEggGraphic, (int)(850 * scaleFactor), (int)(850 * scaleFactor), false); //set size

            eggAnimation = BitmapFactory.decodeResource(this.getResources(), R.drawable.flying_egg);
            eggAnimation = Bitmap.createScaledBitmap(eggAnimation, (int)(200 * scaleFactor), (int)(200 * scaleFactor), false);

            x_eggAnimationStart = 23 * (int)scaleFactor;
            y_eggAnimationStart = 107 * (int)scaleFactor;

        }
        /**
         * Main Loop
         */
        @Override
        public void run() {
            while(playing) { //run until paused
                long startFrameTime = System.currentTimeMillis(); //each time the loop runs is one frame, so we record when it starts here
                update(); //calculations
                draw(); //redraw the screen
                timeThisFrame = System.currentTimeMillis() - startFrameTime; //get elapsed time after screen is updated
                if(timeThisFrame > 0) {
                    fps = 1000 / timeThisFrame; //update fps counter
                }
            }
        }

        /**
         * All calculations go here
         */
        public void update() {
            //todo
        }
        /**
         * This redraws everything on screen each frame
         */
        public void draw() {
            if(ourHolder.getSurface().isValid()) { //idk what this does but some tutorial said to do it
                BigInteger counterCheck = new BigInteger("0");
                canvas = ourHolder.lockCanvas(); //ready to draw

                canvas.drawColor(Color.argb(255, 255, 255, 255)); //white background color

                paint.setColor(Color.argb(255, 0, 0, 0)); //black text
                paint.setTextSize(50);
                canvas.drawText("FPS:" + fps, 20, 80, paint); //draw fps counter
                canvas.drawText(screenWidthActual + "x" + screenHeightActual, 20, 140, paint);
                canvas.drawText("scale factor = " + scaleFactor, 20, 200, paint);

                paint.setTextAlign(Paint.Align.CENTER);
                if(counter.toString().equals("1")) {
                    canvas.drawText(counter.toString() + " egg", (int) (540 * scaleFactor), 300, paint); //draw egg counter
                } else {
                    canvas.drawText(counter.toString() + " eggs", (int) (540 * scaleFactor), 300, paint); //draw egg counter

                }
                paint.setTextAlign(Paint.Align.LEFT);

                canvas.drawBitmap(mainEggGraphic, (int)(115 * scaleFactor), (int)(535 * scaleFactor), paint); //draw main egg

                if(!counterCheck.toString().equals(counter.toString())){

                    x_eggAnimationStart = x_eggAnimationStart + x_dir;
                    y_eggAnimationStart = y_eggAnimationStart + y_dir;


                    canvas.drawBitmap(eggAnimation, x_eggAnimationStart, y_eggAnimationStart, null); //draw main egg
                }

                ourHolder.unlockCanvasAndPost(canvas); //finalize
            }
        }

        /**
         * Thi function gets called if the screen is touched
         */
        @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {
            switch(motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    counter = counter.add(addToCounter);
                    phoneVibrate.vibrate(30); //vibrate phone
                    break;
            }
            return true;
        }
        public void pause() {
            playing = false;
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                Log.e("ERROR:", "uh oh boys");
            }
        }
        public void resume() {
            playing = true;
            gameThread = new Thread(this);
            gameThread.start();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        theGame.resume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        theGame.pause();
    }
    /**
     BigNumber eggCount = new BigNumber();
     Vibrator phoneVibrate;
     RelativeLayout.LayoutParams floatingEggParams;
     int egg_count = 0; //start at 0
     int MAX_INT = Integer.MAX_VALUE; //for testing purposes
     @Override
     protected void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     setContentView(R.layout.mainpage);

     phoneVibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE); //initialize vibrator

     floatingEggParams = new RelativeLayout.LayoutParams(200,200);
     floatingEggParams.addRule(RelativeLayout.CENTER_IN_PARENT);

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

     ImageView floatingEgg = new ImageView(this);
     floatingEgg.setLayoutParams(floatingEggParams);
     floatingEgg.setImageResource(R.drawable.main_egg_down);
     RelativeLayout myLayout = (RelativeLayout)findViewById(R.id.main_layout);
     myLayout.addView(floatingEgg);
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
     */
}
