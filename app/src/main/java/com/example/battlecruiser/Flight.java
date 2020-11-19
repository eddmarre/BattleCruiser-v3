package com.example.battlecruiser;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.battlecruiser.GameView.screenRatioX;
import static com.example.battlecruiser.GameView.screenRatioY;

public class Flight {
    int x,y,width,height,wingCounter=0, toShoot=0, shootCounter=1;
    Bitmap flight1,flight2, shoot1, dead;
    boolean isGoingUp=false;
    private GameView gameView;

    Flight(GameView gameview, int screenY, Resources res)
    {
        this.gameView = gameview;

        flight1= BitmapFactory.decodeResource(res,R.drawable.spaceship);
        flight2=BitmapFactory.decodeResource(res,R.drawable.spaceshipflying);

        width=flight1.getWidth();
        height=flight1.getHeight();

        width /=4;
        height /=4;

        width*=(int)screenRatioX;
        height*=(int)screenRatioY;

        flight1=Bitmap.createScaledBitmap(flight1,width,height,false);
        flight2=Bitmap.createScaledBitmap(flight2,width,height,false);

        shoot1 = BitmapFactory.decodeResource(res, R.drawable.shoot);

        shoot1 = Bitmap.createScaledBitmap(shoot1, width, height, false);

        dead = BitmapFactory.decodeResource(res, R.drawable.spaceshipdead);
        dead = Bitmap.createScaledBitmap(dead, width, height, false);

        y=screenY/2;
        x=(int)(64*screenRatioX);
    }

    Bitmap getFlight()
    {
        if(toShoot != 0){
            if(shootCounter == 1){
                shootCounter++;
                return shoot1;
            }

            shootCounter = 1;
            toShoot--;
            gameView.newBullet();

            return shoot1;

        }
        if(wingCounter==0)
        {
            wingCounter++;
            return flight1;
        }
        wingCounter--;
        return flight2;
    }

    Rect getCollision(){
        return new Rect(x, y, x+width, y+height);
    }

    Bitmap getDead() {
        return dead;
    }

}
