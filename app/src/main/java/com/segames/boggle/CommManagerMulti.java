package com.segames.boggle;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by SAMINA on 1/27/15.
 */

/*
    EA - Turned into a singleton class and made to use BBServeDouble instead
        of BBWords directly.
 */
public class CommManagerMulti implements GlobalConstants {

    private static Context context;
    private static CommManagerMulti instance = new CommManagerMulti();
    private static BluetoothChatService mChatService;

    private CommManagerMulti(){};

    public static CommManagerMulti getInstance() {
        return instance;
    }

    private static BBServerDouble BBServerDouble1;
    private static BBWords BBWords1;
    private static List clientWords = new ArrayList();
    private static List clientWords2 = new ArrayList();

    private static String grid_from_server_str;


    public static String RequestNewGrid(int level, Context context){
        BBWords1 = new BBWords(context);
        BBServerDouble1 = new BBServerDouble(level,BBDoubleCutMode,BBWords1);
        String str = BBWords1.Grid(level);
        clearlists();
        return str;
    }

    //tag argument unused: use as you see fit
    public static String SendServer(String tag, String arg){
        //int value = clientWords.contains(arg)?(-999):BBServer.wordsValue(arg);
        //if(value > 0){clientWords.add(arg);
        String rslt_str = BBServerDouble1.incomeWord(arg,BBPlayer_Me);
        if(rslt_str == wordAlreadyGuessed) return "-999";
        if(rslt_str == wordIncorrect) return "0";

        sendMessage(arg);
        return Integer.toString(BBWords1.wordsValue(arg));
    }

    public static void setChatService(BluetoothChatService bt1, Context c1) {
        mChatService = bt1;
        context = c1;
    }

    // Empty until the shake occurs from the Server. Only used by the client
    public static String getGridFromServer() {
        String str = "";
        return str;
    }

    public static void writeOppWord(String opp_word) {
        BBServerDouble1.incomeWord(opp_word, BBPlayer_Other);
    }

    public static String getGridWords(){
         return BBWords1.getGridWords();
    }

    /* Change required: Server provides opponent's words */
    public static String getOppWords(){
        HashMap gridWords = BBServerDouble1.getGuessedWordsP2();
        Set set = gridWords.entrySet();
        Iterator i = set.iterator();
        String str = "";
        while(i.hasNext()) {
            Map.Entry<String,Integer> me = (Map.Entry)i.next();
            str += me.getKey() + ": " + me.getValue() + "|";
        }
        return str;
    }

    private static void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(context, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mChatService.write(send);
        }
    }

    public static void clearlists(){
        clientWords.clear(); clientWords2.clear();
    }
    public static String getOnGrid(String grid, String word) {
        return BBWords1.annotatedGrid(grid,word);
    }

}
