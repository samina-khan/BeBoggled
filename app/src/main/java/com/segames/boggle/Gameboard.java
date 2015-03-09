package com.segames.boggle;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableRow;

/**
 * Created by SAMINA on 1/26/15.
 */
public class Gameboard implements GlobalConstants{
    static int[][] moves = {{-1,0}, {-1, -1}, {0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}};
    Button[][] buttons;
    int size;
    private Button previous_click;


    public Gameboard(int size){
        this.buttons = new Button[BBNormalLevelSize][BBNormalLevelSize];
        this.size = size;

    }

    void setGameboard(String letters)
    {
        for(int i = 0, k = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                String s = Character.toString(letters.charAt(k++));
                this.buttons[i][j].setText(s.toUpperCase());
            }
        }
    }

    void hideButtons(){
        for(int i=0; i<=3; i++)
            for(int j=0;j<=3; j++)
                if(i==3 || j==3)
                    this.buttons[i][j].setVisibility(View.GONE);

        for(Button[] a: buttons){
            for(Button b: a){
                b.setLayoutParams(new TableRow.LayoutParams(310, 310));
            }
        }
    }

    void showButtons(){
        for(int i=0; i<=3; i++)
            for(int j=0;j<=3; j++)
                if(i==3 || j==3)
                    this.buttons[i][j].setVisibility(View.VISIBLE);
    }

    void opaqueButtons(Drawable d)
    {
        for(Button[] g: this.buttons)
            for(Button b:g) {
                b.setBackground(d);
            }
    }
    boolean isvalidclick(int clickedbuttonID)
    {
        boolean returnval=false;
        if(previous_click==null){
            returnval=true;
        }
        else if(previous_click.getId()==clickedbuttonID){
            returnval=false;
        }
        else{
            int previousi=0,previousj=0,clickedi=0,clickedj=0;
            for(int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (buttons[i][j].getId() == clickedbuttonID) {
                        clickedi = i;
                        clickedj = j;
                    }
                    if (buttons[i][j].getId() == previous_click.getId()) {
                        previousi = i;
                        previousj = j;
                    }

                }

            }

            for(int k=0;k<moves.length;k++)
            {
                int ti = previousi + moves[k][0];
                int tj = previousj + moves[k][1];
                if (ti < 0 || ti >= size || tj < 0 || tj >= size) {
                    continue;
                }
                else
                {
                    if(clickedi == ti && clickedj ==tj)
                    {
                        returnval=true;
                        //Log.v("Gameboard", "valid");
                    }
                }
            }


        }
        //Log.v("Gameboard: ", "button click is: "+returnval);

        return returnval;

    }
    void clearpreviousclick()
    {
        this.previous_click=null;
    }

    Button getpreviousclick(){
        return previous_click;
    }
    void previousclick(int buttonID){

        for(Button[] array: this.buttons)
        {
            for(Button b: array)
            {
                if(b.getId()==buttonID)
                {
                    previous_click = b;
                }
            }
        }

    }
    int getArrow(int buttonID) {
        if(previous_click!=null) {
            int previousi = 0, previousj = 0, clickedi = 0, clickedj = 0;
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (buttons[i][j].getId() == buttonID) {
                        clickedi = i;
                        clickedj = j;
                    }
                    if (buttons[i][j].getId() == previous_click.getId()) {
                        previousi = i;
                        previousj = j;
                    }

                }

            }
            if (clickedi < previousi) {
                if (clickedj < previousj) return topleft;
                else if (clickedj == previousj) return topup;
                else return topright;
            } else if (clickedi == previousi) {
                if (clickedj < previousj) return midleft;
                else if (clickedj == previousj) return -1;
                else if(clickedj>previousj) return midright;
            } else {
                if (clickedj < previousj) return botleft;
                else if (clickedj == previousj) return botdown;
                else return botright;
            }
        }
        return -1;

    }

    void setArrow(Drawable d){
        if(previous_click!=null){
            previous_click.setBackground(d);
        }
    }


}
