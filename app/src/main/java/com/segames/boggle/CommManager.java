package com.segames.boggle;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SAMINA on 1/27/15.
 */
public class CommManager {
    private static BBWords BBWords1;
    private static ArrayList<String> clientWords = new ArrayList<String>();

    public static String RequestNewGrid(int level, Context context){
        BBWords1 = new BBWords(context);
        String str = BBWords1.Grid(level);
        clientWords.clear();
        return str;
    }

    public static String SendServer(String tag, String arg){
        int value = clientWords.contains(arg)?(-999):BBWords1.wordsValue(arg);
        if(value > 0){clientWords.add(arg);}
        return Integer.toString(value);
    }

    public static String getGridWords(){
         return BBWords1.getGridWords();
    }
    public static String getYourWords(){
        String words = "";
        for(String word: clientWords){
            words = words + word + ":|";
        }
        return words;
    }
    public static void clearlist(){clientWords.clear();}
    public static String getOnGrid(String grid, String word) {return BBWords1.annotatedGrid(grid,word);}
}
