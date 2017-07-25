package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Created by kyle on 10/11/2015.
 */
public class SBSwordSlash extends EnemyBullet {

    Texture[] images = new Texture[6];
    Texture image1  = new Texture("SPA/Enemy/Boss/Swordsman/Attack/1.png");
    Texture image2  = new Texture("SPA/Enemy/Boss/Swordsman/Attack/2.png");
    Texture image3  = new Texture("SPA/Enemy/Boss/Swordsman/Attack/3.png");
    Texture image4  = new Texture("SPA/Enemy/Boss/Swordsman/Attack/4.png");
    Texture image5  = new Texture("SPA/Enemy/Boss/Swordsman/Attack/5.png");
    Texture image6  = new Texture("SPA/Enemy/Boss/Swordsman/Attack/6.png");
    int currTexture;

    int timeTillNextTexture;
    int maxTimeTillNextTexture = 6;

    int aliveTime = 0;
    int maxAliveTime = 40; // destroy after has been alive for 80 frames


    boolean isGoingLeft;
    boolean isTravellingVertical;

    float speed;

    public SBSwordSlash(int x, int y, boolean isGoingLeft, boolean isTravellingVertical) {
        enemyBullet = true;
        width = 20;
        height = 20;
        speed = 14;
        this.x = x;
        this.y = y;
        this.isGoingLeft = isGoingLeft;
        this.isTravellingVertical = isTravellingVertical;
        currTexture = 0;
        timeTillNextTexture = maxTimeTillNextTexture;

        if (isTravellingVertical) {
            velY = -8;
            velX = 0;
        } else if (isGoingLeft) {
            velX = -10;
        } else {
            velX = 10;
        }

        images[0] = image1;
        images[1] = image2;
        images[2] = image3;
        images[3] = image4;
        images[4] = image5;
        images[5] = image6;


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
        if (timeTillNextTexture == 0) {
            timeTillNextTexture = maxTimeTillNextTexture;
            if (currTexture < 5) {
                currTexture++;
            }
        }
    }

    public void draw(SpriteBatch batch) {
        if (isTravellingVertical) {
            batch.draw(images[currTexture], x, y, 0, 0, width, height, 1, 1, 270,0,0, 17, 11, false,false);
        } else if (isGoingLeft) {
            batch.draw(images[currTexture], x, y, width, height, 0, 0, 17, 11, true, false);
        } else {
            batch.draw(images[currTexture], x, y, width, height, 0, 0, 17, 11, false, false);
        }
    }
}
