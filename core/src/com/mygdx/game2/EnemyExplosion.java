package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Created by kyle on 10/15/2015.
 */
public class EnemyExplosion extends EnemyBullet {
    Texture texture1 = new Texture("SPA/Objects/Pow/1.png");
    Texture texture2 = new Texture("SPA/Objects/Pow/2.png");
    Texture texture3 = new Texture("SPA/Objects/Pow/3.png");
    Texture texture4 = new Texture("SPA/Objects/Pow/4.png");
    Texture texture5 = new Texture("SPA/Objects/Pow/5.png");
    Texture texture6 = new Texture("SPA/Objects/Pow/6.png");
    Texture texture7 = new Texture("SPA/Objects/Pow/7.png");
    Texture texture8 = new Texture("SPA/Objects/Pow/8.png");
    Texture[] textures = {texture1, texture2, texture3, texture4, texture5, texture6, texture7, texture8};
    boolean destroyed;

    public int animImage = 0;
    public int frameCount = 0;
    public int framesSwitch = 2;
    public float rotation = 0;

    public EnemyExplosion(float x, float y) {
        width = 128;
        height = 128;
        this.x = x - width / 2;
        this.y = y - height / 2;
        rect = new Rectangle(this.x, this.y, width,height);
        this.destroyOnHit = false;
        this.isDangerous = true;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public boolean getIsDangerous() {
        return this.isDangerous;
    }

    public Rectangle getRect() {
        return rect;
    }

    public boolean getDestroyOnHit() {
        return destroyOnHit;
    }

    public void update(Array<BaseTile> baseTiles, Rectangle playerRect, Array<Particle> particles, Array<EnemyBullet> enemyBullets) {
        frameCount++;

        System.out.println(destroyed);

        System.out.println("exploding");
        if (frameCount > framesSwitch) {
            animImage ++;
            frameCount = 0;
            if (animImage > 7) {
                destroyed = true;
            }
        }
    }

    @Override
    public boolean getDestroyed() {
        return destroyed;
    }

    public void draw(SpriteBatch batch) {
        System.out.println("exploding");

        if (animImage <= 7) {
            batch.draw(textures[animImage], x, y, 64,64, 128, 128,1, 1, rotation, 0, 0 , 64, 64, false, false);
        }

    }
}
