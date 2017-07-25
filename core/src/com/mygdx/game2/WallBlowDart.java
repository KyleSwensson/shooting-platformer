package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Created by kyles on 2/20/2017.
 */
public class WallBlowDart extends EnemyBullet{
    Texture imageHorizontal = new Texture("SPA/Traps/Blowdart/boltHorizontal.png");
    Texture imageVertical = new Texture("SPA/Traps/Blowdart/boltVertical.png");

    //this defines the direction the bolt travels in, 1 is to the right, 2 is to the left, 3 is downwards, and 4 is upwards
    int direction;

    int aliveTime = 0;
    int maxAliveTime = 80; // destroy after has been alive for 80 frames

    static int timesMovedPerFrame = 5;
    int bulletSpeed = 12;

    public WallBlowDart(int x, int y, int direction) {
        System.out.println(direction);
        if (direction < 1 || direction > 4) {
            throw new RuntimeException("direction must be between 1 and 4");
        }
        enemyBullet = true;
        this.direction = direction;
        if (direction == 1 || direction == 2) {
            width = 13;
            height = 6;
        } else {
            width = 6;
            height = 13;
        }
        this.x = x;
        this.y = y;
        switch(direction) {
            case 1:
                this.velX = bulletSpeed;
                this.velY = 0;
                break;
            case 2:
                this.velX = -bulletSpeed;
                this.velY = 0;
                break;
            case 3:
                this.velX = 0;
                this.velY = -bulletSpeed;
                break;
            case 4:
                this.velX = 0;
                this.velY = bulletSpeed;
                break;
        }
    }

    public Rectangle getRect() {
        return rect;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public boolean getIsDangerous() {
        return this.isDangerous;
    }

    public boolean getDestroyOnHit() {
        return this.destroyOnHit;
    }

    public void update(Array<BaseTile> baseTiles, Rectangle playerRect, Array<Particle> particles, Array<EnemyBullet> enemyBullets) {
        for (int i = 0; i < timesMovedPerFrame; i++) {
            if (!destroyed) {
                if (direction == 1 || direction == 2) {
                    WallBlowDartBoltTrail trail = new WallBlowDartBoltTrail((int) x, (int) y, true);
                    GdxShooter2.mgEnemyBullets.add(trail);
                } else {
                    WallBlowDartBoltTrail trail = new WallBlowDartBoltTrail((int) x, (int) y, false);
                    GdxShooter2.mgEnemyBullets.add(trail);
                }
                super.update(baseTiles, playerRect, particles, enemyBullets);
            }
        }

        aliveTime ++;
        if (aliveTime > maxAliveTime) {
            destroyed = true;
        }



    }

    public void draw(SpriteBatch batch)
    {
        if (direction == 1 || direction == 2) {
            batch.draw(imageHorizontal, x, y, width, height);
        } else {
            batch.draw(imageVertical, x, y, width, height);
        }
    }
}
