package com.example.battlecruiser;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {
    private Thread thread;
    private boolean isPlaying;
    private boolean isGameOver = false;
    private Background background1,background2;
    private int screenX, screenY;
    public int score = 0;
    private SharedPreferences prefs;
    private Paint paint;
    private SoundPool soundPool;
    private Enemy[] enemies;
    private List<Bullet> bullets;
    private int sound;
    private Random random;
    private GameActivity activity;
    public static float screenRatioX,screenRatioY;
    private Flight flight;

    public GameView(GameActivity activity,int screenX, int screenY) {
        super(activity);

        this.activity = activity;

        prefs = activity.getSharedPreferences("game", Context.MODE_PRIVATE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            AudioAttributes audioAttributes = new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).setUsage(AudioAttributes.USAGE_GAME).build();
            soundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).build();
        }else{
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }

        sound = soundPool.load(activity, R.raw.shoot,1);

        this.screenX=screenX;
        this.screenY=screenY;
        screenRatioX=1920f / screenX;
        screenRatioY=1080f/screenY;
        background1=new Background(screenX,screenY,getResources());
        background2=new Background(screenX,screenY,getResources());

        flight=new Flight(this, screenY,getResources());

        bullets = new ArrayList<>();

        background2.x=screenX;

        paint=new Paint();
        paint.setTextSize(128);
        paint.setColor(Color.WHITE);

        enemies = new Enemy[4];

        random = new Random();

        for(int i = 0; i < 4; i++){
            Enemy enemy = new Enemy(getResources());
            enemies[i] = enemy;
        }
    }

    @Override
    public void run() {
        while(isPlaying)
        {
            update();
            draw();
            sleep();
        }
    }
    private void update()
    {
        background1.x-=10*screenRatioX;
        background2.x-=10*screenRatioX;

        if(background1.x + background1.background.getWidth()<0)
        {
            background1.x=screenX;
        }
        if(background2.x + background2.background.getWidth()<0)
        {
            background2.x=screenX;
        }
        if(flight.isGoingUp)
        {
            flight.y-=30*screenRatioY;
        }
        else
        {
            flight.y+=30*screenRatioY;
        }
        if(flight.y<0)
        {
            flight.y=0;
        }
        if(flight.y>screenY-flight.height)
        {
            flight.y=screenY-flight.height;
        }
        List<Bullet> trash = new ArrayList<>();

        for(Bullet bullet : bullets){
            if(bullet.x > screenX){
                trash.add(bullet);
            }
            bullet.x += 50 * screenRatioX;

            for(Enemy enemy : enemies){

                if(Rect.intersects(enemy.getCollision(), bullet.getCollision())){
                    score++;
                    enemy.x = -500;
                    bullet.x = screenX + 500;
                    enemy.wasShot = true;
                }
            }
        }
        for(Bullet bullet : trash){
            bullets.remove(bullet);
        }

        for(Enemy enemy : enemies){
            enemy.x -= enemy.speed;
            if(enemy.x + enemy.width < 0){

                if(!enemy.wasShot){
                    isGameOver = true;
                    return;
                }

                int bound = (int) (30 * screenRatioX);
                enemy.speed = random.nextInt(bound);

                if (enemy.speed  < 10 * screenRatioX){
                    enemy.speed = (int) (10 * screenRatioX);
                }
                enemy.x = screenX;
                enemy.y = random.nextInt(screenY - enemy.height);

                enemy.wasShot = false;
            }

            if(Rect.intersects(enemy.getCollision(), flight.getCollision())){
                isGameOver = true;
                return;
            }
        }
    }
    private void draw()
    {
        if(getHolder().getSurface().isValid())
        {
            Canvas canvas=getHolder().lockCanvas();
            canvas.drawBitmap(background1.background,background1.x,background1.y,paint);
            canvas.drawBitmap(background2.background,background2.x,background2.y,paint);

            for(Enemy enemy : enemies){
                canvas.drawBitmap(enemy.getEnemy(), enemy.x, enemy.y, paint);
            }
            
            canvas.drawText(score + "", screenX/2f, 164, paint);

            if(isGameOver){
                isPlaying=false;
                canvas.drawBitmap(flight.getDead(), flight.x, flight.y, paint);
                getHolder().unlockCanvasAndPost(canvas);
                saveIfHighScore();
                saveLastScore();
                waitBeforeExit();
                return;
            }

            canvas.drawBitmap(flight.getFlight(),flight.x,flight.y,paint);

            for(Bullet bullet : bullets){
                canvas.drawBitmap(bullet.bullet, bullet.x, bullet.y, paint);
            }

            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void waitBeforeExit() {
        try{
            Thread.sleep(3000);
            activity.startActivity(new Intent(activity, Leaderboard.class));
            activity.finish();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    private void saveIfHighScore() {
        if(prefs.getInt("highscore", 0) < score){
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("highscore", score);
            editor.apply();
        }
    }
    private void saveLastScore()
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("lastscore", score);
        editor.apply();
    }

    private void sleep()
    {
        try {
            thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void resume()
    {
        isPlaying=true;
        thread=new Thread(this);
        thread.start();
    }
    public void pause()
    {
        try {
            isPlaying=false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            {
                if(event.getX()<screenX/2)
                {
                    flight.isGoingUp=true;
                }
                break;
            }
            case MotionEvent.ACTION_UP:
            {
                flight.isGoingUp=false;
                if(event.getX() > screenX / 2){
                    flight.toShoot++;
                }
                break;
            }

        }
        return true;
    }

    public void newBullet() {

        if(!prefs.getBoolean("isMute", false)){
            soundPool.play(sound,1,1,0,0,1);
        }

        Bullet bullet = new Bullet(getResources());
        bullet.x = flight.x + flight.width;
        bullet.y = flight.y + (flight.height / 2);
        bullets.add(bullet);
    }
}
