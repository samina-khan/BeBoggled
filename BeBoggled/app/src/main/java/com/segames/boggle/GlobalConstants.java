package com.segames.boggle;

/**
 * Global constants; implement this interface to access them conveniently.
 */
public interface GlobalConstants {

    /*
     *  bbwords constants
    */

    public static final int BBEasyLevel = 1;
    public static final int BBNormalLevel = 2;

    public static final int BBMaxWordLenEasyLevel = 9;
    public static final int BBMaxWordLenNormalLevel = 16;

    public static final int BBEasyLevelSize = 3;    // i.e. 3 by 3
    public static final int BBNormalLevelSize = 4;  // i.e. 4 by 4

    public static final int BBEasyLevelNoWords = 15;
    public static final int BBNormalLevelNoWords = 20;

    public static final int BBWordsSuccess = 0;
    public static final int BBWordsFailure = -1;

    public static final String BBValidWordsFile = "bbwords.txt";

    /*
     *  BBServer constants
    */
    public static final int BBSingleMode = 0;
    public static final int BBDoubleBasicMode = 1;
    public static final int BBDoubleCutthroatMode = 2;

    public static final int MaxEasyRounds = 3;

    //messages
    public static final String ok = "ok";
    public static final String wordAlreadyGuessed = "Word already guessed";
    public static final String wordIncorrect = "Incorrect";



} // public interface GlobalConst
