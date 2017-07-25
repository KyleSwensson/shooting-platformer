package com.mygdx.game2;

/**
 * Created by kyles on 2/21/2017.
 */

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;


public class CirclingBallOnChain extends Enemy {

    Texture ballImage = new Texture("SPA/Traps/Spikeball/Ball.png");
    Texture chainImage = new Texture("SPA/Traps/Spikeball/Chain.png");

    int ballX;
    int ballY;
    int centerX;
    int centerY;
    float rotationPoint;
    float radius;
    final int chainWidth = 10;
    float chainAngle;

    public static final int DIFFICULTY_POINTS = 40;

    public CirclingBallOnChain(int x, int y) {
        this.centerX = x;
        this.centerY = y;
        this.ballX = centerX;
        this.ballY = centerY;
        radius = 120;
        this.width = 24;
        this.height = 24;
        this.health = 10;
        this.rotationPoint = 0;
        this.chainAngle = 0;
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
        this.rotationPoint += 0.02;
        this.ballX = centerX + (int) (Math.sin(rotationPoint) * radius);
        this.ballY = centerY + (int) (Math.cos(rotationPoint) * radius);
        this.rect.x = ballX;
        this.rect.y = ballY;
        this.rect.width = width;
        this.rect.height = height;
        double distX = Math.sin(rotationPoint) * radius;
        double distY = Math.cos(rotationPoint) * radius;
        double distH = Math.sqrt(Math.pow(distX, 2) + Math.pow(distY, 2));
        if (ballY < centerY) {
            chainAngle = (float) (Math.asin(distX / distH) * 57.2958f) + 180f;
        } else {
            chainAngle = (float) -Math.asin(distX / distH) * 57.2958f;
        }
    }


    public void draw (SpriteBatch batch) {
        batch.draw(chainImage,centerX,centerY, chainWidth/2,0,chainWidth,radius, 1,1,chainAngle ,0 ,0,5,35,false,false);
        batch.draw(ballImage,ballX - (width / 2),ballY - (height / 2),width,height);
    }
}
