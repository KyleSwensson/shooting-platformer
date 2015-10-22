package com.mygdx.game2;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by kyle on 10/22/2015.
 */
public class EnemyHitText extends Animation {

    String text = new String();
    public int frameCount = 0;
    public int framesDelete = 30;
    BitmapFont drawingText = new BitmapFont();

    public EnemyHitText(int x, int y, int damage) {
        this.text = "-"+damage;
        this.x = x;
        this.y = y;
        destroyed = false;
    }

    public void update() {
        frameCount ++;
        if (frameCount > framesDelete) {
            destroyed = true;
        }
        this.y += 1;
    }

    public void draw(SpriteBatch batch) {
        drawingText.draw(batch,text,x,y);
    }

    public boolean getDestroyed() {
        return destroyed;
    }



}
