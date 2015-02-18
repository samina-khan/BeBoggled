package com.segames.boggle;

import android.os.CountDownTimer;

import java.util.HashMap;

/**
 * Created by li on 15/02/15.
 */
public class BBServerDouble implements GlobalConstants{

    private int gameMode;
    private int gameLevel;
    private int gameRound;

    private int gameScoreP1;
    private int gameScoreP2;
    private int nbRoundWonP1;
    private int nbRoundWonP2;

    private BBWords BBWords;

    //guessed words String = word / Integer = player
    private HashMap<String,Integer> allGuessedWords = new HashMap();

    //guessed words per player String = word / Integer = word value
    private HashMap<String,Integer> guessedWordsP1 = new HashMap();
    private HashMap<String,Integer> guessedWordsP2 = new HashMap();

    //timer
    private final long startTime = 60 * 1000;
    private final long interval = 1 * 1000;
    private CountDownTimer countDownTimer;


    //getters
    public int getGameMode() {
        return gameMode;
    }

    public int getGameLevel() {
        return gameLevel;
    }

    public int getGameRound() {
        return gameRound;
    }

    public int getGameScoreP1() {
        return gameScoreP1;
    }

    public int getGameScoreP2() {
        return gameScoreP2;
    }

    public int getNbRoundWonP1() {
        return nbRoundWonP1;
    }

    public int getNbRoundWonP2() {
        return nbRoundWonP2;
    }


    //setters
    public void setGameMode(int gameMode) {
        this.gameMode = gameMode;
    }

    public void setGameLevel(int gameLevel) {
        this.gameLevel = gameLevel;
    }

    public void setGameRound(int gameRound) {
        this.gameRound = gameRound;
    }

    public void setGameScoreP1(int gameScoreP1) {
        this.gameScoreP1 = gameScoreP1;
    }

    public void setGameScoreP2(int gameScoreP2) {
        this.gameScoreP2 = gameScoreP2;
    }

    //functions
    public void updateGameScore(int wordValue, int playerId) {
        if(playerId == 1){
            this.gameScoreP1 = this.gameScoreP1 + wordValue;
        }else if(playerId == 2){
            this.gameScoreP2 = this.gameScoreP2 + wordValue;
        }
    }

    public void updateGameRound(){
        this.gameRound++;
    }

    public void updateNbRoundWon(int playerId){
        if(playerId == 1){
            this.nbRoundWonP1++;
        }else if(playerId == 2){
            this.nbRoundWonP2++;
        }
    }

    public void clearGuessedWords(){
        allGuessedWords.clear();
        guessedWordsP1.clear();
        guessedWordsP2.clear();
    }

    public void newRound(){
        this.allGuessedWords.clear();
        this.guessedWordsP1.clear();
        this.guessedWordsP2.clear();
        this.setGameScoreP1(0);
        this.setGameScoreP2(0);
        this.updateGameRound();
    }

    public String incomeWordBasic(String candidateWord, int playerId){
        String message = ok;

        if(playerId == 1){
            if(guessedWordsP1.containsKey(candidateWord) != true){
                if(BBWords.isWordValid(candidateWord)){
                    if(BBWords.wordsValue(candidateWord) >= 0){
                        //score update
                        this.updateGameScore(BBWords.wordsValue(candidateWord),playerId);

                        //update of guessedWords of player 1
                        guessedWordsP1.put(candidateWord,BBWords.wordsValue(candidateWord));
                    }
                }
                else{
                    //the word submitted id not a correct word
                    message = wordIncorrect;
                }
            }
            else{
                //word submitted was already guessed by the player
                message = wordAlreadyGuessed;
            }
        }else if(playerId == 2){
            if(guessedWordsP2.containsKey(candidateWord) != true){
                if(BBWords.isWordValid(candidateWord)){
                    if(BBWords.wordsValue(candidateWord) >= 0){
                        //score update
                        this.updateGameScore(BBWords.wordsValue(candidateWord),playerId);

                        //update of guessedWords of player 1
                        guessedWordsP2.put(candidateWord,BBWords.wordsValue(candidateWord));
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
        }

        return message;
    }

    public String incomeWordCutthroat(String candidateWord, int playerId){
        String message = ok;

        if(allGuessedWords.containsKey(candidateWord) != true){
            if(BBWords.isWordValid(candidateWord)){
                if(BBWords.wordsValue(candidateWord) >= 0){
                    //score update
                    this.updateGameScore(BBWords.wordsValue(candidateWord),playerId);

                    //update of allGuessedWords
                    allGuessedWords.put(candidateWord,playerId);

                    if(playerId == 1){
                        //update of guessedWords of player 1
                        guessedWordsP1.put(candidateWord,BBWords.wordsValue(candidateWord));
                    }else if (playerId == 2){
                        //update of guessedWords of player 2
                        guessedWordsP2.put(candidateWord,BBWords.wordsValue(candidateWord));
                    }

                }
            }
            else{
                //the word submitted is not a correct word
                return wordIncorrect;
            }
        }
        else{
            //word submitted was already guessed by the player
            return wordAlreadyGuessed;
        }

        return message;
    }

    public String incomeWord(String candidateWord, int playerId){
        String message = ok;

        //if Basic Double Mode
        if(this.getGameMode() == BBDoubleBasicMode){
            return this.incomeWordBasic(candidateWord,playerId);
        }else if (this.getGameMode() == BBDoubleCutthroatMode){
            return this.incomeWordCutthroat(candidateWord,playerId);
        }

        return message;
    }

    public int winnerRound(){
        if (this.getGameScoreP1() < this.getGameScoreP2()){
            this.updateNbRoundWon(2);
            return 2;
        }
        else if(this.getGameScoreP2() < this.getGameScoreP1()){
            this.updateNbRoundWon(1);
            return 1;
        }
        else{
            return 0;
        }
    }

    public int winner(){
        if (this.getNbRoundWonP1() < this.getNbRoundWonP2()){
            return 2;
        }
        else if(this.getNbRoundWonP2() < this.getNbRoundWonP1()){
            return 1;
        }
        else{
            return 0;
        }
    }


}
