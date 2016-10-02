package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.awt.*;
import java.util.Random;

/**
 * Created by kyle on 9/16/2015.
 */
public abstract class Particle {
    float x = 0;
    float y = 0;
    float width = 4;
    float height = 4;
    float velX = 0;
    float velY = 0;
    String partType;
    boolean destroyed = false;
    int destroyTime = 0;
    int destroyMax = 120;
    Random random = new Random();


    float xDist; // distance from block to character x plane
    float yDist; // distance from block to character y plane
    int attractDist = 150; // max distance from player that this should still be drawn and updated
    boolean isAttracted; // boolean to tell whether it is too far away and should be drawn


    boolean facingRight; // false = going right, true = going left
    public Rectangle rect = new Rectangle();


    public Particle(int newX, int newY, float newVelX, float newVelY, String pType, int newDestroyMax) {
        this.x = newX;
        this.y = newY;
        this.velX = newVelX;
        this.velY = newVelY;
        this.partType = pType;
        this.destroyMax = newDestroyMax;

    }

    public void update(Array<BaseTile> baseTiles, Array<Roboto1> robot1s, int playerX, int playerY) {
        destroyTime++;

        if (destroyTime > destroyMax) destroyed = true;



        this.x += velX;
        this.y += velY;

        if (partType.equals("Shell")) {
            if (velX > 0) velX -= .05;
            else if (velX < 0) velX += .05;

            velY -= .25;

        } else if (partType.equals("Flame")) {
            if (velX > 0) velX -= .1;
            else if (velX < 0) velX += .1;

            this.height -= .02;
            this.width -= .02;

            velY += .1;

            if (velY > 2) velY = 2;



        } else if (partType.equals("Spark")) {
            velX *= 1.05;
            velY *= 1.05;
        } else if (partType.equals("EXP")) {
            //if the player is within attraction range accelerate toward player
            xDist = Math.abs(playerX - this.x);
            yDist = Math.abs(playerY - this.y);
            if (Math.sqrt((xDist * xDist) + (yDist * yDist)) > attractDist) isAttracted = false;
            else isAttracted = true;

            if (isAttracted) {
                if (this.x < playerX) velX += .2;
                else if (this.x > playerX) velX -= .2;

                if (this.y < playerY) velY += .2;
                else if (this.y > playerY) velY -= .2;

                if (velX > 3) velX = 3;
                else if (velX < -3) velX = -3;

                if (velY > 3) velY = 3;
                else if (velY < -3) velY = -3;



            } else velY -= .1;
        } else if (partType.equals("dust")) {
            width -= .5;
            height -= .5;
            if (width <= 0 || height <= 0) {
                destroyed = true;
            }
        }

        rect.x = x;
        rect.y = y;
        rect.width = width;
        rect.height = height;

        checkTilesHit(baseTiles);


    }

    public void draw(SpriteBatch batch) {

    }

    public void checkTilesHit(Array<BaseTile> baseTiles) {
        for (BaseTile tile : baseTiles) {
            if (tile.isActive) {
                if (rect.overlaps(tile.rect)) {
                    if (partType == "flyingSpin") destroyed = true;
                    float rect1Top = rect.y + rect.height;
                    float rect1Bot = rect.y;
                    float rect1Left = rect.x;
                    float rect1Right = rect.x + rect.width;
                    float rect2Top = tile.rect.y + tile.rect.height;
                    float rect2Bot = tile.rect.y;
                    float rect2Left = tile.rect.x;
                    float rect2Right = tile.rect.x + tile.rect.width;
                    float leftPassedDistance = Math.abs(rect2Right - rect1Left);
                    float rightPassedDistance = Math.abs(rect1Right - rect2Left);
                    float topPassedDistance = Math.abs(rect1Top - rect2Bot);
                    float bottomPassedDistance = Math.abs(rect2Top - rect1Bot);
                    String directionLeastPassed;
                    if (leftPassedDistance < rightPassedDistance &&
                            leftPassedDistance < topPassedDistance &&
                            leftPassedDistance < bottomPassedDistance) directionLeastPassed = "Left";
                    else if (rightPassedDistance < topPassedDistance &&
                            rightPassedDistance < bottomPassedDistance) directionLeastPassed = "Right";
                    else if (topPassedDistance < bottomPassedDistance) directionLeastPassed = "Top";
                    else directionLeastPassed = "Bottom";


                    if (directionLeastPassed.equals("Bottom")) {
                        velY = -(int) (velY * .8);
                        if (partType.equals("EXP"))
                            y --;

                    } else if (directionLeastPassed.equals("Top")) {
                        velY = -(int) (velY * .8);
                    } else if (directionLeastPassed.equals("Left")) {
                        velX = -(int) (velX * .8);
                    } else {
                        velX = -(int) (velX * .8);
                    }


                }


            }
        }
    }
}
