package com.example.battlecruiser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Leaderboard extends AppCompatActivity {
    private TextView name1,name2,name3,name4,name5;
    private TextView score1,score2,score3,score4,score5;
    private TextView enterNameTextView;
    private EditText enterName;
    private Button confirmName, continuebutton;
    public DatabaseHelper myDB;
    public static boolean wasHistoryButtonPressed=false;

    List<String> names;
    List<Integer> scores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_leaderboard);
        myDB=new DatabaseHelper(this);
        names=new ArrayList<>();
        scores=new ArrayList<>();

        enterNameTextView=findViewById(R.id.enterNameTextView);
        enterName=findViewById(R.id.enterNameEditText);
        confirmName=findViewById(R.id.ConfirmNameButton);
        continuebutton=findViewById(R.id.continueButton);

        name1=findViewById(R.id.leaderboardName1);
        name2=findViewById(R.id.leaderboardName2);
        name3=findViewById(R.id.leaderboardName3);
        name4=findViewById(R.id.leaderboardName4);
        name5=findViewById(R.id.leaderboardName5);

        score1=findViewById(R.id.leaderboardScore1);
        score2=findViewById(R.id.leaderboardScore2);
        score3=findViewById(R.id.leaderboardScore3);
        score4=findViewById(R.id.leaderboardScore4);
        score5=findViewById(R.id.leaderboardScore5);

        enterNameTextView.setVisibility(View.INVISIBLE);
        enterName.setVisibility(View.INVISIBLE);
        confirmName.setVisibility(View.INVISIBLE);
        continuebutton.setVisibility(View.VISIBLE);



        final SharedPreferences prefs = getSharedPreferences("game", MODE_PRIVATE);
        viewAllScores();
        int tempScore=0;
        if(!score5.getText().toString().equals("---"))
        {
            tempScore=Integer.parseInt(score5.getText().toString());
        }

        if(score5.getText().toString().equals("---")&&(prefs.getInt("lastscore", 0)!=prefs.getInt("lastscore", 0))||tempScore<prefs.getInt("lastscore", 0))
        {
            continuebutton.setVisibility(View.INVISIBLE);
            enterNameTextView.setVisibility(View.VISIBLE);
            enterName.setVisibility(View.VISIBLE);
            confirmName.setVisibility(View.VISIBLE);
            AddData();
        }


    }

    public void AddData()
    {
        confirmName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tempName=enterName.getText().toString();
                if(tempName.matches(""))
                {
                    Toast.makeText(Leaderboard.this, "Please enter a valid name", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    final SharedPreferences prefs = getSharedPreferences("game", MODE_PRIVATE);
                    boolean isInserted = myDB.insertData(enterName.getText().toString(), prefs.getInt("lastscore", 0));
                    if (isInserted = true) {
                        Toast.makeText(Leaderboard.this, "Leaderboard updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Leaderboard.this, "Error Updating leaderboard", Toast.LENGTH_SHORT).show();
                    }
                    startActivity(new Intent(Leaderboard.this, MainActivity.class));
                }
            }
        });
    }
    public void viewAllScores()
    {
        Cursor res=myDB.getAllData();
        if(res.getCount()==0)
        {
            return;
        }
        else
        {
            while(res.moveToNext())
            {
                names.add(res.getString(1));
                scores.add(res.getInt(2));
            }

        }
        setScores();
    }

    private void setScores()
    {
        try {
            name1.setText(names.get(0).toString());
            score1.setText(scores.get(0).toString());
        } catch (Exception e) {
            name1.setTextColor(Color.rgb(255,0,0));
            score1.setTextColor(Color.rgb(255,0,0));
            name1.setText("---");
            score1.setText("---");
        }
        try {
            name2.setText(names.get(1).toString());
            score2.setText(scores.get(1).toString());
        } catch (Exception e) {
            name2.setTextColor(Color.rgb(255,0,0));
            score2.setTextColor(Color.rgb(255,0,0));
            name2.setText("---");
            score2.setText("---");
        }
        try {
            name3.setText(names.get(2).toString());
            score3.setText(scores.get(2).toString());
        } catch (Exception e) {
            name3.setTextColor(Color.rgb(255,0,0));
            score3.setTextColor(Color.rgb(255,0,0));
            name3.setText("---");
            score3.setText("---");
        }
        try {
            name4.setText(names.get(3).toString());
            score4.setText(scores.get(3).toString());
        } catch (Exception e) {
            name4.setTextColor(Color.rgb(255,0,0));
            score4.setTextColor(Color.rgb(255,0,0));
            name4.setText("---");
            score4.setText("---");
        }
        try {
            name5.setText(names.get(4).toString());
            score5.setText(scores.get(4).toString());
        } catch (Exception e) {
            name5.setTextColor(Color.rgb(255,0,0));
            score5.setTextColor(Color.rgb(255,0,0));
            name5.setText("---");
            score5.setText("---");
        }
    }


    public void continueToMainMenu(View view) {
        startActivity(new Intent(Leaderboard.this, MainActivity.class));
    }
}