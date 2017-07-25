package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Created by kyle on 10/11/2015.
 */
public class SBAngledSlash extends EnemyBullet {

    Texture[] images = new Texture[6];
    Texture image1  = new Texture("SPA/Enemy/Boss/Swordsman/Attack/Wide/1.png");
    Texture image2  = new Texture("SPA/Enemy/Boss/Swordsman/Attack/Wide/2.png");
    Texture image3  = new Texture("SPA/Enemy/Boss/Swordsman/Attack/Wide/3.png");
    Texture image4  = new Texture("SPA/Enemy/Boss/Swordsman/Attack/Wide/4.png");
    Texture image5  = new Texture("SPA/Enemy/Boss/Swordsman/Attack/Wide/5.png");
    Texture image6  = new Texture("SPA/Enemy/Boss/Swordsman/Attack/Wide/6.png");
    int currTexture;


    int timeTillNextTexture;
    int maxTimeTillNextTexture = 5;

    int aliveTime = 0;
    int maxAliveTime = 80; // destroy after has been alive for 80 frames

    float playerDX = 0;
    float playerDY = 0;

    float playerDist = 0;

    float slopeOfShot;

    float angle;

    float drawAngle;

    float speed;

    public SBAngledSlash(int x, int y, float angle) {
        enemyBullet = true;
        width = 20;
        height = 48;
        velY = 2;
        speed = 14;
        this.x = x;
        this.y = y;


        velX = speed*(float)Math.sin(angle);
        velY = speed * (float) Math.cos(angle);
        this.angle = angle;
        this.drawAngle = (float)Math.atan2(velY, velX);



        images[0] = image1;
        images[1] = image2;
        images[2] = image3;
        images[3] = image4;
        images[4] = image5;
        images[5] = image6;

        timeTillNextTexture = maxTimeTillNextTexture;



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

        timeTillNextTexture--;
        if (timeTillNextTexture <= 0) {
            currTexture ++;
            timeTillNextTexture = maxTimeTillNextTexture;
            if (currTexture > 5) {
                setDestroyed(true);
                currTexture = 5;
            }
        }




    }

    public void draw(SpriteBatch batch) {

        batch.draw(images[currTexture],x,y,0,0,width,height,1,1,drawAngle*57.2958f,0,0,10,24,false,false);

    }
}
