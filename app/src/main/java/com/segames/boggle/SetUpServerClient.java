/* Choice for whether server or client */
package com.segames.boggle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class SetUpServerClient extends ActionBarActivity implements View.OnClickListener,GlobalConstants{

    //All variables
    Button button_server;
    Button button_client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup_serverclient);

        // 2. Access the Buttons defined in layout XML
        // and listen for it here
        button_server = (Button) findViewById(R.id.button_server);
        button_server.setOnClickListener(this);

        button_client = (Button) findViewById(R.id.button_client);
        button_client.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Button current_button= (Button) v;
        //current_button.setText("Pressed!");
        int Round = getIntent().getExtras().getInt("Round");
        int Score = getIntent().getExtras().getInt("Score");
        int Mode = getIntent().getExtras().getInt("Mode");
        Class targetClass = (Mode==BBDoubleBasicMode)?DoublePlayer.class:DoublePlayerCut.class;
        switch(v.getId()){

            case R.id.button_server:
                Intent sIntent = new Intent(v.getContext(), targetClass);
                sIntent.putExtra("Round",Round);
                sIntent.putExtra("Score",Score);
                sIntent.putExtra("Mode",Mode);
                sIntent.putExtra("Role",ServerRole);
                startActivity(sIntent);
                break;
            case R.id.button_client:
                Intent cIntent = new Intent(v.getContext(), targetClass);
                cIntent.putExtra("Round",Round);
                cIntent.putExtra("Score",Score);
                cIntent.putExtra("Mode",Mode);
                cIntent.putExtra("Role",ClientRole);
                startActivity(cIntent);
            default:

        }

    }

    /* EDWARD: My guess is that you want to put in the Device_list menu stuff here */
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
