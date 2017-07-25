package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Created by kyles on 12/3/2016.
 */
public class GroundSpikes extends Enemy {

    Texture image = new Texture("SPA/tile-map/spikes.png");
    public static final int DIFFICULTY_POINTS = 10;

    public GroundSpikes(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = 32;
        this.height = 16;
        this.health = 10;
        this.rect = new Rectangle(this.x, this.y, this.width, this.height);
        this.isHittable = false;
    }

    public void changeHealth(int amount) {

    }

    public Rectangle getRect() {
        return this.rect;
    }

    public boolean getDestroyed() {
        return this.destroyed;
    }

    public void update(Array<Enemy> enemies, Array<EnemyBullet> enemyBullets, Array<PlayerBullet> bullets, Array<Item> items, Array<BaseTile> baseTiles, Array<Particle> particle1s, int playerX, int playerY) {

    }


    public void draw (SpriteBatch batch) {
        batch.draw(image,x,y,width,height);
    }
}
