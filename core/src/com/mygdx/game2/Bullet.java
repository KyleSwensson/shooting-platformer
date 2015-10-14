package com.mygdx.game2;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created by kyle on 10/7/2015.
 */
public abstract class Bullet  {
    int x = 0;
    int y = 0;
    float width;
    float height;
    float velX = 0;
    float velY = 0;
    boolean enemyBullet = false;
    Rectangle rect = new Rectangle();



}
