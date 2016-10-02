package com.mygdx.game2;

import com.badlogic.gdx.graphics.g2d.*;

/**
 * Created by kyles on 10/1/2016.
 */
public class ShopPriceText extends Animation {

    float x;
    float y;
    BitmapFont drawingText = new BitmapFont();
    String text = new String();
    int price;
    boolean destroyed= false;


    public ShopPriceText(float  x, float y, int price) {
        this.x = x;
        this.y = y;
        drawingText.setColor(1.0f,1.0f,1.0f,1.0f);
        this.price = price;
        destroyed = false;
    }

    public void update() {
    }

    public boolean getDestroyed() {
        return destroyed;
    }

    public void draw(SpriteBatch batch) {
        text = "$" + price;

        drawingText.draw(batch,text,x,y);
    }
}
