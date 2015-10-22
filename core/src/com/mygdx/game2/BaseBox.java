package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.List;
import java.util.Random;

/**
 * Created by kyle on 10/21/2015.
 */
public class BaseBox extends Enemy {

    Random random = new Random();
    boolean destroyed;
    boolean isActive;
    float xDist;
    float yDist;
    float drawDist = 500; // distance from player that this continues to update

    //TODO: make the tile drawdist be a little more than the drawDist for everythign else which should be all the same

    public Texture image = new Texture("SPA/Objects/Box/box.png");
    public BaseBox(float x, float y, float velX, float velY, int width, int height) {
        enemyType = "safe";
        health = 40;
        this.x = x;
        this.y = y;
        this.velX = velX;
        this.velY = velY;
        this.width = width;
        this.height = height;
        rect.width = width;
        rect.height = height;
        rect.x = x;
        rect.y = y;
        destroyed = false;
        isActive = false;
    }

    public boolean getDestroyed() {
        return destroyed;
    }

    public void changeHealth(int addToHealth) {
        health += addToHealth;
    }

    public void destroyedAnim(Array<Item> items) {
        destroyed = true;

        HealthCrystal hp = new HealthCrystal(this.x,
                this.y,
                -velX + random.nextInt(3) - 1,
                3 + random.nextInt(3) - 1,
                10,12);
        items.add(hp);

        ManaCrystal mp = new ManaCrystal(this.x,
                this.y,
                -velX + random.nextInt(3) - 1,
                3 + random.nextInt(3) - 1,
                10,12);
        items.add(mp);
    }

    public void update (Array<Enemy> enemies, Array<EnemyBullet> enemyBullets,Array<PlayerBullet> bullets,Array<Item> items, Array<BaseTile> baseTiles, Array<Particle> particle1s, int playerX, int playerY) {
        if (health <= 0) {
            destroyedAnim(items);
        }

        xDist = Math.abs(playerX - this.x);
        yDist = Math.abs(playerY - this.y);
        if (Math.sqrt((xDist * xDist) + (yDist * yDist)) > drawDist) isActive = false;
        else isActive = true;


        if (isActive) {
            velY -= .1;
            if (velX > 0) velX -= .025;
            else if (velX < 0) velX += .025;

            y += velY;
            x += velX;

            rect.x = x;
            rect.y = y;

            checkTilesHit(baseTiles);

            rect.x = x;
            rect.y = y;
        }


    }

    public void draw(SpriteBatch batch){
        batch.draw(image,x,y,width,height);
    }

    public Rectangle getRect() {
        return rect;
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
                        velY = 0;
                        velX *= .8;

                        y = (int)rect2Top;


                    } else if (directionLeastPassed.equals("Top")) {
                        velY = 0;
                        velX *= .8;
                        y = (int)(rect2Bot - rect.height);
                    } else if (directionLeastPassed.equals("Left")) {
                        velX = 0;
                        x = (int)rect2Right;
                    } else {
                        velX = 0;
                        x = (int)(rect2Left - rect.width);
                    }


                }


            }
        }
    }
}
