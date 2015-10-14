package com.mygdx.game2;
import com.badlogic.gdx.math.Rectangle;
import java.awt.*;

/**
 * Created by kyle on 9/16/2015.
 */
public class BaseTile {
    int x;
    int y;
    int xDist; // distance from block to character x plane
    int yDist; // distance from block to character y plane
    int drawDist = 500; // max distance from player that this should still be drawn and updated
    boolean isActive; // boolean to tell whether it is too far away and should be drawn
    boolean passiveTile;
    boolean isFlipped = false;
    boolean isFlippedVertical = false;
    int[] orientation = new int[4]; // int to tell the game which sides the block is covered on, 1000 is left 0100 is right 0010 is top 0001 is bottom
    String type;


    int width = 32;
    int height = 32;

    Rectangle rect = new Rectangle();


    public BaseTile(int newX, int newY) {
        y = newY;
        x = newX;
        rect.x = x;
        rect.y = y;
        rect.width = width;
        rect.height = height;
    }

    public void update(int playerX, int playerY) {
        xDist = Math.abs(playerX - this.x);
        yDist = Math.abs(playerY - this.y);
        if (Math.sqrt((xDist*xDist) + (yDist*yDist)) > drawDist) isActive = false;
        else isActive = true;
    }

}
