package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Created by kyles on 8/9/2016.
 */
public class ShopTable extends Item {


    String type = "shopTable";
    Texture tableImage = new Texture("SPA/Objects/Table1.png");



    boolean intersectingPlayer = false;

    int xDist; // distance from block to character x plane
    int yDist; // distance from block to character y plane
    int drawDist = 500; // max distance from player that this should still be drawn and updated
    boolean isActive; // boolean to tell whether it is too far away and should be drawn

    ShopPickup item1;
    ShopPickup item2;
    ShopPickup item3;

    Rectangle rect = new Rectangle();

    public ShopTable(float x, float y, float velX, float velY, int width, int height) {
        super(x,y,velX,velY,width,height);

        rect.x = x;
        rect.y = y;
        rect.width = width;
        rect.height = height;

        item1 = new ShopPickup(x + 20   ,y + 32,0,0,32,32);
        item2 = new ShopPickup(x + 52,y + 32,0,0,32,32);
        item3 = new ShopPickup(x + 84,y + 32,0,0,32,32);
    }

    public  String getType() {
        return type;
    }

    public void update(Array<BaseTile> tiles, Rectangle playerRect) {
        intersectingPlayer = false;
        if (rect.overlaps(playerRect)) {
            intersectingPlayer = true;

        }

        item1.update(tiles, playerRect);
        item2.update(tiles, playerRect);
        item3.update(tiles, playerRect);
    }






    public void draw(SpriteBatch batch) {
        batch.draw(tableImage,x,y,width,height);
        item1.draw(batch);
        item2.draw(batch);
        item3.draw(batch);
    }



}