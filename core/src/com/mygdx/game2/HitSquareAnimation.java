package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by kyle on 10/8/2015.
 */
public class HitSquareAnimation extends Animation {
    Texture texture1 = new Texture("SPA/Player/Hit/0.png");
    Texture texture2 = new Texture("SPA/Player/Hit/1.png");
    Texture texture3 = new Texture("SPA/Player/Hit/2.png");
    Texture texture4 = new Texture("SPA/Player/Hit/3.png");
    Texture texture5 = new Texture("SPA/Player/Hit/4.png");
    Texture[] textures = {texture2, texture3, texture4, texture5, texture5};
    boolean destroyed;
    boolean isDestroyed;

    public int animImage = 0;
    public int frameCount = 0;
    public int framesSwitch = 2;
    public float rotation = 0;

    public void update() {
        frameCount++;

        if (frameCount > framesSwitch) {
            animImage ++;
            rotation += 30;
            frameCount = 0;
            if (animImage > 3) {
                destroyed = true;
            }
        }

        if (animImage <= 3) {

            destroyed = false;
        } else {

            destroyed = true;
        }
    }

    public boolean getDestroyed() {
        return destroyed;
    }

    public void draw(SpriteBatch batch) {

        if (animImage <= 3) {
            batch.draw(textures[animImage], x, y, 16, 16, 32, 32, 1, 1, rotation, 0, 0, 16, 16, false, false);
            destroyed = true;
        }

    }

}
