package com.segames.boggle;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity implements View.OnClickListener{

    //All variables
    Button button_single;
    Button button_double;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 2. Access the Buttons defined in layout XML
        // and listen for it here
        button_single = (Button) findViewById(R.id.button_single);
        button_single.setOnClickListener(this);

        button_double = (Button) findViewById(R.id.button_double);
        button_double.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Button current_button= (Button) v;
        //current_button.setText("Pressed!");
        int Round = 1;
        int Score = 0;
        switch(v.getId()){

            case R.id.button_single:
                //Round = 1;
                Intent singleIntent = new Intent(v.getContext(), SinglePlayer.class);
                singleIntent.putExtra("Round",Round);
                singleIntent.putExtra("Score",Score);
                startActivity(singleIntent);
                break;
            case R.id.button_double:
                //NumPlayers = 2;
                Intent doubleIntent = new Intent(v.getContext(), DoublePlayer.class);
                doubleIntent.putExtra("Round",Round);
                startActivity(doubleIntent);
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
