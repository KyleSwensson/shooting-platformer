package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Created by kyle on 10/7/2015.
 */
public abstract class EnemyBullet extends Bullet {

    int timeActive = 0;
    int maxTimeActive = 600;
    boolean destroyed = false;
    Rectangle rect = new Rectangle();
    boolean enemyBullet = true;
    Texture image;

    boolean destroyOnHit = true;
    boolean isDangerous = true;

    public void update(Array<BaseTile> baseTiles, Rectangle playerRect, Array<Particle> particles, Array<EnemyBullet> enemyBullets) {

        this.x += velX;
        this.y += velY;
        timeActive++;
        if (timeActive > maxTimeActive) destroyed = true;
//        isDangerous = true;
//        destroyOnHit = true;


        rect.x = x;
        rect.y = y;
        rect.width = width;
        rect.height = height;



        for (BaseTile tile : baseTiles) {
            if (tile.isActive) {
                if (rect.overlaps(tile.rect)) {
                    destroyed = true;
                }
            }
        }
    }

    public boolean getDestroyed() {
        return destroyed;
    }

    public abstract Rectangle getRect();

    public abstract boolean getDestroyOnHit();

    public abstract boolean getIsDangerous();

    public abstract void setDestroyed(boolean destroyed);

    public abstract void draw(SpriteBatch batch);
}
