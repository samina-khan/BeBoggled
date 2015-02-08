package com.segames.beboggled;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class LaunchPage extends ActionBarActivity  implements View.OnClickListener{

    Button button_single;
    Button button_double;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_page);

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
        if(current_button.getText().equals(getString(R.string.single_mode))) {
            Intent myIntent = new Intent(v.getContext(), SinglePlayer.class);
            startActivity(myIntent);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_launch_page, menu);
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
