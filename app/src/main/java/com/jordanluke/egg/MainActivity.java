package com.jordanluke.egg;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.os.Vibrator;
import android.view.Window;
import android.view.WindowManager;

import com.jordanluke.R;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

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

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

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
        String gamestate;
        Bitmap mainEggGraphic;
        List<FlyingEgg> animationList = new ArrayList<>();

        Bitmap menuButtonGraphic;
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

        int menuAnchor = 1920;

        FlyingEgg new_animation = new FlyingEgg(scaleFactor, context);

        /**
         * Game constructor
         */
        public Game(Context context) {
            super(context);
            //initialize stuff
            ourHolder = getHolder();
            paint = new Paint();
            gamestate = "main";

            //initialize graphics
            mainEggGraphic = BitmapFactory.decodeResource(this.getResources(), R.drawable.main_egg_down); //get image file
            mainEggGraphic = Bitmap.createScaledBitmap(mainEggGraphic, (int)(850 * scaleFactor), (int)(850 * scaleFactor), false); //set size



            menuButtonGraphic = BitmapFactory.decodeResource(this.getResources(), R.drawable.menu_button); //get image file
            menuButtonGraphic = Bitmap.createScaledBitmap(menuButtonGraphic, (int)(256 * scaleFactor), (int)(256 * scaleFactor), false); //set size
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
            if (ourHolder.getSurface().isValid()) { //idk what this does but some tutorial said to do it
                canvas = ourHolder.lockCanvas(); //ready to draw

                canvas.drawColor(Color.argb(255, 255, 255, 255)); //white background color

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
                paint.setTextSize(50);
                canvas.drawText("FPS:" + fps, 20, 80, paint); //draw fps counter
                canvas.drawText(screenWidthActual + "x" + screenHeightActual, 20, 140, paint);
                canvas.drawText("scale factor = " + scaleFactor, 20, 200, paint);
                canvas.drawText("gamestate = " + gamestate, 20, 260, paint);

                ourHolder.unlockCanvasAndPost(canvas); //finalize
            }
        }

        public void drawMainScreen() {
            List<FlyingEgg> itemsToRemove = new ArrayList<>();
            List<FlyingEgg> itemsToRemoveInFront = new ArrayList<>();
            List<FlyingEgg> inFront = new ArrayList<>();
            for (int i = 0; i < animationList.size(); i++) {
                if (animationList.get(i).randomSize <= 240) {
                    animationList.get(i).getSurfaceHolder(ourHolder, canvas);
                    animationList.get(i).run();
                } else {
                    inFront.add(animationList.get(i));
                }

                if (animationList.get(i).y_eggAnimationStart >= screenHeightActual) {
                    itemsToRemove.add(animationList.get(i));
                }
            }
            animationList.removeAll(itemsToRemove);
            for (int i = 0; i < itemsToRemove.size(); i++) {
                itemsToRemove.remove(i);
            }

            paint.setTextAlign(Paint.Align.CENTER);
            if (counter.toString().equals("1")) {
                canvas.drawText(counter.toString() + " egg", (int) (540 * scaleFactor), (int)(300 * scaleFactor), paint); //draw egg counter
            } else {
                canvas.drawText(counter.toString() + " eggs", (int) (540 * scaleFactor), (int)(300 * scaleFactor), paint); //draw egg counter

            }
            paint.setTextAlign(Paint.Align.LEFT);


            canvas.drawBitmap(mainEggGraphic, (int) (115 * scaleFactor), (int) (535 * scaleFactor), paint); //draw main egg

            for (int i = 0; i < inFront.size(); i++) {
                inFront.get(i).getSurfaceHolder(ourHolder, canvas);
                inFront.get(i).run();


                if (inFront.get(i).y_eggAnimationStart >= screenHeightActual) {
                    itemsToRemoveInFront.add(inFront.get(i));
                }

            }
            inFront.removeAll(itemsToRemoveInFront);
            for (int i = 0; i < itemsToRemoveInFront.size(); i++) {
                itemsToRemoveInFront.remove(i);
            }
            canvas.drawBitmap(menuButtonGraphic, (int) (35 * scaleFactor), (int) (1629 * scaleFactor), paint);
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

        public void drawMainToMenuScreen() {
            drawMainScreen(); //contine to draw the main screen until the menu is completely covering it
            drawMenuScreen(menuAnchor);
            if(menuAnchor > 0) {
                menuAnchor-=150;
            } else {
                menuAnchor = 0; //ensure the final value is correct
                gamestate = "menu"; //switch to only drawing menu
            }
        }

        public void drawMenuToMainScreen() {
            drawMainScreen(); //main screen on bottom
            drawMenuScreen(menuAnchor);
            if(menuAnchor < 1920) {
                menuAnchor+=150;
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
                        if (motionEvent.getX() < (int) (850 * scaleFactor)
                                && motionEvent.getX() > (int) (260 * scaleFactor)
                                && motionEvent.getY() > (int) (600 * scaleFactor)
                                && motionEvent.getY() < (int) (1300 * scaleFactor)) {
                            counter = counter.add(addToCounter);
                            FlyingEgg animation = new FlyingEgg(scaleFactor, context);
                            animationList.add(animation);
                            draw();
                            phoneVibrate.vibrate(30); //vibrate phone
                        }
                    }
                    // Touched menu button
                    if(motionEvent.getX() < (int)(270 * scaleFactor)
                            && motionEvent.getY() > (int)(1600 * scaleFactor)) {
                        if(gamestate.equals("main")) {
                            gamestate = "mainToMenu";
                        } else if(gamestate.equals("menu")){
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
                gamestate = "menuToMain";
                return true;
            }
            return false;
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
