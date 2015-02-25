package com.segames.boggle;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;



public class Score extends ActionBarActivity implements View.OnClickListener,GlobalConstants{

    Button button_single;
    Button button_main;
    Button[][] gridbtns;
    Drawable[] arrows = new Drawable[9];
    int size = -1;

    int round=1;
    int score=0;
    int oppscore = 0;
    int mode = -1;
    int role;
    String gridstr = "";
    TableLayout table;


    private ViewFlipper viewFlipper;
    private float lastX;

    /* OnCreate - All the start-up stuff here */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score);

        viewFlipper = (ViewFlipper) findViewById(R.id.flipper);
        viewFlipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(mode!=BBSingleMode){return performFunc(event);}
                else return true;
            }
        });


        score = getIntent().getExtras().getInt("Score");
        round = getIntent().getExtras().getInt("Round");
        mode = getIntent().getExtras().getInt("Mode");
        if(mode!=BBSingleMode){oppscore = getIntent().getExtras().getInt("OppScore");}
        gridstr = getIntent().getStringExtra("Grid");

        Log.v("Score",Integer.toString(score));
        Log.v("Round",Integer.toString(round));
        table = (TableLayout) findViewById(R.id.bigtable);


        if(mode==BBSingleMode) {
            findViewById(R.id.greenarrows).setVisibility(View.GONE);
            if (round <= BBMaxEasyRounds) {
                table = (TableLayout) findViewById(R.id.smalltable);
                table.setVisibility(View.VISIBLE);
                findViewById(R.id.bigtable).setVisibility(View.GONE);
                populateSmallGrid();
                size= BBEasyLevelSize;
            }
            else{
                populateBigGrid();
                size= BBNormalLevelSize;
            }
        }
        else{
            role = getIntent().getExtras().getInt("Role");
            populateBigGrid();
            size= BBNormalLevelSize;
        }


        button_single = (Button) findViewById(R.id.button_single);
        button_single.setOnClickListener(this);
        button_main = (Button) findViewById(R.id.button_main);
        button_main.setOnClickListener(this);

        TextView scoreval = (TextView)findViewById(R.id.ScoreVal);
        if(mode!=BBSingleMode){
            TextView scorevalopp = (TextView)findViewById(R.id.ScoreValOpp);
            TextView winner = (TextView)findViewById(R.id.Winner);

            if(oppscore > score) winner.setText("You Lost!");
            else if (oppscore < score) winner.setText("You Won!");
            else winner.setText("You Tied!");
            winner.setVisibility(View.VISIBLE);

            scorevalopp.setText("Opponent Score "+oppscore);
            scorevalopp.setVisibility(View.VISIBLE);
        }
        TextView roundval = (TextView)findViewById(R.id.RoundVal);
        scoreval.setText("Your Score "+score);
        roundval.setText("Round: "+round);

        populateList();
        if(mode!=BBSingleMode)populateOppList();

        ImageView greenarrows = (ImageView) findViewById(R.id.greenarrows);
        greenarrows.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                findViewById(R.id.layout_back).setVisibility(View.VISIBLE);
                findViewById(R.id.layout_front).setVisibility(View.GONE);
                */

            }
        });
        ImageView greenarrows2 = (ImageView) findViewById(R.id.greenarrows2);
        greenarrows2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                /*
                findViewById(R.id.layout_back).setVisibility(View.GONE);
                findViewById(R.id.layout_front).setVisibility(View.VISIBLE);*/
            }
        });

        arrows= new Drawable[]{getResources().getDrawable(R.drawable.yellowtopleft), getResources().getDrawable(R.drawable.yellowup_alt), getResources().getDrawable(R.drawable.yellowtopright),
                getResources().getDrawable(R.drawable.yellowleft_alt), getResources().getDrawable(R.drawable.yellowdie),getResources().getDrawable(R.drawable.yellowright_alt), getResources().getDrawable(R.drawable.yellowbottomleft), getResources().getDrawable(R.drawable.yellowdown_alt), getResources().getDrawable(R.drawable.yellowbottomright)};


    }

    boolean performFunc(MotionEvent touchevent){
        switch (touchevent.getAction()) {

            case MotionEvent.ACTION_DOWN:
                lastX = touchevent.getX();
                break;
            case MotionEvent.ACTION_UP:
                float currentX = touchevent.getX();

                // Handling left to right screen swap.
                if (lastX < currentX) {

                    // If there aren't any other children, just break.
                    if (viewFlipper.getDisplayedChild() == 0)
                        break;

                    // Next screen comes in from left.
                    viewFlipper.setInAnimation(this, R.anim.slide_in_from_left);
                    // Current screen goes out from right.
                    viewFlipper.setOutAnimation(this, R.anim.slide_out_to_right);

                    // Display next screen.
                    viewFlipper.showNext();
                }

                // Handling right to left screen swap.
                if (lastX > currentX) {

                    // If there is a child (to the left), kust break.
                    if (viewFlipper.getDisplayedChild() == 1)
                        break;

                    // Next screen comes in from right.
                    viewFlipper.setInAnimation(this, R.anim.slide_in_from_right);
                    // Current screen goes out from left.
                    viewFlipper.setOutAnimation(this, R.anim.slide_out_to_left);

                    // Display previous screen.
                    viewFlipper.showPrevious();
                }
                break;
        }
        return true;
    }

    /*
    public boolean onTouchEvent(MotionEvent touchevent) {
        return performFunc(touchevent);
    }*/

    @Override
    public void onClick(View v) {
        //Button current_button= (Button) v;
        switch(v.getId()) {
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
                /*
*/
                Intent mainIntent = new Intent(v.getContext(), MainActivity.class);
                startActivity(mainIntent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
    }

    private void populateList(){
        String[] words = (mode!=BBSingleMode)?CommManagerMulti.getGridWords().split("\\|"):CommManager.getGridWords().split("\\|");
        final CustomListAdapter adapter = new CustomListAdapter(this, android.R.layout.simple_list_item_1,words);
        final ListView allwordslist = (ListView) findViewById(R.id.allwordslist);
        allwordslist.setAdapter(adapter);
        allwordslist.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        allwordslist.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int i, long l) {
                TextView tv = (TextView) v;
                String [] word = tv.getText().toString().split(":");
                String positions = (mode!=BBSingleMode)?CommManagerMulti.getOnGrid(gridstr,word[0]):CommManager.getOnGrid(gridstr,word[0]);
                displayOnGrid(word[0].length(),positions);
            }
        });
    }

    private void populateOppList(){
        String[] words = CommManagerMulti.getOppWords().split("\\|");
        final CustomListAdapter adapter = new CustomListAdapter(this, android.R.layout.simple_list_item_1,words);
        final ListView allwordslist = (ListView) findViewById(R.id.opplist);
        allwordslist.setAdapter(adapter);
        allwordslist.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        allwordslist.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int i, long l) {
                /*
                TextView tv = (TextView) v;
                String [] word = tv.getText().toString().split(":");
                String positions = (mode!=BBSingleMode)?CommManagerMulti.getOnGrid(gridstr,word[0]):CommManager.getOnGrid(gridstr,word[0]);
                System.out.println(positions);
                displayOnGrid(word[0].length(),positions);*/
                TextView tv = (TextView) v;
                String [] word = tv.getText().toString().split(":");
                String positions = (mode!=BBSingleMode)?CommManagerMulti.getOnGrid(gridstr,word[0]):CommManager.getOnGrid(gridstr,word[0]);
                displayOnGrid(word[0].length(),positions);
            }
        });
    }

    void cleanGrid(){
        int i=0;
        for(Button[] a: gridbtns){
            for(Button b: a){
                b.setText(Character.toString(gridstr.charAt(i++)));
                b.setBackground(getResources().getDrawable(R.drawable.whitedie));
            }
        }
    }

    private void displayOnGrid(int length, String positions){
        System.out.println("length:"+length);
        String[] letters = positions.split("\\|");
        int i=0;
        int previousi = -1,previousj = -1, nexti = -1, nextj = -1;
        //int lastIndex = -1, newIndex = -1;
        int[] orderPosMap = new int[length];

        cleanGrid();

        //first pass through grid: maps the sequential order of the letters of the word with
        // the sequential position at which they are available on the grid
        for(int idx=0;idx<length;idx++){
            for(int idxj=0;idxj<letters.length;idxj++){
                if(letters[idxj].matches("(\\+|-)?[0-9]+") && (Integer.parseInt(letters[idxj]) == idx+1)){
                    orderPosMap[idx] = idxj ;
                    System.out.println("orderPosMap["+idx+"] = "+idxj);
                }
            }
        }
        //Second pass through the grid: setting arrows sequentially (with info from orderPosMap
        for(int idx = 0; idx<orderPosMap.length;idx++){
            //gridbtns[orderPosMap[idx] / size][orderPosMap[idx] % size].setText(Integer.toString(idx+1));
            previousi = nexti;
            previousj = nextj;
            nexti = orderPosMap[idx] / size;
            nextj = orderPosMap[idx] % size;
            setArrows(previousi, previousj, nexti, nextj);
            gridbtns[orderPosMap[idx] / size][orderPosMap[idx] % size].setBackground(getResources().getDrawable(R.drawable.yellowdie));
        }

    }

    void setArrows(int previousi, int previousj, int nexti, int nextj){
        if(previousi!= -1 || previousj != -1){
            int arrowindex = -1;
            if (nexti < previousi) {
                if (nextj < previousj) arrowindex = topleft;
                else if (nextj == previousj) arrowindex = topup;
                else arrowindex = topright;
            } else if (nexti == previousi) {
                if (nextj < previousj) arrowindex = midleft;
                else if (nextj == previousj) arrowindex = -1;
                else if(nextj>previousj) arrowindex = midright;
            } else {
                if (nextj < previousj) arrowindex = botleft;
                else if (nextj == previousj) arrowindex = botdown;
                else arrowindex = botright;
            }

            if(arrowindex == -1){
                gridbtns[nexti][nextj].setBackground(getResources().getDrawable(R.drawable.yellowdie));
            }
            else{
                gridbtns[previousi][previousj].setBackground(arrows[arrowindex]);
            }
        }

    }

    private void populateSmallGrid(){
        Button[][] smallbtns = new Button[BBEasyLevelSize][BBEasyLevelSize];

        smallbtns[0][0] = (Button) findViewById(R.id.smallbtns1);
        smallbtns[0][1] = (Button) findViewById(R.id.smallbtns2);
        smallbtns[0][2] = (Button) findViewById(R.id.smallbtns3);
        smallbtns[1][0] = (Button) findViewById(R.id.smallbtns4);
        smallbtns[1][1] = (Button) findViewById(R.id.smallbtns5);
        smallbtns[1][2] = (Button) findViewById(R.id.smallbtns6);
        smallbtns[2][0] = (Button) findViewById(R.id.smallbtns7);
        smallbtns[2][1] = (Button) findViewById(R.id.smallbtns8);
        smallbtns[2][2] = (Button) findViewById(R.id.smallbtns9);
        gridbtns = smallbtns;
        int i=0;
        for(Button[] a: gridbtns){
            for(Button b: a){
                b.setText(Character.toString(gridstr.charAt(i++)));
            }
        }

        
    }

    private void populateBigGrid(){
        Button[][] bigbtns = new Button[BBNormalLevelSize][BBNormalLevelSize];

        bigbtns[0][0] = (Button) findViewById(R.id.bigbtns1);
        bigbtns[0][1] = (Button) findViewById(R.id.bigbtns2);
        bigbtns[0][2] = (Button) findViewById(R.id.bigbtns3);
        bigbtns[0][3] = (Button) findViewById(R.id.bigbtns4);
        bigbtns[1][0] = (Button) findViewById(R.id.bigbtns5);
        bigbtns[1][1] = (Button) findViewById(R.id.bigbtns6);
        bigbtns[1][2] = (Button) findViewById(R.id.bigbtns7);
        bigbtns[1][3] = (Button) findViewById(R.id.bigbtns8);
        bigbtns[2][0] = (Button) findViewById(R.id.bigbtns9);
        bigbtns[2][1] = (Button) findViewById(R.id.bigbtns10);
        bigbtns[2][2] = (Button) findViewById(R.id.bigbtns11);
        bigbtns[2][3] = (Button) findViewById(R.id.bigbtns12);
        bigbtns[3][0] = (Button) findViewById(R.id.bigbtns13);
        bigbtns[3][1] = (Button) findViewById(R.id.bigbtns14);
        bigbtns[3][2] = (Button) findViewById(R.id.bigbtns15);
        bigbtns[3][3] = (Button) findViewById(R.id.bigbtns16);

        gridbtns = bigbtns;
        int i=0;
        for(Button[] a: gridbtns){
            for(Button b: a){
                b.setText(Character.toString(gridstr.charAt(i++)));
            }
        }

    }

    private class CustomListAdapter extends ArrayAdapter {

        private Context mContext;
        private int id;
        private String[] items ;

        public CustomListAdapter(Context context, int textViewResourceId , String[] list )
        {
            super(context, textViewResourceId, list);
            mContext = context;
            id = textViewResourceId;
            items = list ;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            TextView tv = (TextView) super.getView(position, convertView, parent);
                tv.setTextColor(Color.WHITE);

            return tv;
        }

    }



}
