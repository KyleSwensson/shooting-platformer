package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

/**
 * Created by kyle on 10/11/2015.
 */
public class Ghosto extends Enemy {

    public static final int DIFFICULTY_POINTS = 40;

    int xDist; // distance from block to character x plane
    int yDist; // distance from block to character y plane
    int drawDist = 500; // max distance from player that this should still be drawn and updated
    boolean isActive; // boolean to tell whether it is too far away and should be drawn

    Texture image = new Texture("SPA/Enemy/ghosto/1.png");

    Texture[] attackImages;

    float x;
    float y;
    float velX;
    float velY;

    int health = 50;

    boolean destroyed = false;

    Random random = new Random();

    boolean facingRight = false;

    boolean isAttacking = false;
    int maxTimeUntilAttack = 200;
    int timeUntilAttack = maxTimeUntilAttack; // counts down when isShooting = false when 0 isShooting = true;
    final int maxAttackTime = 30;
    int attackTime = maxAttackTime; // when isShooting goes down to 0 then shoots then isnt shooting anymore
    int dashNumber = 0;
    int maxDashNumber = 3;
    int currAttackFrame = 0; // frame of attack animation ghost is on
    float dashVelX;
    float dashVelY;

    float dashSpeed = 4;


    final int maxTimeUntilSpawnTrail = 12;
    final int maxTimeUntilSpawnTrailAttacking = 4;
    int timeUntilSpawnTrail = maxTimeUntilSpawnTrail;

    public Ghosto(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = 24;
        this.height = 18;

    }

    public void update(Array<Enemy> enemies, Array<EnemyBullet> enemyBullets, Array<PlayerBullet> bullets,Array<Item> items, Array<BaseTile> baseTiles, Array<Particle> particle1s, int playerX, int playerY) {
        xDist = (int)Math.abs(playerX - this.x);
        yDist = (int)Math.abs(playerY - this.y);
        if (Math.sqrt((xDist * xDist) + (yDist * yDist)) > drawDist) isActive = false;
        else isActive = true;

        if (isActive) {

            moveEnemy(playerX, playerY,  enemyBullets);


            if (health <= 0) {
                destroyEnemy(particle1s, items);

                destroyed = true;
            }

            rect.x = x;
            rect.y = y;
            rect.width = width;
            rect.height = height;


            checkTilesHit(baseTiles);


            timeUntilSpawnTrail--;
            if (timeUntilSpawnTrail <= 0) {
                if (!isAttacking) {
                    timeUntilSpawnTrail = maxTimeUntilSpawnTrail;
                } else {
                    timeUntilSpawnTrail = maxTimeUntilSpawnTrailAttacking;
                }
                GhostoTrail trail = new GhostoTrail(this.x, this.y, !facingRight);
                GdxShooter2.activeAnims.add(trail);
            }
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

                    y = (int) rect.y;
                    x = (int) rect.x;
                }

            }
        }
    }

    private void moveEnemy(int playerX, int playerY, Array<EnemyBullet> enemyBullets) {



        if (!isAttacking) {
            if (playerX > this.x) {
                facingRight = true;
                velX += 0.01;
            } else {
                facingRight = false;
                velX -= 0.01;
            }

            if (playerY > this.y) {
                velY += 0.01;
            } else {
                velY -= 0.01;
            }


            if (velX > 1.5) velX = 1.5f;
            else if (velX < -1.5) velX = -1.5f;

            if (velY > 1.5) velY = 1.5f;
            else if (velY < -1.5) velY = -1.5f;


            if (timeUntilAttack > 0) {
                timeUntilAttack--;
            } else {
                isAttacking = true;
                attackTime = 0;
                dashNumber = 0;
                timeUntilAttack = maxTimeUntilAttack;
            }

            x += velX;

            y += velY;

        } else {
            if (attackTime > 0) {
                attackTime--;
                moveEnemyDashing();
            } else {
                setDashVelocities(playerX, playerY);
            }


        }
    }

    private void moveEnemyDashing() {
        x += dashVelX;
        y += dashVelY;
    }

    private void setDashVelocities(int playerX, int playerY) {
        dashNumber++;
        attackTime = maxAttackTime;
        if (dashNumber>maxDashNumber) {
            isAttacking = false;
            timeUntilAttack = maxTimeUntilAttack;
        } else {
            int distX = playerX - (int) this.x;
            int distY = playerY - (int) this.y;
            float distH = (float) Math.sqrt((distX * distX) + (distY * distY));

            float angle = (float) Math.asin(distX / distH);

            dashVelX = dashSpeed * (float) Math.sin(angle);
            if (playerY > this.y) {
                dashVelY = dashSpeed * (float) Math.cos(angle);
            } else {
                dashVelY = -dashSpeed * (float) Math.cos(angle);
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


            batch.draw(image, x,y,width,height, 0, 0, 11, 11, !facingRight, false);


    }
}

