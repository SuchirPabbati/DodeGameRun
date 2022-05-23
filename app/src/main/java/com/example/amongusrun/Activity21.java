package com.example.amongusrun;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;

import java.util.Timer;

import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
public class Activity21 extends AppCompatActivity {
    GameSurface gameSurface;
    int changeY=12;
    MediaPlayer mediaPlayer;
    SoundPool soundPool;
    int sound1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_21);
        getSupportActionBar().hide();
        gameSurface = new GameSurface(this);
        setContentView(gameSurface);
        mediaPlayer = MediaPlayer.create(this, R.raw.songs);
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(10F, 10F);
        mediaPlayer.start();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(1)
                .build();
        sound1 = soundPool.load(this, R.raw.kill, 1);
        gameSurface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(changeY==12){
                    changeY=36;
                }
                else
                    changeY=12;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameSurface.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameSurface.resume();
    }
    public class GameSurface extends SurfaceView implements Runnable,SensorEventListener{
        Thread gameThread;
        SurfaceHolder holder;
        volatile boolean running = false;
        boolean game=true;
        Bitmap ball,bitmap,gg,scaledBitmap,scaledBitmapStar,back1,back2,scaledBitmap2,back3,back4,back5;
        Bitmap[] frames,impframe;
        int ballX = 0,ballY=0,impY=0,impX=0,x=0,change=0,run=0,rand=0,impypos=0,points=0,myNum=0,back1y=0,back2y=0,counterFrame=0,state=0,back1y2,back2y2,back1y3,back2y3,back1y4,back2y4,back1y5,back2y5;
        String current="";
        boolean point=true;
        Paint paintProperty,paint;
        private long mLastTime = 0;
        private int fps = 0, ifps = 0,totalfps=0;
        int screenWidth, screenHeight;
        public GameSurface(Context context){
            super(context);
            frames = new Bitmap[5];
            impframe = new Bitmap[2];
            holder = getHolder();
            ball = BitmapFactory.decodeResource(getResources(), R.drawable.sus_1);
            frames[0] = BitmapFactory.decodeResource(getResources(), R.drawable.crewmate);
            frames[1] = BitmapFactory.decodeResource(getResources(), R.drawable.sus_1);
            frames[2] = BitmapFactory.decodeResource(getResources(), R.drawable.sus_3);
            frames[3] = BitmapFactory.decodeResource(getResources(), R.drawable.sus_4);
            frames[4] = BitmapFactory.decodeResource(getResources(), R.drawable.deadsus);
            impframe[0] = BitmapFactory.decodeResource(getResources(), R.drawable.impsus);
            impframe[1] = BitmapFactory.decodeResource(getResources(), R.drawable.imposterhitres);
            back1 = BitmapFactory.decodeResource(getResources(), R.drawable.bglayer);
            back2 = BitmapFactory.decodeResource(getResources(), R.drawable.planetround);
            back3 = BitmapFactory.decodeResource(getResources(), R.drawable.multiplanet);
            back4 = BitmapFactory.decodeResource(getResources(), R.drawable.saturn);
            back5 = BitmapFactory.decodeResource(getResources(), R.drawable.stars);
            Display screenDisplay = getWindowManager().getDefaultDisplay();
            Point screenSize = new Point();
            screenDisplay.getSize(screenSize);
            screenWidth = screenSize.x;
            screenHeight = screenSize.y;
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.space);
            gg = BitmapFactory.decodeResource(getResources(), R.drawable.gameover);
            scaledBitmap = Bitmap.createScaledBitmap(back1, screenWidth,    screenHeight, true);
            scaledBitmapStar = Bitmap.createScaledBitmap(back5, screenWidth,    screenHeight, true);
            scaledBitmap2 = Bitmap.createScaledBitmap(gg, screenWidth,    screenHeight, true);
            ballX=(screenWidth/2-ball.getWidth()/2);
            ballY=screenHeight-ball.getHeight()*3;
            back2y = -screenHeight;
            back2y2 = -screenHeight;
            back2y3 = -screenHeight;
            back2y4 = -screenHeight;
            back2y5 = -screenHeight;
            impX=(impframe[0].getWidth()*rand+20);
            SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            new CountDownTimer(60000, 1000) {
                public void onTick(long millisUntilFinished) {
                    current = String.valueOf(millisUntilFinished / 1000);
                    invalidate(); // Force the View to redraw
                }
                public void onFinish() {}
            }.start();
            Sensor magnometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
            //Sensor magnometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this,magnometer,sensorManager.SENSOR_DELAY_GAME);
            paintProperty = new Paint();
        }
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
                if(counterFrame != 4 && ballX+sensorEvent.values[0]/2<screenWidth-ball.getWidth() && ballX + sensorEvent.values[0]/2 > 0) {
                    //Log.d("value",sensorEvent.values[0]+"");
                    ballX+=(sensorEvent.values[0]/2);
                }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
        @Override
        public void run() {
                while (running) {
                    if (holder.getSurface().isValid() == false)
                        continue;
                    Canvas canvas = holder.lockCanvas();
                    paint = new Paint();
                    paint.setColor(Color.WHITE);
                    paint.setStyle(Paint.Style.FILL);
                    canvas.drawPaint(paint);
                    paint.setColor(Color.BLUE);
                    paint.setTextSize(30);
                    canvas.drawRGB(0, 0, 0);
                    canvas.drawBitmap(scaledBitmap,0,back1y,null);
                    canvas.drawBitmap(scaledBitmap,0,back2y+10,null);
                    back1y+=10;
                    back2y+=10;
                    if(back1y>screenHeight){
                        back1y=-screenHeight+10;
                    }
                    if(back2y>screenHeight){
                        back2y=-screenHeight+10;
                    }
                    canvas.drawBitmap(scaledBitmapStar,0,back1y5,null);
                    canvas.drawBitmap(scaledBitmapStar,0,back2y5+10,null);
                    back1y5+=6;
                    back2y5+=6;
                    if(back1y5>screenHeight){
                        back1y5=-screenHeight+10;
                    }
                    if(back2y5>screenHeight){
                        back2y5=-screenHeight+10;
                    }
                    canvas.drawBitmap(back2,screenWidth-ball.getWidth(),back1y2,null);
                    canvas.drawBitmap(back2,100,back2y2+10,null);
                    back1y2+=2;
                    back2y2+=2;
                    if(back1y2>screenHeight){
                        back1y2=-screenHeight+10;
                    }
                    if(back2y2>screenHeight){
                        back2y2=-screenHeight+10;
                    }
                    canvas.drawBitmap(back3,screenWidth-ball.getWidth()*3,back1y3,null);
                    canvas.drawBitmap(back3,50,back2y3+10,null);
                    back1y3+=4;
                    back2y3+=4;
                    if(back1y3>screenHeight){
                        back1y3=-screenHeight+10;
                    }
                    if(back2y3>screenHeight){
                        back2y3=-screenHeight+10;
                    }
                    canvas.drawBitmap(back4,screenWidth-ball.getWidth()*5,back1y4,null);
                    canvas.drawBitmap(back4,75,back2y4+10,null);
                    back1y4+=1;
                    back2y4+=1;
                    if(back1y4>screenHeight){
                        back1y4=-screenHeight+10;
                    }
                    if(back2y4>screenHeight){
                        back2y4=-screenHeight+10;
                    }

                    run++;
                    long now = System.currentTimeMillis();
                    canvas.drawText(current+" s",screenWidth/2,ball.getHeight()/2,paint);
                    canvas.drawText(fps+" fps",screenWidth-ball.getWidth(),ball.getHeight()/2,paint);
                    totalfps++;
                    ifps++;
                    if(now > (mLastTime + 1000)) {
                        mLastTime = now;
                        fps = ifps;
                        ifps = 0;
                    }
                    if(totalfps%10==0){
                       counterFrame = (counterFrame + 1) % 4;
                    }
                    try {
                        myNum = Integer.parseInt(current);
                        if(myNum<=0) {
                            running = false;
                        }
                    } catch(NumberFormatException nfe) {
                        System.out.println("Could not parse " + nfe);
                    }
                    if (impY > screenHeight) {

                        impY = -impframe[0].getHeight();
                        x=0;
                        impypos = 0;
                        if (point) {
                            points++;
                        }
                        else
                            point = true;
                        rand = (int) (Math.random() * 5);
                        //Log.d("balls",rand+"");
                        impX = (impframe[0].getWidth() * rand + 20);

                    }
                    impypos = ((-1 * impframe[0].getHeight()) + (impY * 6));
                    if (ballX + ball.getWidth() > impX && ballX < impX + impframe[0].getWidth() && ballY + ball.getHeight() > impY && ballY < impY + impframe[0].getHeight()) {
                        point = false;
                        if(x==0) {
                            soundPool.play(sound1, 1, 1, 1, 0, 1);
                            x++;
                        }
                        counterFrame = 4;
                        state=1;
                    }
                    else
                        state=0;
                    canvas.drawBitmap(frames[counterFrame], ballX, screenHeight - ball.getHeight() * 3, null);
                    canvas.drawText("Points: "+points, 10, ball.getHeight()/2, paint);
                    canvas.drawBitmap(impframe[state], impX, impY, null);
                    if(myNum<=0) {
                        canvas.drawBitmap(scaledBitmap2, 0, 0, null);
                        paint.setTextSize(60);
                        canvas.drawText("Points: "+points, screenWidth/2-(ball.getWidth()), screenHeight/2, paint);
                    }
                    impY += changeY;
                    holder.unlockCanvasAndPost(canvas);
                }
        }

        public void resume(){
            running = true;
            gameThread = new Thread(this);
            gameThread.start();
        }

        public void pause(){
            running = false;
            while(true){
                try {
                    gameThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    }
