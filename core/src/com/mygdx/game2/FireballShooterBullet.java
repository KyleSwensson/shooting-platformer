package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Created by kyle on 10/11/2015.
 */
public class FireballShooterBullet extends EnemyBullet {

    Texture image1 = new Texture("SPA/Traps/FireballShooter/bullet1.png");
    Texture image2 = new Texture("SPA/Traps/FireballShooter/bullet2.png");
    Texture image3 = new Texture("SPA/Traps/FireballShooter/bullet3.png");

    Texture[] images;


    int currImage;
    final int maxImage = 3;

    int timeUntilNextImage;
    final int maxTimeUntilNextImage = 5;



    int aliveTime = 0;
    int maxAliveTime = 200; // destroy after has been alive for 80 frames

    public FireballShooterBullet(int x, int y, float velX, float velY) {
        enemyBullet = true;
        width = 22;
        height = 22;
        this.velX = velX;
        this.velY = velY;
        this.x = x;
        this.y = y;

        images = new Texture[]{
                image1,image2,image3
        };
        timeUntilNextImage = maxTimeUntilNextImage;
        currImage = 1;
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

        velY -= 0.3f;


        timeUntilNextImage--;
        if (timeUntilNextImage <= 0) {
            timeUntilNextImage = maxTimeUntilNextImage;
            currImage++;
            if (currImage > maxImage) {
                currImage -= maxImage;
            }
        }

    }

    public void draw(SpriteBatch batch)
    {
        batch.draw(images[currImage - 1],x,y,width,height);
    }
}
