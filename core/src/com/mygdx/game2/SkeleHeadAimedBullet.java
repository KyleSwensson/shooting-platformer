package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Created by kyle on 10/11/2015.
 */
public class SkeleHeadAimedBullet extends EnemyBullet {

    Texture image1 = new Texture("SPA/Enemy/skelehead/Fireball/1.png");
    Texture image2 = new Texture("SPA/Enemy/skelehead/Fireball/2.png");
    Texture image3 = new Texture("SPA/Enemy/skelehead/Fireball/3.png");
    Texture[] images;

    int aliveTime = 0;
    int maxAliveTime = 200; // destroy after has been alive for 80 frames

    float angle;

    float speed;

    int currFrame;
    int timeUntilNextFrame;
    int maxTimeUntilNextFrame = 7;

    public SkeleHeadAimedBullet(int x, int y, int playerX, int playerY) {
        enemyBullet = true;
        width = 22;
        height = 22;
        speed = 3.5f;
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

        images = new Texture[4];
        images[0] = image1;
        images[1] = image2;
        images[2] = image3;
        images[3] = image2;
        currFrame = 0;
        timeUntilNextFrame = maxTimeUntilNextFrame;



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

        timeUntilNextFrame--;
        if (timeUntilNextFrame <= 0) {
            timeUntilNextFrame = maxTimeUntilNextFrame;
            currFrame++;
            if (currFrame > 3) {
                currFrame -= 4;
            }
        }

    }

    public void draw(SpriteBatch batch) {

        batch.draw(images[currFrame],x,y,0,0,width,height,1,1,angle,0,0,11,11,false,false);
    }
}
