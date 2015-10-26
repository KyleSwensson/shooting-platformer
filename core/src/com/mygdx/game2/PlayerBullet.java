package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.awt.*;
import java.util.Random;

/**
 * Created by kyle on 9/16/2015.
 */
public abstract class PlayerBullet extends Bullet {

    int timeActive = 0;
    int maxTimeActive = 500;
    boolean destroyed = false;
    String bulletType = "";
    boolean facingRight; // false = going right, true = going left
    public Rectangle rect = new Rectangle();
    public Texture texture = new Texture("machineGunBullet.png");
    Random random = new Random();

    public PlayerBullet() {

    }

    public boolean getDestroyed() {
        return destroyed;
    }

    public void update(Array<PlayerBullet> playerBullets, Array<BaseTile> baseTiles, Array<Enemy> enemies, Array<Animation> anims, Array<Particle> particles) {
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
                    if (!bulletType.equals("Grenade"))
                    destroyed = true;

                    if (!bulletType.equals("Grenade") && !bulletType.equals("Flame") && !bulletType.equals("Rocket")) {
                        if ((int) height > 0) {
                            if (facingRight) {
                                DustParticle part = new DustParticle(tile.x - 3, y + random.nextInt((int) height), 0, 0, "dust", 60);
                                particles.add(part);
                            } else {
                                DustParticle part = new DustParticle(tile.x + tile.width + 3, y + random.nextInt((int) height), 0, 0, "dust", 60);
                                particles.add(part);
                            }
                        }
                    }
                }
            }
        }



    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y, width, height, 0, 0, (int)width, (int)height, !facingRight, false);
    }
}
