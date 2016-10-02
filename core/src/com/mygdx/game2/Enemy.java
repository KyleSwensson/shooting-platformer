package com.mygdx.game2;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

/**
 * Created by kyle on 10/7/2015.
 */
public abstract class Enemy extends Character {
    Rectangle rect = new Rectangle();
    float x;
    float y;
    int width = 0;
    int height = 0;
    float velX = 0;
    float velY = 0;
    float xDist = 0;
    float yDist  =0;
    boolean destroyed;
    int health = 0;
    String enemyType = "";
    Random random = new Random();

    public String getType() {
        return enemyType;
    }


    public abstract void changeHealth(int addToHealth);

    public abstract Rectangle getRect();

    public abstract boolean getDestroyed();

    public void destroyEnemy(Array<Particle> particle1s, Array<Item> items) {


        HealthCrystal hp = new HealthCrystal(this.x,
                this.y,
                -velX + random.nextInt(3) - 1,
                3 + random.nextInt(3) - 1,
                10,12);
        items.add(hp);

        ManaCrystal mp = new ManaCrystal(this.x,
                this.y,
                -velX + random.nextInt(3) - 1,
                3 + random.nextInt(3) - 1,
                10,12);
        items.add(mp);

        int numCoins = random.nextInt(3); // spawn between 0 and 2 coins

        System.out.print(numCoins);

        if (numCoins >= 1) {
            BasicCoin coin = new BasicCoin(this.x,
                    this.y,
                    random.nextInt(5) - 2,
                    3 + random.nextInt(3) - 1,
                    10, 12);
            items.add(coin);
        }

        if (numCoins >= 2) {
            BasicCoin coin2 = new BasicCoin(this.x,
                    this.y,
                    random.nextInt(5) - 2,
                    3 + random.nextInt(3) - 1,
                    10, 12);
            items.add(coin2);
        }
    }

    public abstract void update(Array<Enemy> enemies, Array<EnemyBullet> enemyBullets,Array<PlayerBullet> bullets,Array<Item> items, Array<BaseTile> baseTiles, Array<Particle> particle1s, int playerX, int playerY);

    public abstract void draw(SpriteBatch batch);
}
