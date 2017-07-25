package com.mygdx.game2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Created by kyle on 10/11/2015.
 */
public class FetyrCrossbowBolt extends EnemyBullet {

    Texture image = new Texture("SPA/Enemy/Fetyr/Crossbow/Bolt/1.png");
    boolean isFacingRight;

    int aliveTime = 0;
    int maxAliveTime = 80; // destroy after has been alive for 80 frames

    static int timesMovedPerFrame = 20;
    int bulletSpeed = 5;

    public FetyrCrossbowBolt(int x, int y, boolean isFacingRight) {
        enemyBullet = true;
        this.isFacingRight = isFacingRight;
        width = 22;
        height = 6;
        this.x = x;
        this.y = y;
        this.velY = 0;
        if (isFacingRight) {
            this.velX = bulletSpeed;
        } else {
            this.velX = -bulletSpeed;
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
                super.update(baseTiles, playerRect, particles, enemyBullets);
                FetyrCrossbowBoltTrail trail = new FetyrCrossbowBoltTrail((int) x, (int) y);
                GdxShooter2.mgEnemyBullets.add(trail);
            }
        }

        aliveTime ++;
        if (aliveTime > maxAliveTime) {
            destroyed = true;
        }



    }

    public void draw(SpriteBatch batch)
    {
        batch.draw(image,x,y,width,height);
    }
}
