package com.mygdx.game2;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by kyle on 10/8/2015.
 */
public abstract class Animation {
    //animations are things that happen in the environment but interact with literally nothing, particles interact with things
    // but they dont change them, animations literally dont interact they just do their animation
    public int x = 0;
    public int y = 0;
    public float velX = 0;
    public float velY = 0;
    public boolean destroyed;
    public int animImage;
    public int frameCount;
    public int framesSwitch;
    public abstract void update();
    public abstract void draw(SpriteBatch batch);

    public abstract boolean getDestroyed();
}
