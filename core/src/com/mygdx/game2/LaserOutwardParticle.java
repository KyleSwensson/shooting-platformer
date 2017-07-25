package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

/**
 * Created by kyle on 10/11/2015.
 */
public class LaserOutwardParticle extends EnemyBullet {

    Texture image = new Texture("SPA/Player/player_dust.png");

    int aliveTime = 0;
    int maxAliveTime = 60; // destroy after has been alive for 80 frames
    Random random;
    float angle;
    float speed;

    boolean isDangerous;
    boolean destroyOnHit;



    public LaserOutwardParticle(float x, float y, float angle, boolean isGoingUp) {
        enemyBullet = true;
        width = 8;
        height = 8;
        speed = 3;
        if (Math.abs(angle) > 1.5 && Math.abs(angle) < 1.6) {
            velY = 2;
            velX = 0;
        } else if (angle == 0) {
            velX = 2;
            velY = 0;
        } else {
            velY = 2;
            velX = 0;
        }

        /*else if (angle > 0.7 && angle < 0.8) {
            velY = (float)Math.sqrt(2);
            velX = -(float)Math.sqrt(2);
        } else if (angle < -0.7 && angle > -0.8) {
            velY = (float)Math.sqrt(2);
            velX = -(float)Math.sqrt(2);
        }*/

        if (!isGoingUp) {
            velX *= -1;
            velY *= -1;
        }


        aliveTime = 0;
        this.x = x;
        this.y = y;
        this.angle = angle;
        random = new Random();
        isDangerous = true;
        destroyOnHit = false;

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


        batch.draw(image, x - (width / 2), y - (height / 2), width, height);
        //batch.draw(image,x-(width/2),y-(height/2),0,0,width,height,1,1,angle,0,0,4,4,false,false);
    }
}
