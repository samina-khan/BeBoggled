/* Choice for whether server or client */
package com.segames.boggle;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


public class SetUpServerClient extends ActionBarActivity implements View.OnClickListener,GlobalConstants{

    private static final String TAG = "SetUpdServerClient";
    /**
     * Local Bluetooth adapter
     */
    private BluetoothAdapter mBluetoothAdapter = null;

    /**
     * Member object for the chat services
     */
    private BluetoothChatService mChatService = null;

    /**
     * Name of the connected device
     */
    private String mConnectedDeviceName = null;


    // Intent request codes for Bluetooth Chat Server
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    Context context;

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

        context = this;

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth is not available", Toast.LENGTH_LONG).show();
        }
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

                Intent serverIntent = new Intent(context,DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);

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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    //setupChat();
                } else {
                    // User did not enable Bluetooth or an error occurred
                    // TODO
                    //Log.d(TAG, "BT not enabled");
                    //Toast.makeText(getActivity(), R.string.bt_not_enabled_leaving,
                    //        Toast.LENGTH_SHORT).show();
                    //getActivity().finish();
                }
        }
    }

    private void setStatus(CharSequence subTitle) {
        Log.v(TAG, "setStatus" + subTitle);
    }

    public void onStart() {
        super.onStart();
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else if (mChatService == null) {
            setupChat();
        }
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Context context = getApplicationContext();
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            setStatus("connecting");
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            setStatus("not connected");
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    Log.v(TAG,"Me: " + writeMessage);
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    Log.v(TAG,"Me: " + readMessage);
                    if (null != context) {
                        Toast.makeText(context, "Other player says " + readMessage, Toast.LENGTH_SHORT).show();
                    }
/*                    if(role)
                    if(readMessage.length() == 16) {
                        CommManagerMulti.setGrid();
                    }*/
                    CommManagerMulti.writeOppWord(readMessage);
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    Log.v(TAG,"Me: " + mConnectedDeviceName);
                    if (null != context) {
                        Toast.makeText(context, "Connected to "
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.MESSAGE_TOAST:
                    if (null != context) {
                        Toast.makeText(context, msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    private void setupChat() {
        Log.v(TAG, "setupChat()");

        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothChatService(getApplicationContext(), mHandler);
        CommManagerMulti.setChatService(mChatService,context);
    }


    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras()
                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mChatService.connect(device, secure);
    }

    /* EDWARD: My guess is that you want to put in the Device_list menu stuff here */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_main, menu);
        /*

        Intent serverIntent = new Intent(context,DeviceListActivity.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
        */
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
