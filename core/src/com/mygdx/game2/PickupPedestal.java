package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Created by kyles on 8/9/2016.
 */
public class PickupPedestal extends Item {


    String type = "pickupPedestal";
    Texture pedestalImage = new Texture("SPA/tile-map/itemPedestal.png");

    boolean itemTaken = false; // when this is true the player has taken the item and it should no longer be drawn


    boolean intersectingPlayer = false;

    int xDist; // distance from block to character x plane
    int yDist; // distance from block to character y plane
    int drawDist = 500; // max distance from player that this should still be drawn and updated
    boolean isActive; // boolean to tell whether it is too far away and should be drawn

    Pickup pickup; // the actual pickup above the pedestal

    Rectangle rect = new Rectangle();

    public PickupPedestal(float x, float y, float velX, float velY, int width, int height) {
        super(x,y,velX,velY,width,height);

        rect.x = x;
        rect.y = y;
        rect.width = width;
        rect.height = height;

        pickup = new Pickup(x,y + 30,width,height);
    }

    public  String getType() {
        return type;
    }

    public void update(Array<BaseTile> tiles, Rectangle playerRect) {
        intersectingPlayer = false;
        if (rect.overlaps(playerRect)) {
            intersectingPlayer = true;

        }

        pickup.update();
    }






    public void draw(SpriteBatch batch) {
        batch.draw(pedestalImage,x,y,width,height);
        if (!itemTaken) {
            pickup.draw(batch);
        }
    }



}
