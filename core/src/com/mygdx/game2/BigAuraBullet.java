package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Created by kyle on 10/11/2015.
 */
public class BigAuraBullet extends EnemyBullet {

    Texture image1 = new Texture("SPA/Enemy/shootingGroundMan/1.png");
    Texture image2 = new Texture("SPA/Enemy/shootingGroundMan/2.png");
    Texture image3 = new Texture("SPA/Enemy/shootingGroundMan/3.png");
    Texture[] chargingImages;
    Texture targetedImage = new Texture("SPA/Enemy/shootingGroundMan/targeted/1.png");

    int currFrame;
    int timeToNextFrame;
    int maxTimeToNextFrame = 7;

    int aliveTime = 0;
    int targetTime = 30; // if alive time is less than this velY = +2;
    int maxAliveTime = 80; // destroy after has been alive for 80 frames

    float playerDX = 0;
    float playerDY = 0;

    float playerDist = 0;

    public BigAuraBullet() {
        enemyBullet = true;
        width = 16;
        height = 16;
        velY = 2;
        currFrame = 0;
        timeToNextFrame = maxTimeToNextFrame;
        chargingImages = new Texture[]{image1,image2,image3,image2};
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
            velX = 0;
            velY -= .025;
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


        timeToNextFrame--;
        if (timeToNextFrame <= 0) {
            timeToNextFrame = maxTimeToNextFrame;
            currFrame++;
            if (currFrame > 3) {
                currFrame -= 4;
            }
        }



    }

    public void draw(SpriteBatch batch)
    {
        if (aliveTime < targetTime) {
            batch.draw(chargingImages[currFrame], x, y, width, height);
        } else {
            batch.draw(targetedImage,x,y,width,height,0,0,11,11,(velX > 0), false);
        }
    }
}
