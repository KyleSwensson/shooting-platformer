package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Created by kyle on 10/11/2015.
 */
public class FetyrCrossbowBoltTrail extends EnemyBullet {

    Texture image = new Texture("SPA/Enemy/Fetyr/Crossbow/Bolt/1.png");
    boolean isFacingRight;

    int aliveTime = 0;
    int maxAliveTime = 7; // destroy after has been alive for 80 frames

    public FetyrCrossbowBoltTrail(float x, float y) {
        enemyBullet = true;
        width = 28;
        height = 6;
        this.x = x;
        this.y = y;
        this.velY = 0;
        this.velX = 0;
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
        aliveTime ++;
        if (aliveTime > maxAliveTime) {
            destroyed = true;
        }
        this.rect = new Rectangle(this.x, this.y, this.width, this.height);



    }

    public void draw(SpriteBatch batch)
    {
        batch.draw(image,x,y,width,height);
    }
}
