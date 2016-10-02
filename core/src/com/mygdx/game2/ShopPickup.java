package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

/**
 * Created by kyles on 8/9/2016.
 */
public class ShopPickup extends Item {


    String type = "pickupPedestal";
    Texture holderImage = new Texture("SPA/Objects/shopItemHolder.png");

    boolean itemTaken = false; // when this is true the player has taken the item and it should no longer be drawn


    boolean intersectingPlayer = false;

    int xDist; // distance from block to character x plane
    int yDist; // distance from block to character y plane
    int drawDist = 500; // max distance from player that this should still be drawn and updated
    boolean isActive; // boolean to tell whether it is too far away and should be drawn

    int itemPrice;

    Random random;

    Pickup pickup; // the actual pickup above the pedestal

    ShopPriceText priceText; // the text that shows the cost of an item above the item

    Rectangle rect = new Rectangle();

    public ShopPickup(float x, float y, float velX, float velY, int width, int height) {
        super(x,y,velX,velY,width,height);

        rect.x = x;
        rect.y = y;
        rect.width = width;
        rect.height = height;

        pickup = new Pickup(x,y,width,height, false);


        random = new Random();
        if (random.nextInt(2) == 1) {
            itemPrice = 5;
        } else {
            itemPrice = 10;
        }

        priceText = new ShopPriceText(x + 9,y + 40, itemPrice);

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

        priceText.update();

    }






    public void draw(SpriteBatch batch) {
        batch.draw(holderImage,x,y,width,height);
        if (!itemTaken) {
            pickup.draw(batch);
            priceText.draw(batch);
        }
    }



}