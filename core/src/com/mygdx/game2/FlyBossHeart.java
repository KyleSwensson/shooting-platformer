package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

/**
 * Created by kyle on 10/16/2015.
 */
public class FlyBossHeart extends Enemy implements Boss {


    int xDist; // distance from block to character x plane
    int yDist; // distance from block to character y plane
    int drawDist = 500; // max distance from player that this should still be drawn and updated
    boolean isActive; // boolean to tell whether it is too far away and should be drawn
    Texture image = new Texture("bossCrystal.png");


    Random random = new Random();

    public FlyBossHeart(int x, int y) {
        this.x = x;
        this.y = y;
        health = 500;
        width = 24;
        height = 36;
        image = new Texture("bossCrystal.png");

    }

    public Rectangle getRect() {
        return rect;
    }

    public void update(Array<Enemy> enemies,Array<EnemyBullet> enemyBullets, Array<PlayerBullet> bullets,Array<Item> items, Array<BaseTile> baseTiles, Array<Particle> particle1s, int playerX, int playerY) {
        xDist = (int)Math.abs(playerX - this.x);
        yDist = (int)Math.abs(playerY - this.y);
        if (Math.sqrt((xDist * xDist) + (yDist * yDist)) > drawDist) isActive = false;
        else isActive = true;



        if (isActive) {








            moveThis(playerX, playerY);


            if (health <= 0) {
                destroyRobot(particle1s, items);
            }







            x += velX;
            y += velY;

            rect.x = x;
            rect.y = y;
            rect.width = width;
            rect.height = height;


            checkTilesHit(baseTiles);


        }
    }

    public void draw(SpriteBatch batch) {
        batch.draw(image,x,y,width,height);
    }

    private void destroyRobot(Array<Particle> particle1s, Array<Item> items) {
        destroyed = true;
        //TODO: other things that hap when boss dies
    }

    private void moveThis(int playerX, int playerY) {
        if (playerX > this.x) velX += .02;
        else if (playerX < this.x) velX -= .02;

        if (velX > 1) velX = 1;
        else if (velX < -1) velX = -1;

        if (playerY > this.y) velY += .02;
        if (playerY < this.y) velY -= .02;
        if (velY > 1) velY = 1;
        if (velY < -1) velY = -1;
    }


    public void changeHealth(int addToHealth) {
        health += addToHealth;
    }

    public boolean getDestroyed() {
        return destroyed;
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
                        rect.y = rect2Top;
                        velY = 0;

                    } else if (directionLeastPassed.equals("Top")) {
                        rect.y = rect2Bot - rect.height;
                        velY = 0;
                    } else if (directionLeastPassed.equals("Left")) {
                        rect.x = rect2Right;
                        velX = 0;

                    } else {
                        rect.x = rect2Left - rect.width;
                        velX = 0;

                    }
                }
                y = (int) rect.y;
                x = (int) rect.x;

            }
        }
    }
}