package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Created by kyle on 10/11/2015.
 */
public class CrystalShardBullet extends EnemyBullet {

    Texture image = new Texture("bossCrystalAttackShard.png");

    int aliveTime = 0;
    int maxAliveTime = 80; // destroy after has been alive for 80 frames

    float playerDX = 0;
    float playerDY = 0;

    float playerDist = 0;

    float slopeOfShot;

    float angle;

    float speed;

    public CrystalShardBullet(int x, int y, int playerX, int playerY) {
        enemyBullet = true;
        width = 10;
        height = 14;
        velY = 2;
        speed = 14;
        this.x = x;
        this.y = y;
        int distX = playerX - (int)this.x;
        int distY = playerY - (int)this.y;
        float distH = (float)Math.sqrt((distX * distX) + (distY * distY));

        angle = (float)Math.asin(distX / distH);

        velX = speed*(float)Math.sin(angle);
        if (playerY > this.y) {
            velY = speed * (float) Math.cos(angle);
        } else {
            velY = -speed * (float)Math.cos(angle);
        }



    }

    public boolean getIsDangerous() {
        return this.isDangerous;
    }

    public boolean getDestroyOnHit() {
        return this.destroyOnHit;
    }

    public Rectangle getRect() {
        return rect;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public void update(Array<BaseTile> baseTiles, Rectangle playerRect, Array<Particle> particles, Array<EnemyBullet> enemyBullets) {
        super.update(baseTiles, playerRect, particles, enemyBullets);

        aliveTime ++;
        if (aliveTime > maxAliveTime) {
            destroyed = true;
        }




    }

    public void draw(SpriteBatch batch) {

        batch.draw(image,x,y,0,0,width,height,1,1,angle,0,0,5,7,false,false);
    }
}
