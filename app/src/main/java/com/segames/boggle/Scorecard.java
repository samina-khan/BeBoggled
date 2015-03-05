package com.segames.boggle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class Scorecard extends ActionBarActivity implements View.OnClickListener,GlobalConstants{

    //Button button_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scorecard);

        int scoreVal = getIntent().getExtras().getInt("Score");
        int roundVal = getIntent().getExtras().getInt("Round");

        if(getIntent().getExtras().getInt("Mode") != BBSingleMode){
            scoreVal = CommManagerMulti.getTotalScore();
            int oppscoreVal = CommManagerMulti.getTotalOppScore();
            int roundsWon = CommManagerMulti.getNbRoundWon();
            int roundsOppWon = CommManagerMulti.getNbRoundWonOpp();

            TextView scorevalopp = (TextView)findViewById(R.id.oppscore);
            TextView winner = (TextView)findViewById(R.id.winner);
            TextView tvroundsWon = (TextView) findViewById(R.id.roundsWon);
            TextView tvroundsOppWon = (TextView) findViewById(R.id.roundsOppWon);

            scorevalopp.setText(Integer.toString(oppscoreVal));
            tvroundsWon.setText(Integer.toString(roundsWon));
            tvroundsOppWon.setText(Integer.toString(roundsOppWon));

            findViewById(R.id.oppscoreField).setVisibility(View.VISIBLE);
            findViewById(R.id.roundsWonLayout).setVisibility(View.VISIBLE);

            int result = CommManagerMulti.winner();
            System.out.println("Result: "+ result);
            if(result == BBResultOppWin) winner.setText("You Lost!");
            else if (result == BBResultSelfWin) winner.setText("You Won!");
            else if(result == BBResultTie) winner.setText("Tied!");
            winner.setVisibility(View.VISIBLE);
        }

        TextView scoreval = (TextView)findViewById(R.id.score);
        TextView roundval = (TextView)findViewById(R.id.round);
        scoreval.setText(Integer.toString(scoreVal));
        roundval.setText(Integer.toString(roundVal));

        final Button button_close = (Button) findViewById(R.id.button_close);
        button_close.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(v.getContext(), MainActivity.class);
                mainIntent.putExtra("Mode",getIntent().getExtras().getInt("Mode"));
                startActivity(mainIntent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scorecard, menu);
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

    //@Override
    public void onClick(View v) {
        Intent mainIntent = new Intent(v.getContext(), MainActivity.class);
        startActivity(mainIntent);
        //Button current_button= (Button) v;
        /*switch(v.getId()) {
            case R.id.button_single:
                switch(mode) {
                    case BBSingleMode:
                        Intent singleIntent = new Intent(v.getContext(), SinglePlayer.class);
                        singleIntent.putExtra("Round",round+1);
                        singleIntent.putExtra("Score",score);
                        startActivity(singleIntent);
                        break;
                    case BBDoubleBasicMode:
                        Intent doubleIntent = new Intent(v.getContext(), DoublePlayer.class);
                        doubleIntent.putExtra("Round",round+1);
                        doubleIntent.putExtra("Score",score);
                        doubleIntent.putExtra("Role",role);
                        doubleIntent.putExtra("Mode",BBDoubleBasicMode);
                        startActivity(doubleIntent);
                        break;
                    case BBDoubleCutMode:
                        Intent cutIntent = new Intent(v.getContext(), DoublePlayerCut.class);
                        cutIntent.putExtra("Round",round+1);
                        cutIntent.putExtra("Score",score);
                        cutIntent.putExtra("Role",role);
                        cutIntent.putExtra("Mode",BBDoubleCutMode);
                        startActivity(cutIntent);
                        break;
                }

                break;
            case R.id.button_main:

                Intent mainIntent = new Intent(v.getContext(), Scorecard.class);
                startActivity(mainIntent);
                break;
        }
    */}
}
