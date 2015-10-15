package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

/**
 * Created by kyle on 10/11/2015.
 */
public class SmartEnemy extends Enemy {

    int xDist; // distance from block to character x plane
    int yDist; // distance from block to character y plane
    int drawDist = 500; // max distance from player that this should still be drawn and updated
    boolean isActive; // boolean to tell whether it is too far away and should be drawn

    Texture image = new Texture("robot1fly.png");

    int health = 30;

    boolean destroyed = false;


    Random random = new Random();

    boolean facingRight = false;

    boolean onEdge = false; // var tells if enemy is on edge, if it is it will not move forward but continue facing towards the player

    boolean isShooting = false;
    int timeUntilShoot = 30; // counts down when isShooting = false when 0 isShooting = true;
    int shootTime = 20; // when isShooting goes down to 0 then shoots then isnt shooting anymore


    boolean canJump = false;

    boolean turnRectCollided;
    boolean jumpRectCollided;
    boolean jumpHeightRectCollided;
    Rectangle turnAroundRect = new Rectangle(0,0,25,25); // rectangle that is 1 robot width to the direction the robot is moving and 1 robot width down
    //if it is not intersecting any blocks thenthe robot will fall off edge soon so turn around
    Rectangle jumpRect = new Rectangle(0,0,25,25); // rectangle that is 1.5 robot widths to the direction the robot is moving, if there is a block there
    //then the robot will jump
    Rectangle jumpHeightRect = new Rectangle(0,0,25,25); //rectangle that is jump height above jump rect which tells the robot if there is a place
    // to land when it jumps



    public void jump() {
        if (canJump) {
            this.y += 5;
            this.velY = 8;
            canJump = false;
        }
    }
    public void shortJump() {
        if (canJump) {
            this.y += 3;
            this.velY = 4;
            canJump = false;
        }
    }

    public void update(Array<EnemyBullet> enemyBullets, Array<PlayerBullet> bullets,Array<Item> items, Array<BaseTile> baseTiles, Array<Particle> particle1s, int playerX, int playerY) {
        xDist = Math.abs(playerX - this.x);
        yDist = Math.abs(playerY - this.y);
        if (Math.sqrt((xDist * xDist) + (yDist * yDist)) > drawDist) isActive = false;
        else isActive = true;



        if (isActive) {

            if (facingRight) {
                turnAroundRect.x = x + 50;
                jumpRect.x = x + 50;
                jumpHeightRect.x = x + 50;
            } else {
                turnAroundRect.x = x - 50;
                jumpRect.x = x - 50;
                jumpHeightRect.x = x -50;
            }
            turnAroundRect.y = y - 32 / 2;
            jumpRect.y = y;
            jumpHeightRect.y = y + 120;

            turnRectCollided = false;
            jumpRectCollided = false;
            jumpHeightRectCollided = false;
            for (BaseTile tile : baseTiles) {
                if (turnAroundRect.overlaps(tile.rect)) {
                    turnRectCollided = true;
                }
                if (jumpRect.overlaps(tile.rect)) {
                    jumpRectCollided = true;
                }
                if (jumpHeightRect.overlaps(tile.rect)) {
                    jumpHeightRectCollided = true;
                }
            }
            onEdge = false;
            if (!turnRectCollided) {
                shortJump();
            } else if (jumpRectCollided && !jumpHeightRectCollided) {
                jump();
            } else if (jumpRectCollided && jumpHeightRectCollided) {
                onEdge = true;
            }


            moveEnemy(playerX, enemyBullets);


            if (health <= 0) {
                destroyEnemy(particle1s, items);
            }



            if (velY < 15) velY -= .3;

            x += velX;

            y += velY;

            rect.x = x;
            rect.y = y;
            rect.width = width;
            rect.height = height;


            checkTilesHit(baseTiles);


        }
    }

    public Rectangle getRect() {
        return rect;
    }

    public void changeHealth(int addToHealth) {
        health += addToHealth;
    }

    private void destroyEnemy(Array<Particle> particle1s, Array<Item> items) {
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

    private void moveEnemy(int playerX, Array<EnemyBullet> enemyBullets) {

            if (onEdge && canJump) facingRight = !facingRight;

            if (facingRight) {
                velX += 1;
            } else {
                velX -= 1;
            }


            if (velX > 3) velX = 3;
            else if (velX < -3) velX = -3;






    }

    public boolean getDestroyed() {
        return destroyed;
    }

    public void draw(SpriteBatch batch) {

        batch.draw(image,x,y,width,height, 0, 0, 32, 32, !facingRight, false);

    }
}