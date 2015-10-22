package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

/**
 * Created by kyle on 10/7/2015.
 */
public class MiniBullet extends PlayerBullet {
    public Texture texture =  new Texture("machineGunBullet.png");
    public MiniBullet() {
        texture = new Texture("machineGunBullet.png");
    }
    public void update(Array<PlayerBullet> playerBullets,Array<BaseTile> baseTiles, Array<Enemy> enemies, Array<Animation> anims) {
        super.update(playerBullets, baseTiles, enemies, anims);
        for (Enemy enemy : enemies) {
            if (rect.overlaps(enemy.getRect())) {

                enemy.changeHealth(-6);
                destroyed = true;
                EnemyHitSquareAnimation anim = new EnemyHitSquareAnimation();
                anim.x = x;
                anim.y = y;
                anims.add(anim);

                EnemyHitText hitText = new EnemyHitText(x,y,6);
                anims.add(hitText);
            }
        }
    }
    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y, width, height, 0, 0, (int)width, (int)height, !facingRight, false);
    }
}
