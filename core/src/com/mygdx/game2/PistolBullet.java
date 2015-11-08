package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

/**
 * Created by kyle on 10/7/2015.
 */
public class PistolBullet extends PlayerBullet {

    Texture texture1 = new Texture("machineGunBullet3.png");
    Texture texture2 = new Texture("machineGunBullet4.png");
    Texture texture3 = new Texture("machineGunBullet5.png");
    Texture texture4 = new Texture("machineGunBullet6.png");
    Texture texture5 = new Texture("machineGunBullet7.png");
    Texture texture6 = new Texture("machineGunBullet8.png");


    int initialFrameCount;
    int frameCount = 0;
    int framesSwitch = 0;
    int animImage = 0;


    Texture[] textures = {texture1, texture2,texture3, texture4, texture5, texture6};


    public PistolBullet() {
        width = 20;
        height = 10;
    }
    public void update(Array<PlayerBullet> playerBullets,Array<BaseTile> baseTiles, Array<Enemy> enemies , Array<Animation> anims, Array<Particle> particles) {



        super.update(playerBullets,baseTiles, enemies, anims, particles);

        initialFrameCount++;

        if (initialFrameCount > 15) {
            frameCount++;
            if (frameCount > framesSwitch) {
                animImage++;
                frameCount = 0;
                if (animImage > 5) {
                    destroyed = true;
                }
            }
        }





        for (Enemy enemy : enemies) {
                if (rect.overlaps(enemy.getRect())) {
                    enemy.changeHealth(-10);
                    destroyed = true;
                    EnemyHitSquareAnimation anim = new EnemyHitSquareAnimation();
                    anim.x = x;
                    anim.y = y;
                    anims.add(anim);
                    EnemyHitText hitText = new EnemyHitText(x,y,10);
                    anims.add(hitText);
                }
        }
    }

    public void draw(SpriteBatch batch) {
        batch.draw(textures[animImage], x, y, width, height, 0, 0, (int)width/2, (int)height/2, !facingRight, false);
    }
}
