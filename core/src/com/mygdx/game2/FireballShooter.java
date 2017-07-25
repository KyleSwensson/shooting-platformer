package com.mygdx.game2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Created by kyles on 12/3/2016.
 */
public class FireballShooter extends Enemy {

    Texture image = new Texture("SPA/Traps/FireballShooter/launcherBottom.png");

    public static final int DIFFICULTY_POINTS = 40;

    static final int drawDist = 500; // max distance from player that this should still be drawn and updated

    boolean isActive;

    //orientation defines the wall the launcher is placed on. 1 = left, 2 = right, 3 = top, 4 = bottom
    int orientation;

    boolean isAttacking;
    int attackTime;
    static final int maxAttackTime = 2;
    int timeUntilAttack;
    static final int maxTimeUntilAttack = 160;



    public FireballShooter(int x, int y, int orientation) {
        if (orientation < 1 || orientation > 4) {
            throw new RuntimeException("orientation must be between 1 and 4");
        }
        this.x = x;
        this.y = y;
        this.width = 32;
        this.height = 32;
        this.health = 10;
        attackTime = maxAttackTime;
        timeUntilAttack = maxTimeUntilAttack;
        this.rect = new Rectangle(this.x, this.y, this.width, this.height);
        this.orientation = orientation;
        isActive = false;
        isDangerous = false;
        isHittable = false;
    }

    public void changeHealth(int amount) {
    }

    public Rectangle getRect() {
        return this.rect;
    }

    public boolean getDestroyed() {
        return this.destroyed;
    }

    public void update(Array<Enemy> enemies, Array<EnemyBullet> enemyBullets, Array<PlayerBullet> bullets, Array<Item> items, Array<BaseTile> baseTiles, Array<Particle> particle1s, int playerX, int playerY) {
        xDist = Math.abs(playerX - this.x);
        yDist = Math.abs(playerY - this.y);
        if (Math.sqrt((xDist * xDist) + (yDist * yDist)) > drawDist) isActive = false;
        else isActive = true;

        if (isActive) {
            if (isAttacking) {
                attackTime++;
                if (attackTime == 1) {
                    FireballShooterBullet bullet;
                    switch (orientation) {
                        case 1:
                            bullet = new FireballShooterBullet((int)x + 6, (int) y + (height / 2) - 3, 12, 0);
                            GdxShooter2.mgEnemyBullets.add(bullet);
                            break;
                        case 2:
                            bullet = new FireballShooterBullet((int)x + 6, (int) y + (height / 2) - 3, -12, 0);
                            GdxShooter2.mgEnemyBullets.add(bullet);
                            break;
                        case 3:
                            bullet = new FireballShooterBullet((int)x + 6, (int) y + (height / 2) - 3, 0, -4);
                            GdxShooter2.mgEnemyBullets.add(bullet);
                            break;
                        case 4:
                            bullet = new FireballShooterBullet((int)x + 6, (int) y + (height / 2) - 3, 0, 12);
                            GdxShooter2.mgEnemyBullets.add(bullet);
                            break;

                    }
                }

                if (attackTime > maxAttackTime) {
                    isAttacking = false;
                    timeUntilAttack = maxTimeUntilAttack;
                }
            } else {
                timeUntilAttack--;
                if (timeUntilAttack < 0) {
                    isAttacking = true;
                    attackTime = 0;
                }
            }
        }
    }


    public void draw(SpriteBatch batch) {
        switch (orientation) {
            case 1:
                batch.draw(image, x,y,width/2, height/2, width, height, 1,1,270f,0,0,15,15,false,false);
                break;
            case 2:
                batch.draw(image, x,y,width/2, height/2, width, height, 1,1,90f,0,0,15,15,false,false);
                break;
            case 3:
                batch.draw(image, x,y,width/2, height/2, width, height, 1,1,180f,0,0,15,15,false,false);
                break;
            case 4:
                batch.draw(image, x,y,width/2, height/2, width, height, 1,1,0,0,0,15,15,false,false);
                break;
        }
    }
}
