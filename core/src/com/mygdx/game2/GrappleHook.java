package com.mygdx.game2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by kyles on 5/21/2017.
 */
public class GrappleHook extends PlayerBullet {

    public Texture texture = new Texture("SPA/Player/GrappleHook/up.png");
    boolean isHooked;
    float x;
    float y;
    static final int UPDATES_PER_FRAME = 20;
    float rotation;

    final float baseX;
    final float baseY;
    static final float MAX_DISTANCE_FROM_BASE = 400;

    public GrappleHook(float x, float y, float velX, float velY, float rotation) {
        this.velX = velX;
        this.velY = velY;
        this.x = x;
        this.y = y;
        this.width = 12;
        this.height = 12;
        this.isHooked = false;
        this.maxTimeActive = 300;
        this.baseX = x;
        this.baseY = y;
        this.rotation = rotation;
    }

    public void destroyThis() {
        destroyed = true;
        GdxShooter2.getPlayer().triggerEndOfGrapplingHookPull();
    }

    @Override
    public void update(Array<PlayerBullet> playerBullets, Array<BaseTile> baseTiles, Array<Enemy> enemies, Array<Animation> anims, Array<Particle> particles) {

        if (!isHooked) {
            for (int i = 0; i < UPDATES_PER_FRAME; i++) {
                if (!isHooked) {
                    this.x += velX / UPDATES_PER_FRAME;
                    this.y += velY / UPDATES_PER_FRAME;

                    rect.x = x;
                    rect.y = y;
                    rect.width = width;
                    rect.height = height;

                    for (BaseTile tile : baseTiles) {
                        if (tile.isActive) {
                            if (rect.overlaps(tile.rect)) {
                                isHooked = true;
                                GdxShooter2.getPlayer().triggerGrapplingHookPull(this);
                            }
                        }
                    }
                }
            }
        } else {
            Rectangle bigRect = new Rectangle(rect.x - rect.width, rect.y - rect.height, rect.width*3, rect.height*3);
            Player player = GdxShooter2.getPlayer();
            if (player.rect.overlaps(bigRect)) {
                destroyThis();
            }
        }

        timeActive++;
        System.out.println(timeActive + "/" + maxTimeActive);
        if (timeActive > maxTimeActive) {
            destroyThis();
        }


        float distanceFromBase = new Vector2(baseX, baseY).dst(x,y);
        if (distanceFromBase > MAX_DISTANCE_FROM_BASE) {
            destroyThis();
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(texture,x,y,width/2, height/2, width, height,1,1,rotation,0,0,5,6,false,false);
        Player player = GdxShooter2.getPlayer();
        if (player.activeGrapplingHook == this) {
            GdxShooter2.setLineToDraw(x + width / 2, y + height / 2, player.x + player.width / 2, player.y + player.height / 2);
        }
    }
}