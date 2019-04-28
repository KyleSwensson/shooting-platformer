package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

/**
 * Created by kyle on 10/11/2015.
 */
public class SkullBoss extends Enemy implements Boss {

    int xDist; // distance from block to character x plane
    int yDist; // distance from block to character y plane
    int drawDist = 500; // max distance from player that this should still be drawn and updated
    boolean isActive; // boolean to tell whether it is too far away and should be drawn

    Texture image = new Texture("SPA/Enemy/skelehead/1.png");

    float x;
    float y;
    float velX;
    float velY;

    int health = 30;

    boolean destroyed = false;

    Random random = new Random();

    boolean facingRight = false;

    boolean isShooting = false;
    int timeUntilShoot = 30; // counts down when isShooting = false when 0 isShooting = true;
    final int maxShootTime = 60;
    int shootTime = maxShootTime; // when isShooting goes down to 0 then shoots then isnt shooting anymore
    int currAttackFrame = 0; // frame of attack animation skeleton is on

    int activeAttackType = 0;
    boolean isAttacking = false;
    boolean isPreparingForAttack = false;
    int attackTime = 0;
    int attackPrepareTime = 0;
    final static int[] attackTimes = new int[]{60, 60, 60};
    final static int[] attackPrepareTimes = new int[]{60, 60, 60};
    public static final int MAX_HEALTH = 350;


    public SkullBoss(int x, int y) {
        health = MAX_HEALTH;
        this.x = x;
        this.y = y;
        this.width = 56;
        this.height = 56;
        enemyType = "SkullBoss";

    }

    public void update(Array<Enemy> enemies, Array<EnemyBullet> enemyBullets, Array<PlayerBullet> bullets, Array<Item> items, Array<BaseTile> baseTiles, Array<Particle> particle1s, int playerX, int playerY) {
        xDist = (int) Math.abs(playerX - this.x);
        yDist = (int) Math.abs(playerY - this.y);
        if (Math.sqrt((xDist * xDist) + (yDist * yDist)) > drawDist) isActive = false;
        else isActive = true;

        if (isActive) {

            if (!isPreparingForAttack && !isAttacking) {
                moveEnemy(playerX, playerY);
            }

            if (isPreparingForAttack) {
                attackPrepareTime--;
                if (attackPrepareTime <= 0) {
                    isPreparingForAttack = false;
                    isAttacking = true;
                    attackTime = attackTimes[activeAttackType - 1];
                }
            }

            if (isAttacking) {
                switch (activeAttackType) {
                    case 1:
                        handleAttack1(playerX, playerY);
                        break;
                    case 2:
                        handleAttack2();
                        break;
                    case 3:
                        handleAttack3();
                        break;
                }

                attackTime--;
                if (attackTime <= 0) {
                    isAttacking = false;
                    activeAttackType = 0;
                }
            }

            if (health <= 0) {
                destroyEnemy(particle1s, items);

                destroyed = true;
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

    public float getHealthPercentage() {
        return ((float)health / (float)MAX_HEALTH)*100;
    }

    public Rectangle getRect() {
        return rect;
    }

    public void changeHealth(int addToHealth) {
        health += addToHealth;
    }

    private void handleAttack1(int playerX, int playerY) {
        //TODO: implement shoot 4 bullets at player
        if (attackTime == 21) {
            SkeleHeadAimedBullet bul = new SkeleHeadAimedBullet((int) this.x, (int) this.y, playerX, playerY);
            GdxShooter2.mgEnemyBullets.add(bul);
        } else if (attackTime == 11) {
            SkeleHeadAimedBullet bul = new SkeleHeadAimedBullet((int) this.x, (int) this.y, playerX, playerY);
            GdxShooter2.mgEnemyBullets.add(bul);
        } else if (attackTime == 1) {
            SkeleHeadAimedBullet bul = new SkeleHeadAimedBullet((int) this.x, (int) this.y, playerX, playerY);
            GdxShooter2.mgEnemyBullets.add(bul);
        }
    }

    private void handleAttack2() {
        //TODO: implement shoot arc of bullets at player
    }

    private void handleAttack3() {
        //TODO: implement beams coming out of ground
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

    private void moveEnemy(int playerX, int playerY) {

        int newPlayerX = playerX;
        int newPlayerY = playerY + 30;

        if (newPlayerX > this.x) {
            facingRight = true;
            velX += 0.01;
        } else {
            facingRight = false;
            velX -= 0.01;
        }

        if (newPlayerY > this.y) {
            velY += 0.01;
        } else {
            velY -= 0.01;
        }


        if (velX > 1.5) velX = 1.5f;
        else if (velX < -1.5) velX = -1.5f;

        if (velY > 1.5) velY = 1.5f;
        else if (velY < -1.5) velY = -1.5f;


        if (timeUntilShoot > 0) {
            timeUntilShoot--;
        } else {
            timeUntilShoot = 400;
            isPreparingForAttack = true;
            activeAttackType = 1;
            attackPrepareTime = attackPrepareTimes[activeAttackType-1];
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
        batch.draw(image, x, y, width, height, 0, 0, 11, 11, !facingRight, false);
    }
}
