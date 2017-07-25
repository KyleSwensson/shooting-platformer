package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

/**
 * Created by kyle on 9/16/2015.
 */
public class Scorpain extends Enemy {

    public static final int DIFFICULTY_POINTS = 40;

    boolean facingRight = false; // if facing = false player is facing left, if facing = true player = facing right
    int x = 0;
    int y = 0;
    int width;
    int height;
    float velX;
    float velY;
    int health = 3;
    boolean destroyed = false;
    boolean flying = false;

    Texture image = new Texture("SPA/Enemy/Scorpain/1.png");
    Texture jumpImage = new Texture("SPA/Enemy/Scorpain/jump/1.png");

    boolean canHop;
    int timeUntilCanHop;
    int maxTimeUntilCanHop = 130;

    Random random = new Random();

    int xDist; // distance from block to character x plane
    int yDist; // distance from block to character y plane
    int drawDist = 500; // max distance from player that this should still be drawn and updated
    boolean isActive; // boolean to tell whether it is too far away and should be drawn


    Boolean touchingGround = false;
    Rectangle rect = new Rectangle();

    Rectangle visionRect = new Rectangle();


    public Scorpain(int x, int y) {
        health = 30;
        this.x = x;
        this.y = y;

        canHop = false;
        timeUntilCanHop = maxTimeUntilCanHop;

        this.width = 40;
        this.height = 26;
        this.rect.width = this.width;
        this.rect.height = this.height;
        facingRight = false;

        this.visionRect.width = 220;
        this.visionRect.height = 70;
    }

    public Rectangle getRect() {
        return rect;
    }

    public void changeHealth(int addToHealth) {
        health += addToHealth;
    }

    public void update(Array<Enemy> enemies, Array<EnemyBullet> enemyBullets, Array<PlayerBullet> bullets, Array<Item> items, Array<BaseTile> baseTiles, Array<Particle> particle1s, int playerX, int playerY) {
        xDist = Math.abs(playerX - this.x);
        yDist = Math.abs(playerY - this.y);
        if (Math.sqrt((xDist * xDist) + (yDist * yDist)) > drawDist) isActive = false;
        else isActive = true;

        if (isActive) {

            moveScorp(playerX, playerY);

            if (health <= 0) {
                destroyEnemy(particle1s, items);
                destroyed = true;
            }

            velY -= .3;

            x += velX;
            y += velY;

            rect.x = x;
            rect.y = y;

            if (facingRight) {
                visionRect.x = rect.x + rect.width;
            } else {
                visionRect.x = rect.x - visionRect.width;
            }
            visionRect.y = rect.y;

            checkTilesHit(baseTiles);

            if (visionRect.contains(playerX, playerY)) {
                if (playerX > x) {
                    facingRight = true;
                } else if (playerX < x) {
                    facingRight = false;
                }
            }

        }
    }

    public void draw(SpriteBatch batch) {
        if (touchingGround) {
            batch.draw(image, x, y, width, height, 0, 0, 20, 13, !facingRight, false);
        } else {
            if (Math.abs(velX) > 3) {
                batch.draw(jumpImage, x, y, width, height, 0, 0, 20, 13, !facingRight, false);
            } else {
                batch.draw(image, x, y, width, height, 0, 0, 20, 13, !facingRight, false);
            }
        }
    }

    public void destroyEnemy(Array<Particle> particle1s, Array<Item> items) {

        if (random.nextInt(2) == 0) {
            HealthCrystal hp = new HealthCrystal(this.x,
                    this.y,
                    -velX + random.nextInt(3) - 1,
                    3 + random.nextInt(3) - 1,
                    10, 12);
            items.add(hp);
        }

        if (random.nextInt(2) == 0) {
            ManaCrystal mp = new ManaCrystal(this.x,
                    this.y,
                    -velX + random.nextInt(3) - 1,
                    3 + random.nextInt(3) - 1,
                    10, 12);
            items.add(mp);
        }

        int numCoins = random.nextInt(3); // spawn between 0 and 2 coins

        System.out.print(numCoins);

        if (numCoins >= 1) {
            BasicCoin coin = new BasicCoin(this.x,
                    this.y,
                    random.nextInt(5) - 2,
                    3 + random.nextInt(3) - 1,
                    10, 12);
            items.add(coin);
        }

        if (numCoins >= 2) {
            BasicCoin coin2 = new BasicCoin(this.x,
                    this.y,
                    random.nextInt(5) - 2,
                    3 + random.nextInt(3) - 1,
                    10, 12);
            items.add(coin2);
        }
    }

    public boolean getDestroyed() {
        return destroyed;
    }

    public void moveLeft() {
        velX -= .2;
        if (velX < -1.5) {
            velX = -1.5f;
        }
    }

    public void moveRight() {
        velX += .2;
        if (velX > 1.5) {
            velX = 1.5f;
        }
    }

    private void moveScorp(int playerX, int playerY) {
        if (touchingGround) {

            if (facingRight) {
                moveRight();
            } else {
                moveLeft();
            }

        } else {
            if (velX == 0) {
                if (facingRight && velX == 0) {
                    velX = 1;
                } else {
                    velX = -1;
                }
            }
        }

        if (canHop && touchingGround && visionRect.contains(playerX, playerY)) {
            if (playerX - x > rect.height) {
                velY = 6;
            } else {
                velY = 5;
            }
            if (playerX > this.x) {
                velX = 6.5f;
            } else {
                velX = -6.5f;
            }
            canHop = false;
            timeUntilCanHop = maxTimeUntilCanHop;
        } else {
            timeUntilCanHop--;
            if (timeUntilCanHop < 0) {
                timeUntilCanHop = maxTimeUntilCanHop;
                canHop = true;
            }
        }

    }


    public void checkTilesHit(Array<BaseTile> baseTiles) {
        touchingGround = false;
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
                        touchingGround = true;
                    } else if (directionLeastPassed.equals("Top")) {
                        rect.y = rect2Bot - rect.height;
                        velY = 0;
                    } else if (directionLeastPassed.equals("Left")) {
                        rect.x = rect2Right;
                        velX = 0;
                        facingRight = true;
                    } else {
                        rect.x = rect2Left - rect.width;
                        velX = 0;
                        facingRight = false;
                    }
                }
                y = (int) rect.y;
                x = (int) rect.x;

            }
        }
    }
}
