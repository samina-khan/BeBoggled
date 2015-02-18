package com.segames.boggle;

import android.os.CountDownTimer;

import java.util.HashMap;

/**
 * Created by li on 12/02/15.
 */
public class BBSeverSingle implements GlobalConstants{

    private int gameRound;
    private int gameScore;
    private int gameLevel;
    private int gameMode;

    private final long startTime = 60 * 1000;
    private final long interval = 1 * 1000;
    private CountDownTimer countDownTimer;

    private int playerId1 = 1;
    private int playerId2 = 2;

    private BBWords BBWords;

    //guessed words String = word / Integer = word value
    private HashMap<String,Integer> guessedWords = new HashMap();

    //Constructor
    public BBSeverSingle(){
        this.gameRound = 0;
        this.gameLevel = BBEasyLevel;
        this.gameMode = BBSingleMode;
    }


    //getters
    public int getGameRound() {
        return gameRound;
    }

    public int getGameLevel() {
        return gameLevel;
    }

    public int getGameMode() {
        return gameMode;
    }

    public HashMap<String, Integer> getGuessedWords() {
        return guessedWords;
    }

    public int getGameScore() {
        return gameScore;
    }


    //setters
    public void setGameScore(int gameScore) {
        this.gameScore = gameScore;
    }

    public void setGameLevel(int gameLevel) {
        this.gameLevel = gameLevel;
    }

    //functions
    public void updateGameScore(int wordValue) {

        this.gameScore = this.gameScore + wordValue;
    }

    public void updateGameRound(){
        this.gameRound++;
    }

    public void clearGuessedWords(){
        guessedWords.clear();
    }

    public void newRound(){
        guessedWords.clear();
        this.setGameScore(0);
        this.updateGameRound();
        if(this.getGameRound() > MaxEasyRounds)
            this.setGameLevel(BBNormalLevel);
    }

    public String incomeWord(String candidateWord, int playerId){
        String message = ok;
        if(guessedWords.containsKey(candidateWord) != true){
            if(BBWords.isWordValid(candidateWord)){
                if(BBWords.wordsValue(candidateWord) >= 0){
                    //score update
                    this.updateGameScore(BBWords.wordsValue(candidateWord));

                    //update of guessedWords
                    guessedWords.put(candidateWord,BBWords.wordsValue(candidateWord));
                }
            }
            else{
                //the word submitted is not a correct word
                message = wordIncorrect;
            }
        }
        else{
            //word submitted was already guessed by the player
            message = wordAlreadyGuessed;
        }
        return message;
    }




}
