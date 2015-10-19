package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

/**
 * Created by kyle on 10/18/2015.
 */
public class BossFly extends Enemy {

    int xDist; // distance from block to character x plane
    int yDist; // distance from block to character y plane
    int drawDist = 500; // max distance from player that this should still be drawn and updated
    boolean isActive; // boolean to tell whether it is too far away and should be drawn

    int orbitX; // x coordinate of what the fly orbits around
    int orbitY; // y coordinate of what the fly orbits around

    int orbitXStrength;
    int orbitYStrength;

    Texture image = new Texture("SPA/Decoration/Fly/fly-1.png");
    Texture image2 = new Texture("SPA/Decoration/Fly/fly-2.png");
    Texture image3 = new Texture("SPA/Decoration/Fly/fly-3.png");
    Texture[] images = {image, image2, image3};

    private int animImage = 0;
    private int frameCount = 0;
    private int framesSwitch = 6; // TODO: idea, could possibly have framesswitch go up as time goes on to seem like spinning is slowing

    public BossFly(float x, float y) {
        this.x = x;
        this.y = y;
        orbitXStrength = random.nextInt(4) +6;
        orbitYStrength = random.nextInt(4) + 6;
        width = 18;
        //TODO: make attraction force stronger ther farther it is from the core

        height = 18;
    }



    Random random = new Random();

    public Rectangle getRect() {
        return rect;
    }

    public boolean getDestroyed() {
        return destroyed;
    }

    public void changeHealth(int addToHealth) {
        health += addToHealth;
    }
    public void draw(SpriteBatch batch) {
        //TODO: draw function
        batch.draw(images[animImage], x, y, width,height);
        batch.draw(images[animImage], x, y, width/2,height/2, width, height,1, 1, angleDegrees, 0, 0 , width/2, height/2, false, false);
    }

    public void setOrbitPt(float orbitX, float orbitY) {
        this.orbitX = (int)orbitX;
        this.orbitY = (int)orbitY;
    }

    public void moveThis(int playerX, int playerY) {
        if (orbitX > this.x) {
            velX += .01 * orbitXStrength;
        } else {
            velX -= .01 * orbitXStrength;
        }
        if (orbitY > this.y) {
            velY += .01 * orbitYStrength;
        } else {
            velY -= .01 * orbitYStrength;
        }
        if (velX > 9) velX = 9;
        else if (velX < -9) velX = -9;
        if (velY > 9) velY = 9;
        else if (velY < -9) velY = -9;
    }
    public void update(Array<Enemy> enemies, Array<EnemyBullet> enemyBullets, Array<PlayerBullet> bullets,Array<Item> items, Array<BaseTile> baseTiles, Array<Particle> particle1s, int playerX, int playerY) {
        //xDist = (int)Math.abs(playerX - this.x);
        //yDist = (int)Math.abs(playerY - this.y);
        //player distance irrelevant currently
        //TODO: make fly face heart and move 90 degrees clockwise to this

        xDist = (int) (orbitX - x);
        yDist = (int) (orbitY - y);

        double angleRadians = Math.atan2(yDist, xDist);
        int angleDegrees = (int)Math.toDegrees(angleRadians);

        //setRotation(angleDegrees);



        if (Math.sqrt((xDist * xDist) + (yDist * yDist)) > drawDist) isActive = false;
        else isActive = true;

        for (Enemy enemy : enemies) {
            if (enemy.getType().equals("FlyBoss")) {
                setOrbitPt(enemy.x, enemy.y);
            }
        }

        frameCount++;
        if (frameCount > framesSwitch) {
            frameCount = 0;
            animImage ++;
            if (animImage > 2) animImage = 0;
        }


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

    public void destroyRobot(Array<Particle> particles, Array<Item> items) {

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
                        velY *= -1;

                    } else if (directionLeastPassed.equals("Top")) {
                        rect.y = rect2Bot - rect.height;
                        velY *= -1;
                    } else if (directionLeastPassed.equals("Left")) {
                        rect.x = rect2Right;
                        velX *= -1;

                    } else {
                        rect.x = rect2Left - rect.width;
                        velX *= -1;

                    }
                }
                y = (int) rect.y;
                x = (int) rect.x;

            }
        }
    }

}
