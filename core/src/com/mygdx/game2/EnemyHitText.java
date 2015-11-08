package com.mygdx.game2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by kyle on 10/22/2015.
 */
public class EnemyHitText extends Animation {

    String text = new String();
    public int frameCount = 0;
    public int framesDelete = 30;
    BitmapFont drawingText = new BitmapFont(Gdx.files.internal("SPA/font.fnt"), false);
    float alpha;

    public EnemyHitText(int x, int y, int damage) {
        this.text = "-"+damage;
        this.x = x;
        this.y = y;
        destroyed = false;

        drawingText.setColor(Color.RED);
        alpha = 1.0f;
        drawingText.getData().setScale(.5f,.5f);
    }

    public void update() {
        frameCount ++;
        if (frameCount > framesDelete) {
            destroyed = true;
        }
        this.y += 1;
        alpha -= .03f;
        drawingText.setColor(1.0f,0f,0f,alpha);
    }

    public void draw(SpriteBatch batch) {
        drawingText.draw(batch,text,x,y);

    }

    public boolean getDestroyed() {
        return destroyed;
    }



}
