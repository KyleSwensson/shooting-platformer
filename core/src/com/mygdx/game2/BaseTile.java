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
    int drawDist = 900; // max distance from player that this should still be drawn and updated
    boolean isActive; // boolean to tell whether it is too far away and should be drawn
    boolean passiveTile;
    boolean isFlipped = false;
    boolean isFlippedVertical = false;

    boolean isEdgeTile;

    String type;

    int tileMapOffsetX; // offset in tilemap
    int tileMapOffsetY; //offset in tilemap


    //variables to assess whether the tile is covered on which of its sides
    boolean coveredLeft = false;
    boolean coveredRight = false;
    boolean coveredTop = false;
    boolean coveredBottom = false;

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
        coveredLeft = false;
        coveredRight = false;
        coveredTop = false;
        coveredBottom = false;
        isEdgeTile = false;
    }

    public void update(int playerX, int playerY) {
        xDist = Math.abs(playerX - this.x);
        yDist = Math.abs(playerY - this.y);
        if (Math.sqrt((xDist*xDist) + (yDist*yDist)) > drawDist) isActive = false;
        else isActive = true;
    }

}
