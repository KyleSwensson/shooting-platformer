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

    public static final int DIFFICULTY_POINTS = 40;

    int xDist; // distance from block to character x plane
    int yDist; // distance from block to character y plane
    int drawDist = 500; // max distance from player that this should still be drawn and updated
    boolean isActive; // boolean to tell whether it is too far away and should be drawn

    Texture image = new Texture("robot1fly.png");


    Texture standingImage = new Texture("SPA/Enemy/Skeleton/Idle/1.png");
    Texture fallingImage = new Texture("SPA/Enemy/Skeleton/Fall/Fall.png");
    Texture jumpingImage = new Texture("SPA/Enemy/Skeleton/Jump/Jump.png");

    Texture[] runningImages = new Texture[10];
    Texture run1 = new Texture("SPA/Enemy/Skeleton/Run/1.png");
    Texture run2 = new Texture("SPA/Enemy/Skeleton/Run/2.png");
    Texture run3 = new Texture("SPA/Enemy/Skeleton/Run/3.png");
    Texture run4 = new Texture("SPA/Enemy/Skeleton/Run/4.png");
    Texture run5 = new Texture("SPA/Enemy/Skeleton/Run/5.png");
    Texture run6 = new Texture("SPA/Enemy/Skeleton/Run/6.png");
    Texture run7 = new Texture("SPA/Enemy/Skeleton/Run/7.png");
    Texture run8 = new Texture("SPA/Enemy/Skeleton/Run/8.png");
    Texture run9 = new Texture("SPA/Enemy/Skeleton/Run/9.png");
    Texture run10 = new Texture("SPA/Enemy/Skeleton/Run/10.png");

    int timeUntilNextMoveImage = 0;
    int maxTimeUntilNextMoveImage = 5;

    int currMoveImage = 0;

    int health = 30;

    boolean destroyed = false;


    float x;
    float y;

    Random random = new Random();

    boolean facingRight = false;

    boolean touchingGround = false;

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


    public SmartEnemy(int x, int y) {
        timeUntilNextMoveImage = maxTimeUntilNextMoveImage;
        this.x = x;
        this.y = y;
        this.width = 26;
        this.height = 36;

        runningImages[0] = run1;
        runningImages[1] = run2;
        runningImages[2] = run3;
        runningImages[3] = run4;
        runningImages[4] = run5;
        runningImages[5] = run6;
        runningImages[6] = run7;
        runningImages[7] = run8;
        runningImages[8] = run9;
        runningImages[9] = run10;
    }


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

    public void update(Array<Enemy> enemies, Array<EnemyBullet> enemyBullets, Array<PlayerBullet> bullets,Array<Item> items, Array<BaseTile> baseTiles, Array<Particle> particle1s, int playerX, int playerY) {
        xDist = (int)Math.abs(playerX - this.x);
        yDist = (int)Math.abs(playerY - this.y);
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

                destroyed = true;
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


    public Rectangle getRect() {
        return rect;
    }

    public void changeHealth(int addToHealth) {
        health += addToHealth;
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
                        canJump = true;
                        touchingGround = true;
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

            if (Math.abs(velX) >= 1) {
                timeUntilNextMoveImage--;
                if (timeUntilNextMoveImage <= 0) {
                    timeUntilNextMoveImage = maxTimeUntilNextMoveImage;
                    currMoveImage++;
                    if (currMoveImage > 9) {
                        currMoveImage = 0;
                    }
                }
            } else {
                currMoveImage = 0;
                timeUntilNextMoveImage = maxTimeUntilNextMoveImage;
            }






    }

    public boolean getDestroyed() {
        return destroyed;
    }

    public void draw(SpriteBatch batch) {
        if (touchingGround) {
            if (Math.abs(velX) < 1) {
                batch.draw(standingImage, x, y, width, height, 0, 0, 13, 18, !facingRight, false);
            } else {
                batch.draw(runningImages[currMoveImage], x, y, width, height, 0, 0, 13, 18, !facingRight, false);
            }
        } else {
            if (velY > 0) {
                batch.draw(jumpingImage, x,y,width,height, 0, 0, 13,18, !facingRight, false);
            } else {
                batch.draw(fallingImage, x, y, width, height, 0, 0, 13, 18, !facingRight, false);
            }
        }
    }
}