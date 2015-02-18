package com.segames.boggle;



import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class Score extends ActionBarActivity implements View.OnClickListener,GlobalConstants{

    Button button_single;
    Button button_main;
    Button[][] gridbtns;
    Drawable[] arrows = new Drawable[9];
    int size = -1;

    int round=1;
    int score=0;
    int mode = -1;
    String gridstr = "";
    boolean isBackVisible = false;
    AnimatorSet setRightOut;
    AnimatorSet setLeftIn;
    TableLayout table;




    /*swipe variables*/
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;
    private Animation slideLeftIn;
    private Animation slideLeftOut;
    private Animation slideRightIn;
    private Animation slideRightOut;
    private ViewFlipper viewFlipper;
    /*end of swipe variables*/

    /* OnCreate - All the start-up stuff here */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score);

        //viewFlipper = (ViewFlipper)findViewById(R.id.flipper);
        //slideLeftIn = AnimationUtils.loadAnimation(this, R.animator.slide_left_in);
        //slideLeftOut = AnimationUtils.loadAnimation(this, R.anim.slide_left_out);
        //slideRightIn = AnimationUtils.loadAnimation(this, R.anim.slide_right_in);
        //slideRightOut = AnimationUtils.loadAnimation(this, R.anim.slide_right_out);
        gestureDetector = new GestureDetector(new MyGestureDetector());
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                    return true;
                }
                return false;
            }
        };
        /* Old flip animation
        setRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(),
                R.animator.card_flip_right_out);

        setLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(),
                R.animator.card_flip_left_in);*/

        score = getIntent().getExtras().getInt("Score");
        round = getIntent().getExtras().getInt("Round");
        mode = getIntent().getExtras().getInt("Mode");
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
            populateBigGrid();
            size= BBNormalLevelSize;
        }


        button_single = (Button) findViewById(R.id.button_single);
        button_single.setOnClickListener(this);
        button_main = (Button) findViewById(R.id.button_main);
        button_main.setOnClickListener(this);

        TextView scoreval = (TextView)findViewById(R.id.ScoreVal);
        TextView roundval = (TextView)findViewById(R.id.RoundVal);
        scoreval.setText("Great, Your Score is "+score);
        roundval.setText("Round: "+round);

        populateList();
        populateOppList();

        ImageView greenarrows = (ImageView) findViewById(R.id.greenarrows);
        greenarrows.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                if (!isBackVisible) {
                    setRightOut.setTarget((LinearLayout) findViewById(R.id.layout_front));
                    setLeftIn.setTarget((LinearLayout) findViewById(R.id.layout_back));
                    setRightOut.start();
                    setLeftIn.start();
                    isBackVisible = true;
                }*/
                findViewById(R.id.layout_back).setVisibility(View.VISIBLE);
                findViewById(R.id.layout_front).setVisibility(View.GONE);


            }
        });
        ImageView greenarrows2 = (ImageView) findViewById(R.id.greenarrows2);
        greenarrows2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                findViewById(R.id.layout_back).setVisibility(View.GONE);
                findViewById(R.id.layout_front).setVisibility(View.VISIBLE);
            }
        });

        arrows= new Drawable[]{getDrawable(R.drawable.yellowtopleft), getDrawable(R.drawable.yellowup_alt), getDrawable(R.drawable.yellowtopright),
                getDrawable(R.drawable.yellowleft_alt), getDrawable(R.drawable.yellowdie),getDrawable(R.drawable.yellowright_alt), getDrawable(R.drawable.yellowbottomleft), getDrawable(R.drawable.yellowdown_alt), getDrawable(R.drawable.yellowbottomright)};


    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event))
            return true;
        else
            return false;
    }

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
                        Intent doubleIntent = new Intent(v.getContext(), DoublePlayerAlt.class);
                        doubleIntent.putExtra("Round",round+1);
                        doubleIntent.putExtra("Score",score);
                        startActivity(doubleIntent);
                        break;
                    case BBDoubleCutMode:
                        //startActivity(cutThroatIntent);
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
        String[] words = CommManager.getGridWords().split("\\|");
        final CustomListAdapter adapter = new CustomListAdapter(this, android.R.layout.simple_list_item_1,words);
        final ListView allwordslist = (ListView) findViewById(R.id.allwordslist);
        allwordslist.setAdapter(adapter);
        allwordslist.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        allwordslist.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int i, long l) {
                TextView tv = (TextView) v;
                String positions = CommManager.getOnGrid(gridstr,tv.getText().toString());
                //displayOnGrid(tv.getText().toString().length(),positions);
                displayOnGrid(3,positions);
            }
        });
    }

    private void populateOppList(){
        String[] words = CommManager.getGridWords().split("\\|");
        final CustomListAdapter adapter = new CustomListAdapter(this, android.R.layout.simple_list_item_1,words);
        final ListView allwordslist = (ListView) findViewById(R.id.opplist);
        allwordslist.setAdapter(adapter);
        allwordslist.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        allwordslist.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int i, long l) {
                TextView tv = (TextView) v;
                tv.setTextColor(Color.GREEN);
                String positions = CommManager.getOnGrid(gridstr,tv.getText().toString());
                //displayOnGrid(tv.getText().toString().length(),positions);
                displayOnGrid(3,positions);
            }
        });
    }

    private void displayOnGrid(int length, String positions){
        String[] letters = positions.split("\\|");
        int i=0;
        int previousi = -1,previousj = -1, nexti = -1, nextj = -1;
        //int lastIndex = -1, newIndex = -1;
        int[] orderPosMap = new int[length];

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
            gridbtns[orderPosMap[idx] / size][orderPosMap[idx] % size].setText(Integer.toString(idx+1));
            previousi = nexti;
            previousj = nextj;
            nexti = orderPosMap[idx] / size;
            nextj = orderPosMap[idx] % size;
            setArrows(previousi, previousj, nexti, nextj);
            gridbtns[orderPosMap[idx] / size][orderPosMap[idx] % size].setBackground(getDrawable(R.drawable.yellowdie));
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
                gridbtns[nexti][nextj].setBackground(getDrawable(R.drawable.yellowdie));
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
    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                    // right to left swipe
                    if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                        viewFlipper.setInAnimation(slideLeftIn);
                        viewFlipper.setOutAnimation(slideLeftOut);
                        viewFlipper.showNext();
                    }
                    else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                        viewFlipper.setInAnimation(slideRightIn);
                        viewFlipper.setOutAnimation(slideRightOut);
                        viewFlipper.showPrevious();
                    }
            }
            catch (Exception e) {                 // nothing
            }
            return false;
        }
    }


}
