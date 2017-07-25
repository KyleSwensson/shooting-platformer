package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

/**
 * Created by kyle on 10/11/2015.
 */
public class CrystalLaserSpeck extends EnemyBullet {

    Texture image = new Texture("SPA/Player/player_dust.png");
    Texture redImage = new Texture("SPA/Enemy/Boss/Diamound/laserRed.png");

    int aliveTime = 0;
    int maxAliveTime = 240; // destroy after has been alive for 80 frames
    Random random;
    float angle;

    boolean isDangerous;
    boolean destroyOnHit;

    int timeUntilDangerous;
    int maxTimeUntilDangerous = 75;


    public CrystalLaserSpeck(int x, int y, float angle) {
        enemyBullet = true;
        width = 8;
        height = 8;
        velY = 0;
        velX = 0;
        this.x = x;
        this.y = y;
        this.angle = angle;
        random = new Random();
        isDangerous = false;
        destroyOnHit = false;

        timeUntilDangerous = maxTimeUntilDangerous;
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

        timeUntilDangerous--;
        if (timeUntilDangerous <= 0) {
            isDangerous = true;
        }
        if (isDangerous) {
            int spawnSeed = random.nextInt(80);
            if (spawnSeed == 1) {
                LaserOutwardParticle bull = new LaserOutwardParticle(this.x, this.y, this.angle, true);
                enemyBullets.add(bull);
            } else if (spawnSeed == 2) {
                LaserOutwardParticle bull = new LaserOutwardParticle(this.x, this.y, this.angle, false);
                enemyBullets.add(bull);
            }
        }


        if (aliveTime > 120) {
            System.out.println("dsfasdf");


            System.out.println(angle);

        }

        if (aliveTime > maxAliveTime) {
            destroyed = true;
        }




    }

    public void draw(SpriteBatch batch) {

        if (!isDangerous) {
            batch.draw(redImage, x - (width / 2), y - (height / 2), width, height);
        } else {
            batch.draw(image, x - (width / 2), y - (height / 2), width, height);
        }
        //batch.draw(image,x-(width/2),y-(height/2),0,0,width,height,1,1,angle,0,0,4,4,false,false);
    }
}
