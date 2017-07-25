package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Created by kyle on 10/11/2015.
 */
public class SBSwordDash extends EnemyBullet {

    Texture image = new Texture("bossCrystalAttackShard.png");
    Texture[] imagesHorizontal = new Texture[12];
    Texture ima1 = new Texture("SPA/Enemy/Boss/Swordsman/Dash/Horizontal/1.png");
    Texture ima2 = new Texture("SPA/Enemy/Boss/Swordsman/Dash/Horizontal/2.png");
    Texture ima3 = new Texture("SPA/Enemy/Boss/Swordsman/Dash/Horizontal/3.png");
    Texture ima4 = new Texture("SPA/Enemy/Boss/Swordsman/Dash/Horizontal/4.png");
    Texture ima5 = new Texture("SPA/Enemy/Boss/Swordsman/Dash/Horizontal/5.png");
    Texture ima6 = new Texture("SPA/Enemy/Boss/Swordsman/Dash/Horizontal/6.png");
    Texture ima7 = new Texture("SPA/Enemy/Boss/Swordsman/Dash/Horizontal/7.png");
    Texture ima8 = new Texture("SPA/Enemy/Boss/Swordsman/Dash/Horizontal/8.png");
    Texture ima9 = new Texture("SPA/Enemy/Boss/Swordsman/Dash/Horizontal/9.png");
    Texture ima10 = new Texture("SPA/Enemy/Boss/Swordsman/Dash/Horizontal/10.png");
    Texture ima11 = new Texture("SPA/Enemy/Boss/Swordsman/Dash/Horizontal/11.png");
    Texture ima12 = new Texture("SPA/Enemy/Boss/Swordsman/Dash/Horizontal/12.png");

    int currImage;
    int timeToNextImage;
    int maxTimeToNextImage = 2;

    int aliveTime = 0;
    int maxAliveTime = 45; // destroy after has been alive for 80 frames

    int drawWidth;
    int drawHeight;
    int drawX;
    int drawY;

    SwordsmanBoss parentBoss;

    boolean isTravellingVertically;
    boolean isGoingLeft;

    float speed;

    public SBSwordDash(int x, int y, boolean isGoingLeft, boolean isTravellingVertically, SwordsmanBoss parentBoss) {
        enemyBullet = true;
        width = 20;
        height = 20;
        speed = 14;
        this.x = x;
        this.y = y;

        this.destroyOnHit = false;

        this.drawWidth = 240;
        this.drawHeight = 36;
        if (isGoingLeft) {
            this.drawX = x - this.drawWidth;
            this.drawY = y;
        } else {
            this.drawX = x;
            this.drawY = y;
        }
        this.isGoingLeft = isGoingLeft;
        this.parentBoss = parentBoss;
        this.isTravellingVertically = isTravellingVertically;

        imagesHorizontal[0] = ima1;
        imagesHorizontal[1] = ima2;
        imagesHorizontal[2] = ima3;
        imagesHorizontal[3] = ima4;
        imagesHorizontal[4] = ima5;
        imagesHorizontal[5] = ima6;
        imagesHorizontal[6] = ima7;
        imagesHorizontal[7] = ima8;
        imagesHorizontal[8] = ima9;
        imagesHorizontal[9] = ima10;
        imagesHorizontal[10] = ima11;
        imagesHorizontal[11] = ima12;

        currImage = 0;
        timeToNextImage = maxTimeToNextImage;


        if (isTravellingVertically) {
            velY = 20;
            velX = 0;
            maxAliveTime = 12;
        } else {
            velX = 0;
            velY = 0;
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
        this.x += velX;
        this.y += velY;
        timeActive++;
        if (timeActive > maxTimeActive) destroyed = true;


        rect.x = x;
        rect.y = y;
        rect.width = width;
        rect.height = height;



        for (BaseTile tile : baseTiles) {
            if (tile.isActive) {
                if (rect.overlaps(tile.rect)) {
                    setDestroyed(true);
                }
            }
        }


        timeToNextImage--;
        if (timeToNextImage <= 0) {
            timeToNextImage = maxTimeToNextImage;
            currImage++;
            if (currImage > 11) {
                setDestroyed(true);
                currImage = 11;
            }
        }


        aliveTime ++;
        if (aliveTime > maxAliveTime) {
            setDestroyed(true);
        }
    }

    public void draw(SpriteBatch batch) {
        if (isTravellingVertically) {
            //batch.draw(imageVertical,x,y,width,height);
        } else if (isGoingLeft) {
            batch.draw(imagesHorizontal[currImage],drawX,drawY,drawWidth,drawHeight,0,0,120,36,true, false);
        } else {
            batch.draw(imagesHorizontal[currImage],drawX,drawY,drawWidth,drawHeight,0,0,120,36,false, false);
        }
    }
}
