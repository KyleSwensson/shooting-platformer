package com.mygdx.game2;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Created by kyle on 10/7/2015.
 */
public abstract class Enemy extends Character {
    Rectangle rect = new Rectangle();
    int x = 0;
    int y = 0;
    int width = 0;
    int height = 0;
    float velX = 0;
    float velY = 0;
    float xDist = 0;
    float yDist  =0;
    boolean destroyed;
    int health = 0;


    public abstract void changeHealth(int addToHealth);

    public abstract Rectangle getRect();

    public abstract boolean getDestroyed();

    public abstract void update(Array<EnemyBullet> enemyBullets,Array<PlayerBullet> bullets,Array<Item> items, Array<BaseTile> baseTiles, Array<Particle> particle1s, int playerX, int playerY);

    public abstract void draw(SpriteBatch batch);
}
