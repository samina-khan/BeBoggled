package com.segames.boggle;



import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;


public class DoublePlayer extends ActionBarActivity implements View.OnClickListener,GlobalConstants{


    static final int basic = 0;
    static int mode = basic;
    static final int cutthroat = 1;
    static final int normal_size = 4;
    //Game set-up utility variables
    static char[][] letters = null;
    static int game_boardSize=4;

    //Game state variables
    static boolean gameInProgress = false;
    static int score = 0;
    static int numRounds = 1;
    static String selection="";
    //XML components
    Button button_submit;
    Button button_exit;
    TextView my_wordlist;
    String my_list = "";
    TextView opp_wordlist;
    String opp_list = "";
    //static Button[][] game_buttons = new Button[normal_size][normal_size];
    Gameboard gameboard;
    CommManager mgr;
    int numPlayers;
    GestureDetector gestureDetector;
    //Shake-detection variables
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeEventManager mShakeDetector;

    /* OnCreate - All the start-up stuff here */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //BBWords1 = new BBWords(this);
        setContentView(R.layout.double_player);
        numPlayers = getIntent().getExtras().getInt("NumPlayers");
        gameboard = new Gameboard(normal_size);

        button_submit = (Button) findViewById(R.id.button_submit);
        button_submit.setOnClickListener(this);

        initializeBoardButtons();

        setShakeDetection();
        setDoubleTap();

        //startNewGame();


    }

    void setDoubleTap()
    {
        gestureDetector = new GestureDetector(this,new GestureDetector.SimpleOnGestureListener() {
            public boolean onDoubleTap(MotionEvent e) {
                //button_submit = (Button) findViewById(R.id.button_submit);
                gameboard.opaqueButtons(getDrawable(R.drawable.whitedie));
                int tempscore = wordscore(selection);
                if( tempscore > -1){
                    my_list = my_list.concat("\n"+selection);
                    Log.v("Tag",selection);
                    my_wordlist.setText(my_list);
                    score+=tempscore;
                    setScore(score);
                }
                gameboard.clearpreviousclick();
                selection="";
                return true;
            }
        });
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
            String serverreply = CommManager.SendServer("word",selection);
            score = Integer.parseInt(serverreply);
        }

        return score;

    }
    void shakeGrid(int length){

        if(!gameInProgress){
        //if(true){
            setAuxiliary();
            setGameBoard();
            startNewGame();
        }
        else{
            Log.v("gameinprogress: ",gameInProgress+"\n");
        }
    }

    /* startNewGame: clears the state and sets up for a new game */
    void startNewGame()
    {
        score = 0;
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

        //setGameBoard();

    }

    void setGameBoard()
    {
        String str = CommManager.RequestNewGrid(BBNormalLevel,this);
        gameboard.setGameboard(str);
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
                shakeGrid(game_boardSize*game_boardSize);
                startNewGame();
                button_submit.setVisibility(View.GONE);
            }
        });
    }

    void setAuxiliary()
    {

        my_wordlist = (TextView)findViewById(R.id.my_wordlist);
        if(my_wordlist != null)my_list = my_wordlist.getText().toString();
        opp_wordlist = (TextView)findViewById(R.id.opp_wordlist);
        if(opp_wordlist != null)opp_list = opp_wordlist.getText().toString();

        my_wordlist.setMovementMethod(new ScrollingMovementMethod());
        opp_wordlist.setMovementMethod(new ScrollingMovementMethod());
        TableLayout listtable = (TableLayout)findViewById(R.id.table2);
        listtable.setVisibility(View.VISIBLE);




        /*
        button_exit = (Button) findViewById(R.id.button_exit);
        button_exit.setOnClickListener(this);*/
    }

    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }


    @Override
    public void onClick(View v) {
        Button current_button= (Button) v;
        switch(v.getId()){
            case R.id.button_submit:
                shakeGrid(gameboard.size*gameboard.size);
                startNewGame();
                current_button.setVisibility(View.GONE);
                break;
            default:
                if(gameInProgress && gameboard.isvalidclick(current_button.getId())) {
                    Log.v("DoublePlayer:",current_button.getText().toString());
                    //current_button.getBackground().setAlpha(63);
                    current_button.setAlpha(0.25f);
                    selection = selection + current_button.getText();
                    gameboard.previousclick(current_button.getId());
                }
                else{
                   //do nothing? display message?
                }
        }
        /*
        if(current_button.getText().equals(getString(R.string.shake))) {
            shakeGrid(game_boardSize*game_boardSize);
            current_button.setText("Pressed!");
        }*/


    }
    void receiveOpponentWord()
    {
        String opp_string="Word";
        opp_list = opp_list.concat("\n"+opp_string);
        opp_wordlist.setText(opp_list);
    }
    /*
    void opaqueButtons()
    {
        for(Button[] g: gameboard.buttons)
            for(Button b:g)
                b.getBackground().setAlpha(127);
    }*/
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
