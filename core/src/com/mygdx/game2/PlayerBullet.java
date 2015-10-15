package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.awt.*;

/**
 * Created by kyle on 9/16/2015.
 */
public abstract class PlayerBullet extends Bullet {

    int timeActive = 0;
    int maxTimeActive = 500;
    boolean destroyed = false;
    String bulletType;
    boolean facingRight; // false = going right, true = going left
    public Rectangle rect = new Rectangle();
    public Texture texture = new Texture("machineGunBullet.png");

    public PlayerBullet() {

    }

    public boolean getDestroyed() {
        return destroyed;
    }

    public void update(Array<PlayerBullet> playerBullets, Array<BaseTile> baseTiles, Array<Enemy> enemies, Array<Animation> anims) {
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
                    destroyed = true;
                }
            }
        }



    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y, width, height, 0, 0, (int)width, (int)height, !facingRight, false);
    }
}
