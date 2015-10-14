package com.mygdx.game2;
import com.badlogic.gdx.math.Rectangle;
import java.awt.*;

/**
 * Created by kyle on 9/16/2015.
 */
public class Gate {
    int x;
    int y;
    int xDist; // distance from block to character x plane
    int yDist; // distance from block to character y plane
    int drawDist = 500; // max distance from player that this should still be drawn and updated
    boolean isActive; // boolean to tell whether it is too far away and should be drawn
    boolean passiveTile;
    boolean isFlipped = false;

    String type;


    int width = 32;
    int height = 32;

    Rectangle rect = new Rectangle();


    public Gate(int newX, int newY) {
        y = newY;
        x = newX;
        rect.x = x;
        rect.y = y;
        rect.width = width;
        rect.height = height;
    }

    public void update(int playerX, int playerY) {

    }

}
