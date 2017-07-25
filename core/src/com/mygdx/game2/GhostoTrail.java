package com.mygdx.game2;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by kyle on 10/8/2015.
 */
public class GhostoTrail extends Animation {
    Texture texture = new Texture("SPA/Enemy/ghosto/1.png");
    boolean destroyed;

    public float alpha;
    public boolean isFacingLeft;

    public GhostoTrail(float x, float y, boolean isFacingLeft) {
        this.x = x;
        this.y = y;
        alpha = 1f;
        destroyed = false;
        this.isFacingLeft = isFacingLeft;
    }

    public void update() {
        if (alpha > 0) {
            alpha -= 0.05f;
        } else {
            destroyed = true;
        }
    }

    public boolean getDestroyed() {
        return destroyed;
    }

    public void draw(SpriteBatch batch) {

        Color oldColor = batch.getColor();
        batch.setColor(oldColor.r, oldColor.g, oldColor.b, alpha);
        batch.draw(texture,x,y,24,18,0,0,12,9,isFacingLeft,false);
        batch.setColor(oldColor);


    }

}
