package com.mygdx.game2;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by kyle on 10/22/2015.
 */
public class NewLevelText extends Animation {

    String text = new String();
    public int frameCount = 0;
    public int framesDelete = 140;
    BitmapFont drawingText = new BitmapFont();
    float alpha;
    int alphaTimer = 0;
    int alphaTimerMax = 10;

    public NewLevelText(int x, int y, int level) {
        this.text = "Level " + level;
        this.x = x;
        this.y = y;
        destroyed = false;
        alpha = 1.0f;
        drawingText.getData().setScale(2,2);
    }

    public void update() {
        alphaTimer++;
        if (alphaTimer > alphaTimerMax) {
            alpha -= .05f;
            alphaTimer = 0;
        }
        frameCount ++;
        if (frameCount > framesDelete) {
            destroyed = true;
        }
        drawingText.setColor(1.0f,1.0f,1.0f,alpha);

        y += .25;

        //TODO:this wont change anyrhing so change y to float instead of int
        //ALSO TODO: have this slowly follow the player but not fast enough to catch up

    }

    public void draw(SpriteBatch batch) {
        drawingText.draw(batch,text,x,y);
    }

    public boolean getDestroyed() {
        return destroyed;
    }



}
