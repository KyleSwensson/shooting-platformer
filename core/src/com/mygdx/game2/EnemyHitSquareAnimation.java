package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by kyle on 10/9/2015.
 */
public class EnemyHitSquareAnimation extends Animation {
    Texture texture2 = new Texture("SPA/Player/Hit/Enemy/1.png");
    Texture texture3 = new Texture("SPA/Player/Hit/Enemy/2.png");
    Texture texture4 = new Texture("SPA/Player/Hit/Enemy/3.png");
    Texture texture5 = new Texture("SPA/Player/Hit/Enemy/4.png");
    Texture[] textures = {texture2, texture3, texture4, texture5, texture5};
    boolean destroyed;

    public int animImage = 0;
    public int frameCount = 0;
    public int framesSwitch = 2;
    public float rotation = 0;

    public void update() {
        frameCount++;

        rotation += 30;

        if (frameCount > framesSwitch) {
            animImage ++;
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
            batch.draw(textures[animImage], x, y, 16,16, 32, 32,1, 1, rotation, 0, 0 , 16, 16, false, false);
            destroyed = true;
        }

    }
}
