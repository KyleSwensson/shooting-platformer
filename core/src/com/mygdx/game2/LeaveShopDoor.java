package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Created by kyles on 8/9/2016.
 */
public class LeaveShopDoor extends Item {


    String type = "leaveShopDoor";
    Texture doorImage = new Texture("SPA/tile-map/exitDoor.png");
    boolean intersectingPlayer = false;

    int xDist; // distance from block to character x plane
    int yDist; // distance from block to character y plane
    int drawDist = 500; // max distance from player that this should still be drawn and updated
    boolean isActive; // boolean to tell whether it is too far away and should be drawn

    Rectangle rect = new Rectangle();

    public LeaveShopDoor(float x, float y, float velX, float velY, int width, int height) {
        super(x,y,velX,velY,width,height);

        rect.x = x;
        rect.y = y;
        rect.width = width;
        rect.height = height;
    }

    public  String getType() {
        return type;
    }

    public void update(Array<BaseTile> tiles, Rectangle playerRect) {
        intersectingPlayer = false;
        if (rect.overlaps(playerRect)) {
            intersectingPlayer = true;
        }
    }






    public void draw(SpriteBatch batch) {
        batch.draw(doorImage,x,y,width,height);
    }



}
