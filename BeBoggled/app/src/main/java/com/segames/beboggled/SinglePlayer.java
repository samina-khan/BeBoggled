package com.segames.beboggled;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class SinglePlayer extends ActionBarActivity  implements View.OnClickListener{

    Button button_shake;
    Button button_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_page);

        // 2. Access the Buttons defined in layout XML
        // and listen for it here
        button_shake = (Button) findViewById(R.id.button_shake);
        button_shake.setOnClickListener(this);

        button_exit = (Button) findViewById(R.id.button_exit);
        button_exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Button current_button= (Button) v;
        current_button.setText("Pressed!");
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
