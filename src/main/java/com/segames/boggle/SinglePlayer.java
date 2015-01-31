package com.segames.boggle;



import android.content.Context;
import android.gesture.GestureOverlayView;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class SinglePlayer extends ActionBarActivity implements View.OnClickListener{

    //XML components
    Button button_submit;
    Button button_exit;
    TextView my_wordlist;
    String my_list = "";


    //Game set-up utility variables
    static char[][] letters = null;
    static int game_boardSize=3;
    static final int maxEasyRounds = 3;
    static final int easy = 0;
    static final int normal = 1;
    static final int easy_size = 3;
    static final int normal_size = 4;
    static Button[][] game_buttons = new Button[normal_size][normal_size];


    //Game state variables
    static boolean gameInProgress = false;
    static int score = 0;
    static int numRounds = 1;
    static int level = easy;
    static String selection = new String();

    //Shake-detection variables
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeEventManager mShakeDetector;
    GestureDetector gestureDetector;

    /* OnCreate - All the start-up stuff here */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_playeralt);
        my_wordlist = (TextView)findViewById(R.id.my_wordlist);
        if(my_wordlist != null) my_list = my_wordlist.getText().toString();
        my_wordlist.setMovementMethod(new ScrollingMovementMethod());

        initializeBoardButtons();
        setAuxiliaryButtons();

        setShakeDetection();
        setDoubleTap();

        startNewGame();


    }

    void setDoubleTap()
    {
        gestureDetector = new GestureDetector(this,new GestureDetector.SimpleOnGestureListener() {
            public boolean onDoubleTap(MotionEvent e) {
                opaqueButtons();
                if(wordOK(selection)){
                my_list = my_list.concat("\n"+selection);
                my_wordlist.setText(my_list);}
                selection="";
                return true;
            }
        });
    }
    boolean wordOK(String word)
    {
        //Pass to server
        return true;
    }

    void shakeGrid(int length){

        if(!gameInProgress){
            setGameBoard();
            startNewGame();
        }
    }

    /* startNewGame: clears the state and sets up for a new game */
    void startNewGame()
    {
        score = 0;

        if(numRounds <= maxEasyRounds) level = easy;
        else level = normal;

        if(level == easy){ game_boardSize = easy_size; hideButtons();}
        else { game_boardSize = normal_size; showButtons();}

        gameInProgress=true;

    }

    void hideButtons(){
        for(int i=0; i<=3; i++)
            for(int j=0;j<=3; j++)
                if(i==3 || j==3)
                game_buttons[i][j].setVisibility(View.GONE);
    }

    void showButtons(){
        for(int i=0; i<=3; i++)
            for(int j=0;j<=3; j++)
                if(i==3 || j==3)
                    game_buttons[i][j].setVisibility(View.VISIBLE);
    }

    void initializeBoardButtons()
    {
        //find buttons
        game_buttons[0][0]= (Button) findViewById(R.id.button_0);
        game_buttons[0][1]= (Button) findViewById(R.id.button_1);
        game_buttons[0][2]= (Button) findViewById(R.id.button_2);
        game_buttons[0][3]= (Button) findViewById(R.id.button_3);
        game_buttons[1][0]= (Button) findViewById(R.id.button_4);
        game_buttons[1][1]= (Button) findViewById(R.id.button_5);
        game_buttons[1][2]= (Button) findViewById(R.id.button_6);
        game_buttons[1][3]= (Button) findViewById(R.id.button_7);
        game_buttons[2][0]= (Button) findViewById(R.id.button_8);
        game_buttons[2][1]= (Button) findViewById(R.id.button_9);
        game_buttons[2][2]= (Button) findViewById(R.id.button_10);
        game_buttons[2][3]= (Button) findViewById(R.id.button_11);
        game_buttons[3][0]= (Button) findViewById(R.id.button_12);
        game_buttons[3][1]= (Button) findViewById(R.id.button_13);
        game_buttons[3][2]= (Button) findViewById(R.id.button_14);
        game_buttons[3][3]= (Button) findViewById(R.id.button_15);

        //set onClick Listener
        for(int i=0; i< 4; i++)
        {
            for(int j=0; j< 4;j++){
            game_buttons[i][j].setOnClickListener(this);
            game_buttons[i][j].setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        return gestureDetector.onTouchEvent(event);
                    }
                });
            }
          /* insert code - dynamically change height of button to match width of button
            to go with all screen sizes */
        }

        setGameBoard();

    }

    void setGameBoard()
    {
        int max_loop = 3;
        if(level == normal) max_loop = 4;

        RandomString generator = new RandomString(max_loop*max_loop);
        String[] letters1 = generator.nextString(); //assuming correct string comes in
        //String[] letters1="abcdefghijklmnop";
        for(int i = 0, k = 0; i < max_loop; i++){
            for(int j = 0; j < max_loop; j++){
                game_buttons[i][j].setText(letters1[k++]);
            }
        }


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
            }
        });
    }

    void setAuxiliaryButtons()
    {
        button_submit = (Button) findViewById(R.id.button_submit);
        button_submit.setOnClickListener(this);

        button_exit = (Button) findViewById(R.id.button_exit);
        button_exit.setOnClickListener(this);
    }

    void sendToServer(String s)
    {
        Log.println(1,"sendToServer:","Selection is: " + s);
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
                opaqueButtons();
                current_button.setText(selection);
                selection="";
                break;
            default:
                current_button.getBackground().setAlpha(100);
                selection = selection + current_button.getText();
        }
        /*
        if(current_button.getText().equals(getString(R.string.shake))) {
            shakeGrid(game_boardSize*game_boardSize);
            current_button.setText("Pressed!");
        }*/


    }
    void opaqueButtons()
    {
        for(Button[] g: game_buttons)
            for(Button b:g)
                b.getBackground().setAlpha(255);
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
