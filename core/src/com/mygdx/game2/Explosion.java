package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Created by kyle on 10/15/2015.
 */
public class Explosion extends PlayerBullet {
    Texture texture1 = new Texture("SPA/Objects/Pow/1.png");
    Texture texture2 = new Texture("SPA/Objects/Pow/2.png");
    Texture texture3 = new Texture("SPA/Objects/Pow/3.png");
    Texture texture4 = new Texture("SPA/Objects/Pow/4.png");
    Texture texture5 = new Texture("SPA/Objects/Pow/5.png");
    Texture texture6 = new Texture("SPA/Objects/Pow/6.png");
    Texture texture7 = new Texture("SPA/Objects/Pow/7.png");
    Texture texture8 = new Texture("SPA/Objects/Pow/8.png");
    Texture[] textures = {texture1, texture2, texture3, texture4, texture5, texture6, texture7, texture8};
    boolean destroyed;

    public int animImage = 0;
    public int frameCount = 0;
    public int framesSwitch = 2;
    public float rotation = 0;

    public Explosion() {
        width = 128;
        height = 128;
        rect = new Rectangle(0,0,width,height);
    }

    public void update(Array<PlayerBullet> playerBullets,Array<BaseTile> baseTiles, Array<Enemy> enemies , Array<Animation> anims) {
        frameCount++;

        if (frameCount > framesSwitch) {
            animImage ++;
            frameCount = 0;
            if (animImage > 7) {
                destroyed = true;
            }
        }
        if (frameCount == 1 && animImage == 0) {
            for (Enemy enemy : enemies) {
                if (rect.overlaps(enemy.getRect())) {
                    enemy.changeHealth(-20);
                    EnemyHitText hitText = new EnemyHitText((int)enemy.x,(int)enemy.y,20);
                    anims.add(hitText);
                    EnemyHitSquareAnimation anim = new EnemyHitSquareAnimation();
                    anim.x = (int)enemy.x;
                    anim.y = (int)enemy.y;
                    anims.add(anim);
                    System.out.println("explosion hit an enemy");
                }
            }
        }


    }

    public boolean getDestroyed() {
        return destroyed;
    }

    public void draw(SpriteBatch batch) {
        if (animImage <= 7) {
            batch.draw(textures[animImage], x, y, 64,64, 128, 128,1, 1, rotation, 0, 0 , 64, 64, false, false);
        }

    }
}
