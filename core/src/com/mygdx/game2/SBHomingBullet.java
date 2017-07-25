package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Created by kyle on 10/11/2015.
 */
public class SBHomingBullet extends EnemyBullet {

    Texture image = new Texture("enemyBigBullet.png");

    int aliveTime = 0;
    int targetTime = 30; // if alive time is less than this velY = +2;
    int maxAliveTime = 80; // destroy after has been alive for 80 frames

    float playerDX = 0;
    float playerDY = 0;

    float playerDist = 0;

    public SBHomingBullet(int x, int y, int velX, int velY) {
        enemyBullet = true;
        width = 16;
        height = 16;
        this.x = x;
        this.y = y;
        this.velX = velX;
        this.velY = velY;
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
        super.update(baseTiles, playerRect, particles, enemyBullets);

        aliveTime ++;
        if (aliveTime > maxAliveTime) {
            destroyed = true;
        }
        if (aliveTime < targetTime) {

            velY -= .025;

            if (velX > 0) {
                velX -= 0.025;
            } else if (velX < 0) {
                velX += 0.025;
            }

            if (velY > 0) {
                velY -= 0.025;
            } else if (velY < 0) {
                velY += 0.025;
            }
        } else {
            playerDX = Math.abs(rect.x - playerRect.x);
            playerDY = Math.abs(rect.y - playerRect.y);


            if (playerRect.x > this.x) {
                velX += .5 * (playerDX / (playerDX + playerDY));
            } else {
                velX -= .5 * (playerDX / (playerDX + playerDY));
            }
            if (playerRect.y > this.y) {
                velY += .5 * (playerDY / (playerDX + playerDY));
            } else {
                velY -= .5 * (playerDY / (playerDX + playerDY));
            }
        }



    }

    public void draw(SpriteBatch batch) {
        batch.draw(image,x,y,width,height);
    }
}
