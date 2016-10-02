package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

/**
 * Created by kyle on 9/16/2015.
 */
public class Roboto1 extends Enemy {
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

    Texture image = new Texture("robot1.png");


    boolean turnRectCollided;
    Rectangle turnAroundRect = new Rectangle(0,0,25,25); // rectangle that is 1 robot width to the direction the robot is moving and 1 robot width down
    //if it is not intersecting any blocks then the robot will fall off edge soon so turn around

    Random random = new Random();

    boolean canJump = false;

    int bulletSpawnCounter = 0;
    final int bullet1SpawnTime = 8;

    int xDist; // distance from block to character x plane
    int yDist; // distance from block to character y plane
    int drawDist = 500; // max distance from player that this should still be drawn and updated
    boolean isActive; // boolean to tell whether it is too far away and should be drawn


    Boolean touchingGround = false;
    Boolean touchingCeiling = false;
    Rectangle rect = new Rectangle();

    public Roboto1() {
        health = 30;

    }

    public Rectangle getRect() {
        return rect;
    }

    public void changeHealth(int addToHealth) {
        health += addToHealth;
    }

    public void update(Array<Enemy> enemies, Array<EnemyBullet> enemyBullets, Array<PlayerBullet> bullets,Array<Item> items, Array<BaseTile> baseTiles, Array<Particle> particle1s, int playerX, int playerY) {
        xDist = Math.abs(playerX - this.x);
        yDist = Math.abs(playerY - this.y);
        if (Math.sqrt((xDist * xDist) + (yDist * yDist)) > drawDist) isActive = false;
        else isActive = true;





        if (isActive) {

            //TODO: make an x and y in 10 frames variable and if y is lower than current turn around

            if (facingRight) {
                turnAroundRect.x = x + width;
            } else {
                turnAroundRect.x = x - width;
            }
            turnAroundRect.y = y - height / 2;

            turnRectCollided = false;
            for (BaseTile tile : baseTiles) {
                if (turnAroundRect.overlaps(tile.rect)) {
                    turnRectCollided = true;
                }
            }
            if (!turnRectCollided) {
                facingRight = !facingRight;
            }


            if (!flying) {
                moveGroundRobot(playerX);
            } else {
                moveFlyingRobot(playerX);
            }

            if (health <= 0) {
                destroyEnemy(particle1s, items);
                destroyed = true;
            }

            if (velX == 3 || velX == -3) {
                ShellParticle part = new ShellParticle(this.x,
                        this.y,
                        -velX + random.nextInt(3) - 1,
                        3 + random.nextInt(3) - 1,
                        "Standard",
                        60
                );
                if (velX == 3) part.x = this.x;
                else if (velX == -3) part.x = this.x + this.width;

                particle1s.add(part);
            }
            if (!flying) {
                if (velY < 15) velY -= .3;
            } else {
                if (playerY > this.y) velY += .2;
                if (playerY < this.y) velY -= .2;

                if (velY > 5) velY = 5;
                if (velY < -5) velY = -5;
            }


            if (velX > 0) facingRight = true;
            else if (velX < 0) facingRight = false;

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

        batch.draw(image,x,y,width,height, 0, 0, 32, 32, !facingRight, false);

    }

    public void destroyEnemy(Array<Particle> particle1s, Array<Item> items) {


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



    private void moveFlyingRobot(int playerX) {
        if (playerX > this.x) velX += .4;
        else if (playerX < this.x) velX -= .4;

        if (velX > 2.5) velX = 2.5f;
        else if (velX < -2.5) velX = -2.5f;
    }

    public boolean getDestroyed() {
        return destroyed;
    }

    private void moveGroundRobot(int playerX) {
        if (facingRight) velX += 1;
        else velX -= 1;


        if (velX > 3) velX = 3;
        else if (velX < -3) velX = -3;
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
                        canJump = true;
                    } else if (directionLeastPassed.equals("Top")) {
                        rect.y = rect2Bot - rect.height;
                        velY = 0;
                    } else if (directionLeastPassed.equals("Left")) {
                        rect.x = rect2Right;
                        velX = 0;
                        if (!flying) facingRight = !facingRight;
                    } else {
                        rect.x = rect2Left - rect.width;
                        velX = 0;
                        if (!flying) facingRight = !facingRight;
                    }
                }
                y = (int) rect.y;
                x = (int) rect.x;

            }
        }
    }
}
