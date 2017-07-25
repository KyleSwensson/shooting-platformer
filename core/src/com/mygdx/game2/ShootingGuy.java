package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

/**
 * Created by kyle on 10/11/2015.
 */
public class ShootingGuy extends Enemy {

    public static final int DIFFICULTY_POINTS = 40;

    int xDist; // distance from block to character x plane
    int yDist; // distance from block to character y plane
    int drawDist = 500; // max distance from player that this should still be drawn and updated
    boolean isActive; // boolean to tell whether it is too far away and should be drawn

    Texture image = new Texture("shootingRobot.png");

    int health = 30;

    boolean destroyed = false;

    boolean canJump = false;

    Random random = new Random();

    boolean facingRight = false;

    boolean onEdge = false; // var tells if enemy is on edge, if it is it will not move forward but continue facing towards the player

    boolean isShooting = false;
    int timeUntilShoot = 30; // counts down when isShooting = false when 0 isShooting = true;
    int shootTime = 20; // when isShooting goes down to 0 then shoots then isnt shooting anymore


    boolean turnRectCollided;
    Rectangle turnAroundRect = new Rectangle(0,0,25,25); // rectangle that is 1 robot width to the direction the robot is moving and 1 robot width down
    //if it is not intersecting any blocks thenthe robot will fall off edge soon so turn around



    public ShootingGuy(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = 32;
        this.height = 32;
    }

    public void update(Array<Enemy> enemies, Array<EnemyBullet> enemyBullets, Array<PlayerBullet> bullets,Array<Item> items, Array<BaseTile> baseTiles, Array<Particle> particle1s, int playerX, int playerY) {
        xDist = (int)Math.abs(playerX - this.x);
        yDist = (int)Math.abs(playerY - this.y);
        if (Math.sqrt((xDist * xDist) + (yDist * yDist)) > drawDist) isActive = false;
        else isActive = true;



        if (isActive) {

            if (facingRight) {
                turnAroundRect.x = x + 50;
            } else {
                turnAroundRect.x = x - 50;
            }
            turnAroundRect.y = y - 32 / 2;

            turnRectCollided = false;
            for (BaseTile tile : baseTiles) {
                if (turnAroundRect.overlaps(tile.rect)) {
                    turnRectCollided = true;
                }
            }
            if (turnRectCollided) {
                onEdge = false;
            } else {
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

    public Rectangle getRect() {
        return rect;
    }

    public void changeHealth(int addToHealth) {
        health += addToHealth;
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
        if (!isShooting) {
            if (playerX > this.x) {
                facingRight = true;
                velX += 1;
            } else {
                facingRight = false;
                velX -= 1;
            }


            if (velX > 3) velX = 3;
            else if (velX < -3) velX = -3;

            if (onEdge) {
                velX = 0;
            }

            if (timeUntilShoot > 0) {
                timeUntilShoot--;
            } else {
                timeUntilShoot = 100;
                isShooting = true;
            }

        } else {
            if (shootTime > 0) {
                velX = 0;
                shootTime --;
            } else {
                BigAuraBullet bul = new BigAuraBullet();
                bul.x = (int)this.x;
                bul.y = (int)this.y + 25;

                bul.velX = 0;
                bul.velY = 2;
                enemyBullets.add(bul);
                shootTime = 50;
                timeUntilShoot = 300;
                velX = 0;
                isShooting = false;
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

    public void draw(SpriteBatch batch) {

        batch.draw(image,x,y,width,height, 0, 0, 32, 32, !facingRight, false);

    }
}
