package com.segames.boggle;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SAMINA on 1/27/15.
 */
public class CommManagerMulti {
    private static BBWords BBWords1;
    private static List clientWords = new ArrayList();
    private static List clientWords2 = new ArrayList();


    public static String RequestNewGrid(int level, Context context){
        BBWords1 = new BBWords(context);
        String str = BBWords1.Grid(level);
        clearlists();
        return str;
    }

    //tag argument unused: use as you see fit
    public static String SendServer(String tag, String arg){
        int value = clientWords.contains(arg)?(-999):BBWords1.wordsValue(arg);
        if(value > 0){clientWords.add(arg);}
        return Integer.toString(value);
    }

    public static String getGridWords(){
         return BBWords1.getGridWords();
    }

    /* Change required: Server provides opponent's words */
    public static String getOppWords(){
        return BBWords1.getGridWords();
    }

    public static void clearlists(){
        clientWords.clear(); clientWords2.clear();
    }
    public static String getOnGrid(String grid, String word) {
        return BBWords1.annotatedGrid(grid,word);
    }

}
