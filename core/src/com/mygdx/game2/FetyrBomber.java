package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

/**
 * Created by kyle on 10/11/2015.
 */
public class FetyrBomber extends Enemy {

    public static final int DIFFICULTY_POINTS = 40;

    int xDist; // distance from block to character x plane
    int yDist; // distance from block to character y plane
    int drawDist = 500; // max distance from player that this should still be drawn and updated
    boolean isActive; // boolean to tell whether it is too far away and should be drawn

    Texture image1 = new Texture("SPA/Enemy/Fetyr/Bomber/1.png");
    Texture image2 = new Texture("SPA/Enemy/Fetyr/Bomber/2.png");
    Texture image3 = new Texture("SPA/Enemy/Fetyr/Bomber/3.png");
    Texture image4 = new Texture("SPA/Enemy/Fetyr/Bomber/4.png");
    Texture image5 = new Texture("SPA/Enemy/Fetyr/Bomber/5.png");
    Texture image6 = new Texture("SPA/Enemy/Fetyr/Bomber/6.png");
    Texture image7 = new Texture("SPA/Enemy/Fetyr/Bomber/7.png");
    Texture image8 = new Texture("SPA/Enemy/Fetyr/Bomber/8.png");

    Texture runImage1 = new Texture("SPA/Enemy/Fetyr/Bomber/Running/1.png");
    Texture runImage2 = new Texture("SPA/Enemy/Fetyr/Bomber/Running/2.png");
    Texture runImage3 = new Texture("SPA/Enemy/Fetyr/Bomber/Running/3.png");
    Texture runImage4 = new Texture("SPA/Enemy/Fetyr/Bomber/Running/4.png");
    Texture runImage5 = new Texture("SPA/Enemy/Fetyr/Bomber/Running/5.png");
    Texture runImage6 = new Texture("SPA/Enemy/Fetyr/Bomber/Running/6.png");
    Texture runImage7 = new Texture("SPA/Enemy/Fetyr/Bomber/Running/7.png");
    Texture runImage8 = new Texture("SPA/Enemy/Fetyr/Bomber/Running/8.png");
    Texture runImage9 = new Texture("SPA/Enemy/Fetyr/Bomber/Running/9.png");
    Texture runImage10 = new Texture("SPA/Enemy/Fetyr/Bomber/Running/10.png");

    Texture[] shootingImages;

    Texture[] runningImages;

    int currentRunningImage;
    final int MAX_RUNNING_IMAGE = 9;
    int timeUntilNextRunningImage;
    final int MAX_TIME_UNTIL_NEXT_RUNNING_IMAGE = 4;


    int health = 30;

    boolean destroyed = false;

    boolean canJump = false;

    Random random = new Random();

    boolean facingRight = false;

    boolean onEdge = false; // var tells if enemy is on edge, if it is it will not move forward but continue facing towards the player

    boolean isShooting = false;
    final int maxTimeUntilShoot = 300;
    int timeUntilShoot = maxTimeUntilShoot; // counts down when isShooting = false when 0 isShooting = true;
    final int maxShootTime = 50;
    int shootTime = maxShootTime; // when isShooting goes down to 0 then shoots then isnt shooting anymore


    boolean turnRectCollided;
    Rectangle turnAroundRect = new Rectangle(0, 0, 25, 25); // rectangle that is 1 robot width to the direction the robot is moving and 1 robot width down
    //if it is not intersecting any blocks then the enemy will fall off edge soon so turn around

    final int bombSpeed = 7;

    public FetyrBomber(int x, int y) {
        this.x = x;
        this.y = y;
        this.rect.x = this.x;
        this.rect.y = this.y;
        this.width = 24;
        this.height = 36;
        this.currentRunningImage = 0;
        shootingImages = new Texture[]{
                image1, image1, image2, image2, image3, image3,  image4, image4,  image5, image5, image5, image5, image6, image7, image8
        };

        runningImages = new Texture[]{
                runImage1, runImage2, runImage3, runImage4, runImage5, runImage6,  runImage7, runImage8,  runImage9, runImage10
        };
    }

    public void update(Array<Enemy> enemies, Array<EnemyBullet> enemyBullets, Array<PlayerBullet> bullets, Array<Item> items, Array<BaseTile> baseTiles, Array<Particle> particle1s, int playerX, int playerY) {
        xDist = (int) Math.abs(playerX - this.x);
        yDist = (int) Math.abs(playerY - this.y);
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

            if (velX != 0) {
                timeUntilNextRunningImage--;
                if (timeUntilNextRunningImage <= 0) {
                    timeUntilNextRunningImage = MAX_TIME_UNTIL_NEXT_RUNNING_IMAGE;
                    currentRunningImage++;
                    if (currentRunningImage > MAX_RUNNING_IMAGE) {
                        currentRunningImage = 0;
                    }
                }
            } else {
                currentRunningImage = 0;
            }

            if (timeUntilShoot > 0) {
                timeUntilShoot--;
            } else {
                isShooting = true;
            }

        } else {
            if (shootTime > 0) {
                velX = 0;
                shootTime--;
                if (shootTime == 14) {
                    FetyrThrownBomb bomb;
                    float bombVelX = (float) Math.sqrt((Math.abs((this.x - playerX)) * 0.3f) / 2);

                    if (facingRight) {
                        bomb = new FetyrThrownBomb((int) this.x, (int) this.y, bombVelX, bombVelX);
                    } else {
                        bomb = new FetyrThrownBomb((int) this.x, (int) this.y, -bombVelX, bombVelX);
                    }
                    enemyBullets.add(bomb);
                }
            } else {
                shootTime = maxShootTime;
                timeUntilShoot = maxTimeUntilShoot;
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

        if (isShooting) {
            int currentShootingImage = 14 - (int) Math.min(Math.floor(((float) shootTime / (float) maxShootTime) * 14), 14);
            batch.draw(shootingImages[currentShootingImage], x, y, width, height, 0, 0, 12, 18, !facingRight, false);
        } else if (velX != 0) {
            batch.draw(runningImages[currentRunningImage], x,y,width,height,0,0,12,18,!facingRight, false);
        } else {
            batch.draw(image1, x, y, width, height, 0, 0, 12, 18, !facingRight, false);
        }
    }
}
