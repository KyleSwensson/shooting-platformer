package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Created by kyle on 10/10/2015.
 */
public class HealthCrystal extends PlayerItem {

    int destroyTime = 0;
    int destroyMax = 1000;

    float playerX;
    float playerY;
    boolean destroyed;
    float xDist;
    float yDist;
    int attractDist = 25; // max distance from player that this should still be drawn and updated
    boolean isAttracted = false;
    public Rectangle rect = new Rectangle();


    Texture texture1 = new Texture("SPA/Items/HP1.png");
    Texture texture2 = new Texture("SPA/Items/HP2.png");
    Texture texture3 = new Texture("SPA/Items/HP3.png");
    Texture texture4 = new Texture("SPA/Items/HP4.png");
    Texture texture5 = new Texture("SPA/Items/HP5.png");
    Texture[] textures = {texture1, texture3, texture4, texture2, texture5};

    private int animImage = 0;
    private int frameCount = 0;
    private int framesSwitch = 6; // TODO: idea, could possibly have framesswitch go up as time goes on to seem like spinning is slowing

    public HealthCrystal(float x, float y, float velX, float velY, int width, int height) {
        super(x,y,velX,velY,width,height);
        destroyed = false;
        type = "Health";
    }



    public void update(Array<BaseTile> tiles, Rectangle playerRect) {

        frameCount ++;
        if (frameCount > framesSwitch) {
            animImage ++;

            frameCount = 0;
            if (animImage > 4) {
                animImage = 0;
            }
        }

        destroyTime++;

        if (destroyTime > destroyMax) destroyed = true;



        this.x += velX;
        this.y += velY;

        /*if (partType.equals("Shell")) {
            if (velX > 0) velX -= .05;
            else if (velX < 0) velX += .05;

            velY -= .25;

        } */
            //if the player is within attraction range accelerate toward player
            xDist = Math.abs(playerRect.x - this.x);
            yDist = Math.abs(playerRect.y - this.y);
            if (Math.sqrt((xDist * xDist) + (yDist * yDist)) > attractDist) isAttracted = false;
            else isAttracted = true;

            if (isAttracted) {
                if (this.x < playerRect.x) velX += .2;
                else if (this.x > playerRect.x) velX -= .2;

                if (this.y < playerRect.y) velY += .2;
                else if (this.y > playerRect.y) velY -= .2;

                if (velX > 3) velX = 3;
                else if (velX < -3) velX = -3;

                if (velY > 3) velY = 3;
                else if (velY < -3) velY = -3;



            } else velY -= .1;


        rect.x = x;
        rect.y = y;
        rect.width = width;
        rect.height = height;

        checkTilesHit(tiles);
    }



    public Rectangle getRect() {
        return rect;
    }

    public String getType(){
        return type;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public void checkTilesHit(Array<BaseTile> baseTiles) {
        for (BaseTile tile : baseTiles) {
            if (tile.isActive) {
                if (rect.overlaps(tile.rect)) {
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
                        velX *= .8;

                        y = (int)rect2Top;


                    } else if (directionLeastPassed.equals("Top")) {
                        velY = -(int) (velY * .8);
                        velX *= .8;
                        y = (int)(rect2Bot - rect.height);
                    } else if (directionLeastPassed.equals("Left")) {
                        velX = -(int) (velX * .7);
                        x = (int)rect2Right;
                    } else {
                        velX = -(int) (velX * .7);
                        x = (int)(rect2Left - rect.width);
                    }


                }


            }
        }
    }
    public boolean getDestroyed() {
        return this.destroyed;
    }

    public void draw(SpriteBatch batch) {


        batch.draw(textures[animImage], x, y, width,height);


    }
}


