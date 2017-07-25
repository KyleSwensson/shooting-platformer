package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Created by kyle on 10/11/2015.
 */
public class WallBlowDartBoltTrail extends EnemyBullet {

    Texture imageHorizontal = new Texture("SPA/Traps/Blowdart/boltHorizontal.png");
    Texture imageVertical = new Texture("SPA/Traps/Blowdart/boltVertical.png");
    boolean isHorizontal;

    int aliveTime = 0;
    int maxAliveTime = 7; // destroy after has been alive for 80 frames

    public WallBlowDartBoltTrail(int x, int y, boolean isHorizontal) {
        enemyBullet = true;
        this.isHorizontal = isHorizontal;
        if (isHorizontal) {
            width = 13;
            height = 6;
        } else {
            width = 6;
            height = 13;
        }
        this.x = x;
        this.y = y;
        this.velY = 0;
        this.velX = 0;
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
        aliveTime ++;
        if (aliveTime > maxAliveTime) {
            destroyed = true;
        }



    }

    public void draw(SpriteBatch batch)
    {
        if (isHorizontal) {
            batch.draw(imageHorizontal, x, y, width, height);
        } else {
            batch.draw(imageVertical, x, y, width, height);

        }
    }
}
