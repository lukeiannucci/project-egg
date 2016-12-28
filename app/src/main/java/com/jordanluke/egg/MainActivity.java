package com.jordanluke.egg;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.os.Vibrator;
import android.view.Window;
import android.view.WindowManager;

import com.jordanluke.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * STUFF TO ADD
 *      +1s / +2s
 *      Egg type upgrades
 *      Save the number of eggs & current upgrades bought in between app closes
 *      Auto eggs (no tapping required)
 *      Get auto eggs while away
 */

public class MainActivity extends AppCompatActivity{
    Game theGame;
    BigInteger counter = new BigInteger("0");
    boolean secondPassed = false;
    long startFrameTime = 0;
    BigInteger startEggs = new BigInteger("0");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //load in the file
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String temp = sharedPref.getString("counter", "");
        if (!temp.equals("")){
            counter = new BigInteger(temp);
        }

        theGame = new Game(this);
        setContentView(theGame);
    }
    Context context = this;

    class Game extends SurfaceView implements Runnable {
        Thread gameThread = null;
        SurfaceHolder ourHolder;

        volatile boolean playing;

        Canvas canvas;
        Paint paint;

        long fps; //fps counter
        private long timeThisFrame; //current frame time
        String gamestate;

        Bitmap mainEggGraphic;
        Bitmap mainEggGraphicDown;
        List<Bitmap> mainEggFrames = new ArrayList<>();
        int mainEggFrame;
        int mainEggFrameCounter;

        Bitmap pointImage1;
        Bitmap pointImage2;
        Bitmap pointImage3;


        List<FlyingEgg> animationListBack = new ArrayList<>();
        List<FlyingEgg> animationListFront = new ArrayList<>();

        List<PointAnimation> pointAnimationList = new ArrayList<>();

        Bitmap menuButtonGraphic;
        Vibrator phoneVibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE); //initialize vibrator

        Bitmap flyingEggGraphic;

        WindowManager wm = ((WindowManager)context.getSystemService(context.WINDOW_SERVICE));
         //start at zero
        BigInteger addToCounter = new BigInteger("1"); //not this will change depending on how fast they are clicking
        Display display = wm.getDefaultDisplay();
        int screenWidthActual = display.getWidth();
        int screenHeightActual = display.getHeight();
        int screenWidthTarget = 1080;
        int screenHeightTarget = 1920;
        double scaleFactor = screenWidthActual * 1.0 / screenWidthTarget;

        int menuAnchor = 1920;
        double menuTransitionSpeed = 200;

        Typeface typeface_bold;
        Typeface typeface_regular;

        BigInteger eggsPerSecond = new BigInteger("0");

        /**
         * Game constructor
         */
        public Game(Context context) {
            super(context);
            //initialize stuff
            ourHolder = getHolder();
            paint = new Paint();
            gamestate = "main";

            typeface_regular= Typeface.create(paint.getTypeface(), Typeface.NORMAL);
            typeface_bold = Typeface.create(paint.getTypeface(), Typeface.BOLD);

            //initialize graphics
            mainEggGraphic = BitmapFactory.decodeResource(this.getResources(), R.drawable.main_egg_up); //get image file
            mainEggGraphic = Bitmap.createScaledBitmap(mainEggGraphic, (int)(1300 * scaleFactor), (int)(1300 * scaleFactor), false); //set size

            mainEggGraphicDown = BitmapFactory.decodeResource(this.getResources(), R.drawable.main_egg_down); //get image file
            mainEggGraphicDown = Bitmap.createScaledBitmap(mainEggGraphicDown, (int)(1290 * scaleFactor), (int)(1290 * scaleFactor), false); //set size

            mainEggFrames.add(mainEggGraphic);
            mainEggFrames.add(mainEggGraphicDown);
            mainEggFrame = 0;
            mainEggFrameCounter = 0;

            menuButtonGraphic = BitmapFactory.decodeResource(this.getResources(), R.drawable.menu_button); //get image file
            menuButtonGraphic = Bitmap.createScaledBitmap(menuButtonGraphic, (int)(256 * scaleFactor), (int)(256 * scaleFactor), false); //set size

            pointImage1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.number); //get image file
            pointImage1 = Bitmap.createScaledBitmap(pointImage1, (int)(60 * scaleFactor), (int)(60 * scaleFactor), false);

            pointImage2 = BitmapFactory.decodeResource(this.getResources(), R.drawable.number2); //get image file
            pointImage2 = Bitmap.createScaledBitmap(pointImage2, (int)(60 * scaleFactor), (int)(60 * scaleFactor), false);

            pointImage3 = BitmapFactory.decodeResource(this.getResources(), R.drawable.number3); //get image file
            pointImage3 = Bitmap.createScaledBitmap(pointImage3, (int)(60 * scaleFactor), (int)(60 * scaleFactor), false);

            flyingEggGraphic = BitmapFactory.decodeResource(this.getResources(), R.drawable.flying_egg);

        }
        /**
         * Main Loop
         */
        @Override
        public void run() {
            while(playing) { //run until paused
                if(secondPassed == false) {
                    startFrameTime = System.currentTimeMillis(); //each time the loop runs is one frame, so we record when it starts here
                    startEggs = counter;
                    secondPassed = true;
                }
                long testFrameTime = System.currentTimeMillis();
                long isSecond = testFrameTime - startFrameTime;

                if(isSecond >= 1000){
                    //BigInteger endEggs = counter;
                    eggsPerSecond = (counter.subtract(startEggs));
                    secondPassed = false;
                }
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
            if (ourHolder.getSurface().isValid()) { //idk what this does but some tutorial said to do it
                canvas = ourHolder.lockCanvas(); //ready to draw

                canvas.drawColor(Color.argb(255, 255, 243, 196)); //background color

                if(gamestate.equals("main")) {
                    drawMainScreen();
                } else if(gamestate.equals("menu")) {
                    drawMenuScreen(menuAnchor);
                } else if(gamestate.equals("mainToMenu")) {
                    drawMainToMenuScreen();
                } else if(gamestate.equals("menuToMain")) {
                    drawMenuToMainScreen();
                }

                //debug stuff always on top
                paint.setColor(Color.argb(255, 0, 0, 0)); //black text
                paint.setTextSize((int)(37 * scaleFactor));
                canvas.drawText("FPS:" + fps, 20, 80, paint); //draw fps counter
                canvas.drawText(screenWidthActual + "x" + screenHeightActual, 20, 140, paint);
                canvas.drawText("scale factor = " + scaleFactor, 20, 200, paint);
                canvas.drawText("gamestate = " + gamestate, 20, 260, paint);

                ourHolder.unlockCanvasAndPost(canvas); //finalize
            }
        }

        public void drawMainScreen() {
            //create an list of flying eggs to remove
            List<FlyingEgg> itemsToRemove = new ArrayList<>();
            List<PointAnimation> pointsToRemove = new ArrayList<>();

            //loop through and run each animation until it reaches the bottom of the screen
            for (int i = 0; i < animationListBack.size(); i++) {
                canvas.drawBitmap(animationListBack.get(i).getSizedBitmap(), animationListBack.get(i).getXPos(), animationListBack.get(i).getYPos(), paint);

                //check if it is at the bottom, if so store it into our store list
                if (animationListBack.get(i).y_eggAnimationStart >= screenHeightActual) {
                    itemsToRemove.add(animationListBack.get(i));
                }
            }

            //remove the animations at the bottom
            animationListBack.removeAll(itemsToRemove);

            //free up our list
            itemsToRemove.clear();

            if(mainEggFrameCounter >= 1) {
                mainEggFrameCounter--;
                canvas.drawBitmap(mainEggFrames.get(1), (int) (-110 * scaleFactor), (int) (500 * scaleFactor), paint); //draw main egg
            } else {
                canvas.drawBitmap(mainEggFrames.get(0), (int) (-110 * scaleFactor), (int) (500 * scaleFactor), paint); //draw main egg
            }

            //loop through and run our front animations until it reaches the bottom of the screen
            for (int i = 0; i < animationListFront.size(); i++) {
                canvas.drawBitmap(animationListFront.get(i).getSizedBitmap(), animationListFront.get(i).getXPos(), animationListFront.get(i).getYPos(), paint);

                //check if it is at the bottom, if so store it into our remove list
                if (animationListFront.get(i).y_eggAnimationStart >= screenHeightActual) {
                    itemsToRemove.add(animationListFront.get(i));
                }

            }

            //remove the animations at the bottom
            animationListFront.removeAll(itemsToRemove);

            //free up our list
            itemsToRemove.clear();

            //loop through and run each animation until it reaches the bottom of the screen
            for (int i = 0; i < pointAnimationList.size(); i++) {
                if(eggsPerSecond.intValue() <= 3) {
                    canvas.drawBitmap(pointImage1, pointAnimationList.get(i).getXPos(), pointAnimationList.get(i).getYPos(), paint);
                }
                else if (eggsPerSecond.intValue() <= 6) {
                    canvas.drawBitmap(pointImage2, pointAnimationList.get(i).getXPos(), pointAnimationList.get(i).getYPos(), paint);
                }
                else {
                    canvas.drawBitmap(pointImage3, pointAnimationList.get(i).getXPos(), pointAnimationList.get(i).getYPos(), paint);

                }

                //check if it is at the bottom, if so store it into our store list
                if (pointAnimationList.get(i).y_pointAnimationStart <= -100) {
                    pointsToRemove.add(pointAnimationList.get(i));
                }
            }

            pointAnimationList.removeAll(pointsToRemove);

            pointsToRemove.clear();

            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTypeface(typeface_bold);
            paint.setARGB(255, 50, 50, 50);
            paint.setTextSize((int)(150 * scaleFactor));
            if (counter.toString().equals("1")) {
                canvas.drawText(counter.toString() + " egg", (int) (545 * scaleFactor), (int)(375 * scaleFactor), paint); //draw egg counter
                paint.setARGB(255, 255, 255, 255);
                canvas.drawText(counter.toString() + " egg", (int) (540 * scaleFactor), (int)(370 * scaleFactor), paint); //draw egg counter
            } else {
                canvas.drawText(counter.toString() + " eggs", (int) (545 * scaleFactor), (int)(375 * scaleFactor), paint); //draw egg counter
                paint.setARGB(255, 255, 255, 255);
                canvas.drawText(counter.toString() + " eggs", (int) (540 * scaleFactor), (int)(370 * scaleFactor), paint); //draw egg counter
            }
            paint.setTypeface(typeface_regular);
            paint.setARGB(255, 0, 0, 0);
            paint.setTextSize((int)(45 * scaleFactor));
            canvas.drawText(eggsPerSecond.intValue() + " eggs/sec", (int)(542 * scaleFactor), (int)(502 * scaleFactor), paint);
            paint.setARGB(255, 255, 255, 255);
            canvas.drawText(eggsPerSecond.intValue() + " eggs/sec", (int)(540 * scaleFactor), (int)(500 * scaleFactor), paint);
            paint.setTextSize((int)(37 * scaleFactor));
            paint.setTextAlign(Paint.Align.LEFT);
            canvas.drawBitmap(menuButtonGraphic, (int) (35 * scaleFactor), (int) (1629 * scaleFactor), paint);

            //canvas.drawRect((int)(scaleFactor * 150), (int)(scaleFactor * 620), (int)(930* scaleFactor), (int)(1675 * scaleFactor), paint); //egg touch target
        }

        public void drawMenuScreen(int anchorPoint) {
            paint.setARGB(255, 255, 255, 255);
            canvas.drawRect(0, (int)(scaleFactor * anchorPoint), (int)(1080 * scaleFactor), (int)(1920 * scaleFactor) + (int)(scaleFactor * anchorPoint), paint);
            paint.setARGB(255, 0, 0, 0);
            //canvas.drawText("0x" + 0 + (int)(scaleFactor * anchorPoint) + "x" + (int)(1080 * scaleFactor) + "x" +  ((int)(1920 * scaleFactor) + (int)(scaleFactor)), 300, 300, paint);
            canvas.drawText("Menu", (int)(540 * scaleFactor), (int)(300 * scaleFactor) + (int)(scaleFactor * anchorPoint), paint);
            canvas.drawText("item_1", (int)(540 * scaleFactor), (int)(400 * scaleFactor) + (int)(scaleFactor * anchorPoint), paint);
            canvas.drawText("item_2", (int)(540 * scaleFactor), (int)(480 * scaleFactor) + (int)(scaleFactor * anchorPoint), paint);
            canvas.drawText("item_3", (int)(540 * scaleFactor), (int)(560 * scaleFactor) + (int)(scaleFactor * anchorPoint), paint);
        }

        //add in number of frames failsafe for menu transition

        public void drawMainToMenuScreen() {
            drawMainScreen(); //continue to draw the main screen until the menu is completely covering it
            drawMenuScreen(menuAnchor);
            if(menuAnchor > 0) {
                menuAnchor-=menuTransitionSpeed;
                menuTransitionSpeed/=1.1;
            } else {
                menuAnchor = 0; //ensure the final value is correct
                gamestate = "menu"; //switch to only drawing menu
            }
        }

        public void drawMenuToMainScreen() {
            drawMainScreen(); //main screen on bottom
            drawMenuScreen(menuAnchor);
            if(menuAnchor < 1920) {
                menuAnchor+=menuTransitionSpeed;
                menuTransitionSpeed/=1.1;
            } else {
                menuAnchor = 1920; //ensure the final value is correct
                gamestate = "main"; //switch to only drawing menu
            }
        }

        /**
         * This function gets called if the screen is touched
         */
        @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {
            switch(motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    if(gamestate.equals("main")) {
                        //Touched main egg
                        if (motionEvent.getX() < (int) (930 * scaleFactor)
                                && motionEvent.getX() > (int) (150 * scaleFactor)
                                && motionEvent.getY() > (int) (620 * scaleFactor)
                                && motionEvent.getY() < (int) (1675 * scaleFactor)) {
                            try {
                                counter = counter.add(addToCounter);
                                mainEggFrameCounter = 5;

                                //create a new animation each press
                                FlyingEgg animation = new FlyingEgg(scaleFactor, flyingEggGraphic);
                                PointAnimation points = new PointAnimation(scaleFactor);
                                points.getEggsPerSec(eggsPerSecond);
                                if(pointAnimationList.size() <= 25){
                                    pointAnimationList.add(points);
                                }

                                //add it to the appropriate list depending on the size
                                if (animation.randomSize <= 240) {
                                    if(animationListBack.size() <= 25) {
                                        animationListBack.add(animation);
                                    }
                                } else {
                                    animationListFront.add(animation);
                                }
                                draw();
                                phoneVibrate.vibrate(30); //vibrate phone
                            } catch(Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    // Touched menu button
                    if(motionEvent.getX() < (int)(270 * scaleFactor)
                            && motionEvent.getY() > (int)(1600 * scaleFactor)) {
                        if(gamestate.equals("main")) {
                            menuTransitionSpeed = 200;
                            gamestate = "mainToMenu";
                        } else if(gamestate.equals("menu")){
                            menuTransitionSpeed = 200;
                            gamestate = "menuToMain";
                        }
                        phoneVibrate.vibrate(30); //vibrate phone
                    }
                    break;
            }
            return true;
        }

        public boolean backButton() {
            if(gamestate.equals("menu")) {
                menuTransitionSpeed = 200;
                gamestate = "menuToMain";
                return true;
            }
            return false;
        }

        public void pause() {
            save();
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

        public void save() {
            //save counter
            SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            String temp = counter.toString();
            editor.putString("counter", temp);
            editor.commit();
        }
    }

    /**
     * Back button was pressed
     */
    @Override
    public void onBackPressed() {
        if(!theGame.backButton()) { //game didn't handle the back button press
            new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
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
}
