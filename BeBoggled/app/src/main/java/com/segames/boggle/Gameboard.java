package com.segames.boggle;

import android.util.Log;
import android.widget.Button;
import android.view.View;

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

                this.buttons[i][j].setText(Character.toString(letters.charAt(k++)));
            }
        }
    }

    void hideButtons(){
        for(int i=0; i<=3; i++)
            for(int j=0;j<=3; j++)
                if(i==3 || j==3)
                    this.buttons[i][j].setVisibility(View.GONE);
    }

    void showButtons(){
        for(int i=0; i<=3; i++)
            for(int j=0;j<=3; j++)
                if(i==3 || j==3)
                    this.buttons[i][j].setVisibility(View.VISIBLE);
    }

    void opaqueButtons()
    {
        for(Button[] g: this.buttons)
            for(Button b:g) {
                b.setAlpha(0.55f);
            }
                //b.getBackground().setAlpha(127);
        Log.v("Gameboard:","opaquing all");
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
                        Log.v("Gameboard", "valid");
                    }
                }
            }


        }
        Log.v("Gameboard: ", "button click is: "+returnval);

        return returnval;

    }
    void clearpreviousclick()
    {
        this.previous_click=null;
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


}
