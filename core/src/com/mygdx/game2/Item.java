package com.mygdx.game2;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Rectangle;
import java.awt.*;

/**
 * Created by kyle on 10/7/2015.
 */

//TODO: Finish implementing type hierarchy on mgItems
public abstract class Item {
    boolean destroyed = false;
    float x;
    float y;
    float velX;
    float velY;
    int width;
    int height;
    Rectangle rect = new Rectangle();
    protected String type = "";

    public Item(float x, float y, float velX, float velY, int width, int height) {
        this.x = x;
        this.y = y;
        this.velX = velX;
        this.velY = velY;
        this.width = width;
        this.height = height;
    }
    public abstract String getType();

    public abstract void update(Array<BaseTile> tiles, Rectangle playerRect);

    public boolean getDestroyed() {
        return destroyed;
    }

    public abstract void draw(SpriteBatch batch);

    public  Rectangle getRect() {
        return rect;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

}
