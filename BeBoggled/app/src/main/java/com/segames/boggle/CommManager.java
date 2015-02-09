package com.segames.boggle;

import android.content.Context;
import android.util.Log;

/**
 * Created by SAMINA on 1/27/15.
 */
public class CommManager {
    private static BBWords BBWords1;

    public static String RequestNewGrid(int level, Context context){

        /*
        RandomString generator = new RandomString(length);
        String[] letters = generator.nextString(); //assuming correct string comes in
        String str = "";
        for(int i=0;i<letters.length;i++)
        {
            str = str+letters[i];
        }*/
        BBWords1 = new BBWords(context);
        String str = BBWords1.Grid(level);
        Log.v("Server: ", str + "\n");
        return str;
    };
    public static String SendServer(String tag, String arg){

        return Integer.toString(BBWords1.wordsValue(arg));
    };

    public static String getGridWords(){
         return BBWords1.getGridWords();
    }


}
