package com.segames.boggle;



import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class Score extends ActionBarActivity implements View.OnClickListener{



    /* OnCreate - All the start-up stuff here */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score);
        int score = getIntent().getExtras().getInt("Score");
        //InputStream is = getResources().openRawResource(R.raw.wordlist);

        TextView scoreval = (TextView)findViewById(R.id.ScoreVal);
        //readTxt();
        scoreval.setText("Score: "+score);

        TextView helloTxt = (TextView)findViewById(R.id.allwords);
        helloTxt.setText(readTxt());


    }

    @Override
    public void onClick(View v) {

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
