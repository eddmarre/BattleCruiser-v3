package com.example.battlecruiser;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.battlecruiser.GameView.screenRatioX;
import static com.example.battlecruiser.GameView.screenRatioY;

public class Enemy {

    public boolean wasShot = true;
    int speed = 20;
    int x, y, width, height, enemyCounter=1;
    Bitmap enemy1;

    Enemy(Resources res){
        enemy1 = BitmapFactory.decodeResource(res, R.drawable.enemy);

        width = enemy1.getWidth();
        height = enemy1.getHeight();

        width /= 6;
        height /= 6;

        width *= (int) screenRatioX;
        height *= (int) screenRatioY;

        enemy1 = Bitmap.createScaledBitmap(enemy1, width, height, false);

        y = -height;

    }

    Bitmap getEnemy(){
        if(enemyCounter==1){
            return enemy1;
        }

        enemyCounter = 1;
        return enemy1;
    }

    Rect getCollision(){
        return new Rect(x, y, x+width, y+height);
    }
}
