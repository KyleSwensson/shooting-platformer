package com.mygdx.game2;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by kyles on 8/2/2016.
 */
public abstract class AbstractButton {
    int x;
    int y;
    int width;
    int height;
    public abstract void draw(SpriteBatch batch);
}
