package com.segames.boggle;



import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class Score extends ActionBarActivity implements View.OnClickListener{

Button button_single;
Button button_main;
    int round=1;
    int score=0;

    /* OnCreate - All the start-up stuff here */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score);
        score = getIntent().getExtras().getInt("Score");
        round = getIntent().getExtras().getInt("Round");
        Log.v("Score",Integer.toString(score));
        Log.v("Round",Integer.toString(round));

        button_single = (Button) findViewById(R.id.button_single);
        button_single.setOnClickListener(this);
        button_main = (Button) findViewById(R.id.button_main);
        button_main.setOnClickListener(this);
        //InputStream is = getResources().openRawResource(R.raw.wordlist);

        TextView scoreval = (TextView)findViewById(R.id.ScoreVal);
        TextView roundval = (TextView)findViewById(R.id.RoundVal);
        scoreval.setText("Score "+score);
        roundval.setText("Round: "+round);

        TextView helloTxt = (TextView)findViewById(R.id.allwords);
        helloTxt.setText("All Possible Words\n"+readTxt());
        //helloTxt.setMovementMethod(new ScrollingMovementMethod());


    }

    @Override
    public void onClick(View v) {
        //Button current_button= (Button) v;

        switch(v.getId()) {
            case R.id.button_single:
                Intent singleIntent = new Intent(v.getContext(), SinglePlayer.class);
                singleIntent.putExtra("Round",round+1);
                singleIntent.putExtra("Score",score);
                startActivity(singleIntent);
                break;
            case R.id.button_main:
                Intent mainIntent = new Intent(v.getContext(), MainActivity.class);
                startActivity(mainIntent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
    }

    private String readTxt(){

        String str = "";
        String[] words = CommManager.getGridWords().split("\\|");
        for(String word:words){
            str+=word+"\n";
        }
        return str;

    }
}
