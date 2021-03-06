package com.segames.boggle;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


public class MainActivity extends ActionBarActivity implements View.OnClickListener,GlobalConstants{

    //All variables
    Button button_single;
    Button button_double;
    Button button_doubleCT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 2. Access the Buttons defined in layout XML
        // and listen for it here
        button_single = (Button) findViewById(R.id.button_single);
        button_single.setOnClickListener(this);
        moveViewToScreenCenter(button_single);

        button_double = (Button) findViewById(R.id.button_double);
        button_double.setOnClickListener(this);
        moveViewToScreenCenter(button_double);

        button_doubleCT = (Button) findViewById(R.id.button_doubleCT);
        button_doubleCT.setOnClickListener(this);
        moveViewToScreenCenter(button_doubleCT);
    }

    private void moveViewToScreenCenter( View view )
    {
        TranslateAnimation anim = new TranslateAnimation( 900, 0 , 0, 0 );
        anim.setDuration(1700);
        anim.setFillAfter( true );
        view.startAnimation(anim);
    }




    @Override
    public void onClick(View v) {
        Button current_button= (Button) v;
        //current_button.setText("Pressed!");
        int Round = 1;
        int Score = 0;
        switch(v.getId()){

            case R.id.button_single:
                Intent singleIntent = new Intent(v.getContext(), SinglePlayer.class);
                singleIntent.putExtra("Round",Round);
                singleIntent.putExtra("Score",Score);
                startActivity(singleIntent);
                break;
            case R.id.button_double:
                Intent doubleIntent = new Intent(v.getContext(), SetUpServerClient.class);
                doubleIntent.putExtra("Round",Round);
                doubleIntent.putExtra("Score",Score);
                doubleIntent.putExtra("Mode",BBDoubleBasicMode);
                startActivity(doubleIntent);
                break;
            case R.id.button_doubleCT:
                //NumPlayers = 2;
                Intent doubleCTIntent = new Intent(v.getContext(), SetUpServerClient.class);
                doubleCTIntent.putExtra("Round",Round);
                doubleCTIntent.putExtra("Score",Score);
                doubleCTIntent.putExtra("Mode",BBDoubleCutMode);
                startActivity(doubleCTIntent);
                break;
            default:

        }
        /*
        if(current_button.getText().equals(getString(R.string.single_mode))) {
            Intent myIntent = new Intent(v.getContext(), SinglePlayer.class);
            startActivity(myIntent);
        }*/


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
