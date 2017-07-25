package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Created by kyle on 10/11/2015.
 */
public class FetyrThrownBomb extends EnemyBullet {

    Texture image = new Texture("SPA/Enemy/Fetyr/Bomber/Bomb/1.png");


    int aliveTime = 0;
    int maxAliveTime = 80; // destroy after has been alive for 80 frames

    public FetyrThrownBomb(int x, int y, float velX, float velY) {
        destroyed = false;
        enemyBullet = true;
        width = 10;
        height = 10;
        this.velX = velX;
        this.velY = velY;
        this.x = x;
        this.y = y;
    }

    public Rectangle getRect() {
        return rect;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
        if (destroyed) {
            System.out.println("creaiting explositon");
            GdxShooter2.mgEnemyBullets.add(new EnemyExplosion(this.x, this.y));
        }
    }

    public boolean getIsDangerous() {
        return this.isDangerous;
    }

    public boolean getDestroyOnHit() {
        return this.destroyOnHit;
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

        aliveTime ++;
        if (aliveTime > maxAliveTime) {
            setDestroyed(true);
        }

        velY -= 0.3f;
    }

    public void draw(SpriteBatch batch)
    {
        batch.draw(image,x,y,width,height);
    }
}
