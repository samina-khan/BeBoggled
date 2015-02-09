package com.segames.boggle;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.io.Console;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * BeBoggled packages
*/
//import bbglobal.GlobalConstants;


/**
 *
 * @author EA
 */
public class BBWords implements GlobalConstants {

    private Context context;
    private final HashMap<String,Integer> validWords = new HashMap();
    private HashMap<String,Integer> gridWords = null;
    private int current_level = -1;
    private int current_size = -1;
    private int minimum_valid_words = -1;
    private int valid_words_count = -1;
    private int max_word_len = -1;

    private final static int MAX_GRID_SIZE = 4;
    private char[][] grid;

    public BBWords(Context context) {
        this.context = context;
        // Open the valid words file and load it into the hash table
        if(LoadWordsFile() == BBWordsFailure){
            /*
            TODO: General message box for failure recovery
                e.g. "Could not find file 'BBWords.txt'. Please put in BBWords folder and try restarting game."
            */
            System.out.println("Could not find file 'BBWords.txt'. Please put in BBWords folder and try restarting game.");
            return;
        };
        grid = new char[MAX_GRID_SIZE][MAX_GRID_SIZE];
    }

    private void randomize_grid() {
        for (int r = 0; r < current_size; r++) {
            for (int c = 0; c < current_size; c++) {
                grid[r][c] = (char)('a' + (int)(Math.random()*26));
            }
        }
    }

    private String get_grid_str() {
        String str = "";
        for (int i = 0; i < current_size; i++) {
            for (int j = 0; j < current_size; j++) {
                str += grid[i][j];
            }
        }
        return str;
    }

    public void dumpGrid() {
        for(int i = 0;i < current_size;i++) {
            for (char letter : grid[i]) {
                System.out.print(letter + " ");
            }
            if (i < current_size - 1) {
                System.out.print("\n");
            }
        }
        System.out.println();
    }

    public String Grid(int level){
        current_level = level;
        switch(current_level) {
            case BBEasyLevel:
                current_size = BBEasyLevelSize;
                break;
            case BBNormalLevel:
                current_size = BBNormalLevelSize;
                break;
            default:
                current_size = -1;
        }

        gridWords = new HashMap();
        while(true) {
            randomize_grid();

            // find all words in validWords in the grid
            find_all_grid_words();

            if(current_level == BBEasyLevel && gridWords.size() >= BBEasyLevelNoWords)
                break;

            if(current_level == BBNormalLevel && gridWords.size() >= BBNormalLevelNoWords)
                break;

            // try again
            System.out.println("re-randomizing");
            gridWords.clear();
        }

        return get_grid_str();
    }

    // Used by the method LoadWordFile to create the validWords hash map
    private final int calculate_word_value(String word) {
        int word_len = word.length();
        int word_val = 0;
        switch(word_len) {
            case 0: word_val = 0;
                break;
            case 1: word_val = 0;
                break;
            case 2: word_val = 0;
                break;
            case 3: word_val = 1;
                break;
            case 4: word_val = 1;
                break;
            case 5: word_val = 2;
                break;
            case 6: word_val = 3;
                break;
            case 7: word_val = 5;
                break;
            default: word_val = 11; // words of 8 letters or more
        }
        return word_val;
    }

    /*
        Just returns true or false if the word exists in the list of valid words
    */
    public Boolean isWordValid(String candidateWord) {
        if(validWords.get(candidateWord) == null)
            return false;
        else
            return true;
    }

    /*
        If the word is greater than 9 letters and the level is Easy,
            then its value is negative 500.
        If the word is greater than 16 letters and the level is Normal,
            then its value is negative 1000.
        If the word is not in the list then, its value is zero.
        Otherwise, the value of the word is obtained from the validWords hash map.
    */
    public int wordsValue(String candidateWord) {

        int word_len = candidateWord.length();
        if(current_level == BBEasyLevel && word_len > BBMaxWordLenEasyLevel)
            return -500;
        if(current_level == BBNormalLevel && word_len > BBMaxWordLenNormalLevel)
            return -1000;

        // Length of word checking is completed so we now use the hash map

        // If the word exists, use the value assigned
        // Otherwise, return a value of 0

        /*(validWords.get(candidateWord))? return :*/
        //Log.e("word_len",Integer.toString(word_len));
        if(validWords.get(candidateWord) != null){
        return validWords.get(candidateWord);}
        else return 0;
    }

    private int LoadWordsFile() {

        try {
            InputStream inputStream = context.getResources().openRawResource(R.raw.bbwords);
            //BufferedReader buffReader = new BufferedReader(new FileReader(BBValidWordsFile));
            BufferedReader buffReader = new BufferedReader(new InputStreamReader(inputStream));
            String word;
            while ((word = buffReader.readLine()) != null)
            {
                if(word.length() > 2) {
                    validWords.put(word,calculate_word_value(word));
                    valid_words_count++;
                    if(word.length() > max_word_len)
                        max_word_len = word.length();
                }
            }
            buffReader.close();
        } catch (Exception e) {
            Log.e("LoadWordsFile():", "Exception occurred while trying to open the valid words file !!!\n");
            //e.printStackTrace();
            return BBWordsFailure;
        }

        /*
        *   Words were loaded into hash array successfully
        */
        return BBWordsSuccess;
    }

    public boolean searchForWord(String word) {
        for(int i = 0; i < current_size; i++) {
            for(int j = 0; j < current_size; j++) {
                if(gridSearch(word, i, j)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean gridSearch(String str, int i, int j) {

        // If str is empty, we have consumed the whole word
        if(str.equals("")) return true;

        if(i < 0 || i >= current_size) return false;

        if(j < 0 || j >= current_size) return false;

        if(grid[i][j] != str.charAt(0)) return false;

        // We mark the current letter of the string as visited
        char tmp = grid[i][j];  grid[i][j] = '#';

        // Get the tail of the string
        String substr = str.substring(1, str.length());

        // Try NW, N, NE, E, SE, S, SW, W consecutively
        boolean rslt = gridSearch(substr, i-1, j-1) ||
                gridSearch(substr, i-1,   j) ||
                gridSearch(substr, i-1, j+1) ||
                gridSearch(substr,   i, j-1) ||
                gridSearch(substr,   i, j+1) ||
                gridSearch(substr, i+1, j-1) ||
                gridSearch(substr, i+1,   j) ||
                gridSearch(substr, i+1, j+1);

        // Restore the letter as we unrecurse
        grid[i][j] = tmp;

        return rslt;
    }

    public String getGridWords() {
        Set set = gridWords.entrySet();
        Iterator i = set.iterator();
        String str = "";
        while(i.hasNext()) {
            Map.Entry<String,Integer> me = (Map.Entry)i.next();
            str += me.getKey() + ": " + me.getValue() + "|";
        }
        return str;
    }

    public void dumpGridWords() {
        Set set = gridWords.entrySet();
        Iterator i = set.iterator();
        while(i.hasNext()) {
            Map.Entry<String,Integer> me = (Map.Entry)i.next();
            System.out.print(me.getKey() + ": ");
            System.out.println(me.getValue());
        }
    }

    public void find_all_grid_words() {

        Set set = validWords.entrySet();
        Iterator i = set.iterator();
        while(i.hasNext()) {
            Map.Entry<String,Integer> me = (Map.Entry)i.next();
            String word = me.getKey();
            if(searchForWord(word)) {
                gridWords.put(word, me.getValue());
            }
        }
    }


}
