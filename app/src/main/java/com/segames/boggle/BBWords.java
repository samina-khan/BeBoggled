package com.segames.boggle;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

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

    private final HashMap<String,Integer> validWords = new HashMap<String,Integer>();
    private TreeMap<String,Integer> gridWords = null;
    private TreeMap<Integer,Integer> letters = null;
    private int current_level = -1;
    private int current_grid_size = -1;
    private int minimum_valid_words = -1;
    private int valid_words_count = -1;
    private int max_word_len = -1;

    private final static int MAX_GRID_SIZE = 4;
    private char[][] grid;
    private char[][] annotated_grid;

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

        // used for specific word in grid
        annotated_grid = new char[MAX_GRID_SIZE][MAX_GRID_SIZE];
    }

    private void randomize_grid() {
        for (int r = 0; r < current_grid_size; r++) {
            for (int c = 0; c < current_grid_size; c++) {
                grid[r][c] = (char)('a' + (int)(Math.random()*26));
            }
        }
    }

    private String get_grid_str() {
        String str = "";
        for (int i = 0; i < current_grid_size; i++) {
            for (int j = 0; j < current_grid_size; j++) {
                str += grid[i][j];
            }
        }
        return str;
    }
    private String get_annotated_grid_str() {
        String str = "";
        for (int i = 0; i < current_grid_size; i++) {
            for (int j = 0; j < current_grid_size; j++) {
                str += annotated_grid[i][j] + "|";
            }
        }
        return str;
    }

    public void dumpGrid() {
        for(int i = 0;i < current_grid_size;i++) {
            for (char letter : grid[i]) {
                System.out.print(letter + " ");
            }
            if (i < current_grid_size - 1) {
                System.out.print("\n");
            }
        }
        System.out.println();
    }

        public void dumpAnnotatedGrid() {
        for(int i = 0;i < current_grid_size;i++) {
            for (char letter : annotated_grid[i]) {
                System.out.print(letter + " ");
            }
            if (i < current_grid_size - 1) {
                System.out.print("\n");
            }
        }
        System.out.println();
        System.out.println(get_annotated_grid_str());
    }


    public String Grid(int level){
        current_level = level;
        switch(current_level) {
            case BBEasyLevel:
                current_grid_size = BBEasyLevelSize;
                break;
            case BBNormalLevel:
                current_grid_size = BBNormalLevelSize;
                break;
            default:
                current_grid_size = -1;
        }

        // This TreeMap is just used to get a count of the words in the grid
        //      that are from the valid list
        gridWords = new TreeMap<String,Integer>();
        while(true) {

            randomize_grid();

            // find all words in validWords in the grid
            find_all_grid_words(get_grid_str());

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
    private int calculate_word_value(String word) {
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
        if(validWords.get(candidateWord.toLowerCase()) == null)
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
        if(validWords.get(candidateWord.toLowerCase()) != null){
        return validWords.get(candidateWord.toLowerCase());}
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

    public boolean searchForWord(String grid_str,String word) {
        // for each row of the grid and each col of the grid
        //      do a gridSearch for the word
        set_grid(grid_str);
        for(int i = 0; i < current_grid_size; i++) {
            for(int j = 0; j < current_grid_size; j++) {
                // reset the annotated grid to an unmarked state
                set_annotated_grid(grid_str);
                // check if word found starting at this grid cell
                if(gridSearch(word, i, j, 1) == true) {
                    return true;
                }
            }
        }
        return false;
    }

    private void set_grid(String grid_str) {
        for (int i = 0; i < current_grid_size; i++) {
            for (int j = 0; j < current_grid_size; j++) {
                grid[i][j] = grid_str.charAt(i*current_grid_size +j);
            }
        }
    }

    private void set_annotated_grid(String grid_str) {
        for (int i = 0; i < current_grid_size; i++) {
            for (int j = 0; j < current_grid_size; j++) {
                annotated_grid[i][j] = grid_str.charAt(i*current_grid_size +j);
            }
        }
    }

    private boolean gridSearch(String str, int i, int j, int c) {

        // If str is empty, we have consumed the whole word
        if(str.equals("")) return true;

        // Hit a N or S wall
        if(i < 0 || i >= current_grid_size) return false;

        // Hit a E or W wall
        if(j < 0 || j >= current_grid_size) return false;

        // Current letter of word doesn't match cell letter
        if(grid[i][j] != str.charAt(0)) return false;

        // We mark the current cell of the grid as visited
        //      before we recurse
        char tmp = grid[i][j];  grid[i][j] = '#';

        // put current count in annotated_grid
        //  note - the charAt(0) only works for words between 0 and 9 characters
        //  in length
        annotated_grid[i][j] = Integer.toString(c).charAt(0);

        // Get the tail of the string
        String substr = str.substring(1, str.length());

        // Try NW, N, NE, E, SE, S, SW, W consecutively and short circuit with
        //      first successful call
        boolean rslt =   gridSearch(substr, i-1, j-1, c+1) ||
                         gridSearch(substr, i-1,   j, c+1) ||
                         gridSearch(substr, i-1, j+1, c+1) ||
                         gridSearch(substr,   i, j-1, c+1) ||
                         gridSearch(substr,   i, j+1, c+1) ||
                         gridSearch(substr, i+1, j-1, c+1) ||
                         gridSearch(substr, i+1,   j, c+1) ||
                         gridSearch(substr, i+1, j+1, c+1);

        // Restore the letter if we backtrack
        if(rslt == false) {

            annotated_grid[i][j] = tmp;
        }

        // always return grid cell back to what it was
        grid[i][j] = tmp;
        return rslt;
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
    public void dumpAllAnnotatedGrids() {
        Set set = gridWords.entrySet();
        Iterator i = set.iterator();
        while(i.hasNext()) {
            System.out.println();
            dumpGrid();
            Map.Entry<String,Integer> me = (Map.Entry)i.next();
            System.out.print(me.getKey() + ": ");
            System.out.println(me.getValue());
            annotatedGrid(get_grid_str(),me.getKey());
            dumpAnnotatedGrid();
        }
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

    public String annotatedGrid(String this_grid_str, String word_str) {
        searchForWord(this_grid_str,word_str);
        String annotated_grid_str = get_annotated_grid_str();
        return annotated_grid_str;
    }

    public String wordPosFromAnnotatedGrid(String annotated_grid_str) {
        TreeMap<Integer,String> letters = new TreeMap<Integer,String>();

        for(int i = 0; i < annotated_grid_str.length();i++){
            if(Character.isDigit(annotated_grid_str.charAt(i))) {
                System.out.println(annotated_grid_str.charAt(i));
                int letter_index = annotated_grid_str.charAt(i) - 48;
                letters.put(letter_index,Integer.toString(i));
            }
        }

        Set set = letters.entrySet();
        Iterator i = set.iterator();
        String str = "";
        while(i.hasNext()) {
            Map.Entry<Integer,String> me = (Map.Entry)i.next();
            str += Integer.toString(me.getKey()) + ": " + me.getValue() + "|";
        }
        return str;
    }

    private void find_all_grid_words(String grid_str) {

        Set set = validWords.entrySet();
        Iterator i = set.iterator();
        while(i.hasNext()) {
            Map.Entry<String,Integer> me = (Map.Entry)i.next();
            String word = me.getKey();
            if(searchForWord(grid_str,word)) {
                gridWords.put(word, me.getValue());
            }
        }
    }
}
