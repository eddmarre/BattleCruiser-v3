package com.example.battlecruiser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.ads.AdView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class MainActivity extends AppCompatActivity {
    private boolean isMute;
    private AdView AdView;
    private Leaderboard leaderboard;
    //private DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //myDb=new DatabaseHelper(this);




        //sets up SDK for admob
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        //locates and loads in google test ad
        AdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        AdView.loadAd(adRequest);


        findViewById(R.id.PlayTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GameActivity.class));
            }
        });

       /* findViewById(R.id.historyImageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Leaderboard.class));
            }
        });*/



        TextView highscore = findViewById(R.id.HighScoreTextView);

        final SharedPreferences prefs = getSharedPreferences("game", MODE_PRIVATE);
        highscore.setText("HighScore: " +prefs.getInt("highscore", 0));

        isMute = prefs.getBoolean("isMute", false);

        final ImageView volumeCtrl = findViewById(R.id.VolumeControlImageView);

        if(isMute){
            volumeCtrl.setImageResource(R.drawable.ic_baseline_volume_off_24);
        }else{
            volumeCtrl.setImageResource(R.drawable.ic_baseline_volume_up_24);
        }

        volumeCtrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isMute = !isMute;
                if(isMute){
                    volumeCtrl.setImageResource(R.drawable.ic_baseline_volume_off_24);
                }else{
                    volumeCtrl.setImageResource(R.drawable.ic_baseline_volume_up_24);
                }
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("isMute", isMute);
                editor.apply();
            }
        });
    }
}