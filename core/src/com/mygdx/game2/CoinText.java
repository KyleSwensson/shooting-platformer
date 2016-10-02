package com.mygdx.game2;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by kyle on 10/26/2015.
 */
public class CoinText {
    int x;
    int y;
    BitmapFont drawingText = new BitmapFont();
    String text = new String();


    public CoinText(int x, int y) {
        this.x = x;
        this.y = y;
        drawingText.setColor(1.0f,1.0f,1.0f,1.0f);
    }

    public void draw(SpriteBatch batch, int currentCoins) {
        if (currentCoins == 696969) {
            text = "inf";

        } else text = "Money: " + currentCoins;

        drawingText.draw(batch,text,x,y);
    }

}
