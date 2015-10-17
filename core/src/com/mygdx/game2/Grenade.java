package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

/**
 * Created by kyle on 10/16/2015.
 */
public class Grenade extends PlayerBullet {


    int deleteTime = 100;
    int currentTime = 0;
    public Texture texture = new Texture("grenade.png");

    public Grenade() {
        width = 6;
        height = 10;

    }

    private void makeExplosion(Array<PlayerBullet> playerBullets) {
        Explosion explosion = new Explosion();
        explosion.x = (int)(x + (width / 2) - (128 / 2));
        explosion.y = (int)(y + (height / 2) - (128 / 2));
        explosion.rect.x = explosion.x;
        explosion.rect.y =explosion.y;
        playerBullets.add(explosion);
    }

    public void update(Array<PlayerBullet> playerBullets,Array<BaseTile> baseTiles, Array<Enemy> enemies , Array<Animation> anims) {
        super.update(playerBullets,baseTiles, enemies, anims);
        for (Enemy enemy : enemies) {
            if (rect.overlaps(enemy.getRect())) {
                enemy.changeHealth(-4);
                velX = enemy.velX + (float)(velX * -.5);
                velY = enemy.velY + (float)(velX * -.5);
                EnemyHitSquareAnimation anim = new EnemyHitSquareAnimation();
                anim.x = x;
                anim.y = y;
                anims.add(anim);
                destroyed = true;
                makeExplosion(playerBullets);
            }
        }

        velY -= .2;


        currentTime ++;
        if (currentTime > deleteTime) {
            destroyed = true;
            makeExplosion(playerBullets);
        }
        checkTilesHit(baseTiles);
    }

    public void checkTilesHit(Array<BaseTile> baseTiles) {
        for (BaseTile tile : baseTiles) {
            if (tile.isActive) {
                if (rect.overlaps(tile.rect)) {
                    float rect1Top = rect.y + rect.height;
                    float rect1Bot = rect.y;
                    float rect1Left = rect.x;
                    float rect1Right = rect.x + rect.width;
                    float rect2Top = tile.rect.y + tile.rect.height;
                    float rect2Bot = tile.rect.y;
                    float rect2Left = tile.rect.x;
                    float rect2Right = tile.rect.x + tile.rect.width;
                    float leftPassedDistance = Math.abs(rect2Right - rect1Left);
                    float rightPassedDistance = Math.abs(rect1Right - rect2Left);
                    float topPassedDistance = Math.abs(rect1Top - rect2Bot);
                    float bottomPassedDistance = Math.abs(rect2Top - rect1Bot);
                    String directionLeastPassed;
                    if (leftPassedDistance < rightPassedDistance &&
                            leftPassedDistance < topPassedDistance &&
                            leftPassedDistance < bottomPassedDistance) directionLeastPassed = "Left";
                    else if (rightPassedDistance < topPassedDistance &&
                            rightPassedDistance < bottomPassedDistance) directionLeastPassed = "Right";
                    else if (topPassedDistance < bottomPassedDistance) directionLeastPassed = "Top";
                    else directionLeastPassed = "Bottom";


                    if (directionLeastPassed.equals("Bottom")) {
                        velY = -(int) (velY * .8);
                        velX *= .8;

                        y = (int)rect2Top;


                    } else if (directionLeastPassed.equals("Top")) {
                        velY = -(int) (velY * .8);
                        velX *= .8;
                        y = (int)(rect2Bot - rect.height);
                    } else if (directionLeastPassed.equals("Left")) {
                        velX = -(int) (velX * .7);
                        x = (int)rect2Right;
                    } else {
                        velX = -(int) (velX * .7);
                        x = (int)(rect2Left - rect.width);
                    }


                }


            }
        }
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y, width, height, 0, 0, (int)width, (int)height, !facingRight, false);
    }
}
