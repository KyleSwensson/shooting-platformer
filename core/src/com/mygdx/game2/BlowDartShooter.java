package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Created by kyles on 12/3/2016.
 */
public class BlowDartShooter extends Enemy {

    Texture imageLeft = new Texture("SPA/Traps/Blowdart/launcherLeft.png");
    Texture imageRight = new Texture("SPA/Traps/Blowdart/launcherRight.png");
    Texture imageTop = new Texture("SPA/Traps/Blowdart/launcherTop.png");
    Texture imageBottom = new Texture("SPA/Traps/Blowdart/launcherBottom.png");

    static final int drawDist = 500; // max distance from player that this should still be drawn and updated

    boolean isActive;

    //orientation defines the wall the launcher is placed on. 1 = left, 2 = right, 3 = top, 4 = bottom
    int orientation;

    boolean isAttacking;
    int attackTime;
    static final int maxAttackTime = 2;
    int timeUntilAttack;
    static final int maxTimeUntilAttack = 160;

    public static final int DIFFICULTY_POINTS = 40;

    public BlowDartShooter(int x, int y, int orientation) {
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
                    if (orientation == 1 || orientation == 2) {
                        WallBlowDart dart = new WallBlowDart((int) x + 6, (int) y + (height / 2) - 3, this.orientation);
                        GdxShooter2.mgEnemyBullets.add(dart);
                    } else {
                        WallBlowDart dart = new WallBlowDart((int) x + (width / 2) - 3, (int) y + 6, this.orientation);
                        GdxShooter2.mgEnemyBullets.add(dart);
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
                batch.draw(imageLeft, x, y, width, height);
                break;
            case 2:
                batch.draw(imageRight, x, y, width, height);
                break;
            case 3:
                batch.draw(imageTop, x, y, width, height);
                break;
            case 4:
                batch.draw(imageBottom, x, y, width, height);
                break;
        }
    }
}
