package com.segames.boggle;

import android.util.Log;

/**
 * Created by SAMINA on 1/27/15.
 */
public class CommManager {
    public static String RequestNewGrid(int length){

        /* Replace with actual code*/
        RandomString generator = new RandomString(length);
        String[] letters = generator.nextString(); //assuming correct string comes in
        String str = "";
        for(int i=0;i<letters.length;i++)
        {
            str = str+letters[i];
        }
        Log.v("Server: ", str + "\n");
        return str;
    };
    public static String SendServer(String tag, String args){
        return Integer.toString(args.length());
    };


}
