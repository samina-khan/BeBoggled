/*Double Player Basic Mode */
package com.segames.boggle;



import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;


public class DoublePlayer extends ActionBarActivity implements View.OnClickListener,GlobalConstants{

    private final long startTime = BBGameTime * 1000;
    private final long interval = 1 * 1000;
    private CountDownTimer countDownTimer;
    private CountDownTimer poller;
    private CommManagerMulti commManagerMulti1 = CommManagerMulti.getInstance();


    //Game state variables
    static boolean gameInProgress = false;
    static int score = 0;
    static int oppscore = 0;
    static int numRounds = 1;
    static int role;
    static String selection="";

    //XML components
    Button button_submit;
    TextView my_wordlist;
    TextView timer;
    String my_list = "";
    TextView opp_wordlist;
    String opp_list = "";
    private String gridstr;

    Gameboard gameboard;
    GestureDetector gestureDetector;

    //Shake-detection variables
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeEventManager mShakeDetector;


    Drawable[] arrows = new Drawable[9];
    Animation rotation;

    /* OnCreate - All the start-up stuff here */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.double_playeralt);


        /* Getting arguments from previous screen */
        numRounds = getIntent().getExtras().getInt("Round");
        score = getIntent().getExtras().getInt("Score");
        role = getIntent().getExtras().getInt("Role");
        System.out.println("Role: "+role);

        Log.v("Round",Integer.toString(numRounds));
        gameboard = new Gameboard(BBNormalLevelSize);


        button_submit = (Button) findViewById(R.id.button_submit);
        button_submit.setOnClickListener(this);



        initializeBoardButtons();


        setTimer(this);
        setShakeDetection();
        setDoubleTap();

        TextView roundtext = (TextView) findViewById(R.id.round);
        roundtext.setText(Integer.toString(numRounds));

        TextView scoretext = (TextView) findViewById(R.id.score);
        scoretext.setText(Integer.toString(score));
        arrows= new Drawable[]{getResources().getDrawable(R.drawable.yellowtopleft), getResources().getDrawable(R.drawable.yellowup_alt), getResources().getDrawable(R.drawable.yellowtopright),
                getResources().getDrawable(R.drawable.yellowleft_alt), getResources().getDrawable(R.drawable.yellowdie),getResources().getDrawable(R.drawable.yellowright_alt), getResources().getDrawable(R.drawable.yellowbottomleft), getResources().getDrawable(R.drawable.yellowdown_alt), getResources().getDrawable(R.drawable.yellowbottomright)};


    }

    void setTimer(Context c){
        countDownTimer = new CountDownTimerActivity(startTime, interval,c);
        timer = (TextView) findViewById(R.id.timer);
        timer.setText("" + String.format("%02d",((startTime/1000)/60))+":"+String.format("%02d",((startTime/1000)%60)));
    }
    @Override
    public void onBackPressed() {
    }

    void setDoubleTap()
    {
        gestureDetector = new GestureDetector(this,new GestureDetector.SimpleOnGestureListener() {
            public boolean onDoubleTap(MotionEvent e) {
                wordfinalize();
                return true;
            }
        });
    }

    void wordfinalize(){


        if(selection.length() >= BBMinWordLength) {
            gameboard.opaqueButtons(getResources().getDrawable(R.drawable.whitedie));
            int tempscore = wordscore(selection);
            if (tempscore == -888 || tempscore > 0) {
                my_list = my_list.concat(selection+"\n");
                Log.v("Tag", selection);
                my_wordlist.setText(my_list);
                score += tempscore;
                setScore(score);
            } else {
                String str = (tempscore == -999 ) ? "Selected!" : "Bad Word!";

                MediaPlayer mp = MediaPlayer.create(this,R.raw.glass_ping);
                mp.start();
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.toast_layout,
                        (ViewGroup) findViewById(R.id.toast_layout_root));

                TextView text = (TextView) layout.findViewById(R.id.text);
                text.setText(str);

                Toast toast = new Toast(getApplicationContext());
                //toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();
                /*
                Toast toast = Toast.makeText(getApplicationContext(), str,
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP | Gravity.LEFT, 400, 400);
                toast.show();*/
            }
            gameboard.clearpreviousclick();
            selection = "";
        }

    }

    void setScore(int score)
    {
        TextView scoretxt = (TextView) findViewById(R.id.score);
        scoretxt.setText(Integer.toString(score));

    }

    int wordscore(String word)
    {
        int score = -1;

        if(selection.length()>=3)
        {
            String serverreply = commManagerMulti1.SendServer("word",selection);
            score = Integer.parseInt(serverreply);
        }

        return score;

    }
    void shakeGrid(int length){

        if(!gameInProgress){
            setAuxiliary();
            setGameBoard();
            startNewGame();
            countDownTimer.start();
        }
    }

    /* startNewGame: clears the state and sets up for a new game */
    void startNewGame()
    {
        gameInProgress=true;

    }


    void initializeBoardButtons()
    {
        //find buttons
        gameboard.buttons[0][0]= (Button) findViewById(R.id.button_0);
        gameboard.buttons[0][1]= (Button) findViewById(R.id.button_1);
        gameboard.buttons[0][2]= (Button) findViewById(R.id.button_2);
        gameboard.buttons[0][3]= (Button) findViewById(R.id.button_3);
        gameboard.buttons[1][0]= (Button) findViewById(R.id.button_4);
        gameboard.buttons[1][1]= (Button) findViewById(R.id.button_5);
        gameboard.buttons[1][2]= (Button) findViewById(R.id.button_6);
        gameboard.buttons[1][3]= (Button) findViewById(R.id.button_7);
        gameboard.buttons[2][0]= (Button) findViewById(R.id.button_8);
        gameboard.buttons[2][1]= (Button) findViewById(R.id.button_9);
        gameboard.buttons[2][2]= (Button) findViewById(R.id.button_10);
        gameboard.buttons[2][3]= (Button) findViewById(R.id.button_11);
        gameboard.buttons[3][0]= (Button) findViewById(R.id.button_12);
        gameboard.buttons[3][1]= (Button) findViewById(R.id.button_13);
        gameboard.buttons[3][2]= (Button) findViewById(R.id.button_14);
        gameboard.buttons[3][3]= (Button) findViewById(R.id.button_15);

        //set onClick Listener
        for(int i=0; i< 4; i++)
        {
            for(int j=0; j< 4;j++){
                gameboard.buttons[i][j].setOnClickListener(this);
               gameboard.buttons[i][j].setOnTouchListener(new View.OnTouchListener() {

                    public boolean onTouch(View v, MotionEvent event) {

                        return gestureDetector.onTouchEvent(event);
                    }
                });
            }
          /* insert code - dynamically change height of button to match width of button
            to go with all screen sizes */
        }

    }

    void startWobble(){
        rotation = AnimationUtils.loadAnimation(this, R.anim.wobble);
        rotation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                startWobbleSlow();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        for(Button[] a: gameboard.buttons){
            for(Button b: a){
                b.startAnimation(rotation);
            }

        }
    }
    void startWobbleSlow(){
        rotation = AnimationUtils.loadAnimation(this, R.anim.wobbleslow);
        rotation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                gameboard.setGameboard(gridstr);
                countDownTimer.start();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        for(Button[] a: gameboard.buttons){
            for(Button b: a){
                b.startAnimation(rotation);
            }

        }
    }
    void setGameBoard()
    {
        startWobble();
        String str = "";
        str = CommManagerMulti.getGridFromServer(role, BBNormalLevel,BBDoubleBasicMode, this);
        Log.v("strlen",Integer.toString(str.length()));
        gridstr=str;
        //gameboard.setGameboard(str);
    }

    void setShakeDetection()
    {
        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeEventManager();
        mShakeDetector.setOnShakeListener(new ShakeEventManager.OnShakeListener() {

            @Override
            public void onShake(int count) {
                Toast.makeText(getApplicationContext(), "Shaken!", Toast.LENGTH_SHORT).show();
                shakeGrid(gameboard.size*gameboard.size);
                button_submit.setVisibility(View.GONE);
                findViewById(R.id.overlay).setVisibility(View.GONE);
            }
        });
    }

    void setAuxiliary()
    {

        my_wordlist = (TextView)findViewById(R.id.my_wordlist);
        if(my_wordlist != null){my_list = my_wordlist.getText().toString();my_wordlist.setMovementMethod(new ScrollingMovementMethod());}
        opp_wordlist = (TextView)findViewById(R.id.opp_wordlist);
        if(opp_wordlist != null){opp_list = opp_wordlist.getText().toString();opp_wordlist.setMovementMethod(new ScrollingMovementMethod());}

        //my_wordlist.setMovementMethod(new ScrollingMovementMethod());
        //opp_wordlist.setMovementMethod(new ScrollingMovementMethod());
        TableLayout listtable = (TableLayout)findViewById(R.id.table2);
        listtable.setVisibility(View.VISIBLE);

    }

    @Override
    public void onResume() {
        super.onResume();
        // registering the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        //unregistering the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }


    @Override
    public void onClick(View v) {
        Button current_button= (Button) v;
        switch(v.getId()){
            case R.id.button_submit:
                shakeGrid(gameboard.size*gameboard.size);
                //startNewGame();
                current_button.setVisibility(View.GONE);
                findViewById(R.id.overlay).setVisibility(View.GONE);
                break;
            default:
                if(gameInProgress && gameboard.isvalidclick(current_button.getId())) {
                    int arrow_index = gameboard.getArrow(current_button.getId());
                    current_button.setBackground(getResources().getDrawable(R.drawable.yellowdie));
                    if(arrow_index!=-1)gameboard.setArrow(arrows[arrow_index]);

                    selection = selection + current_button.getText();
                    gameboard.previousclick(current_button.getId());
                }
                else{
                    //do what?

                }
        }
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

    void populateOppInfo(){
        oppscore = CommManagerMulti.getOppScore();
        TextView score_opp = (TextView) findViewById(R.id.score_opp);
        if(score_opp!= null) score_opp.setText(Integer.toString(oppscore));

        String[] oppwords = CommManagerMulti.getOppWordsList().split("\\|");
        String str = "";
        for(String word: oppwords){
            str = str + word + "\n";
        }
        if(opp_wordlist!=null)opp_wordlist.setText(str);

    }

    public class CountDownTimerActivity extends CountDownTimer {
        Context context;
        public CountDownTimerActivity(long startTime, long interval, Context context) {
            super(startTime, interval);
            this.context=context;
        }

        @Override
        public void onFinish() {
            if(gameInProgress) {
                Intent scoreIntent = new Intent(context, Score.class);

                scoreIntent.putExtra("Score", score);
                scoreIntent.putExtra("OppScore", oppscore);
                scoreIntent.putExtra("Round", numRounds);
                scoreIntent.putExtra("Mode", BBDoubleBasicMode);
                scoreIntent.putExtra("Grid", gridstr);
                scoreIntent.putExtra("Role", role);
                gameInProgress = false;
                startActivity(scoreIntent);
            }
        }


        @Override
        public void onTick(long millisUntilFinished) {
            if(gameInProgress) {
                populateOppInfo();
                timer = (TextView) findViewById(R.id.timer);
                if (millisUntilFinished / 1000 == 30) {
                    timer.setTextColor(Color.RED);
                }
                timer.setText("" + String.format("%02d", ((millisUntilFinished / 1000) / 60)) + ":" + String.format("%02d", ((millisUntilFinished / 1000) % 60)));
            }
        }
    }
}
