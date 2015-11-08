package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

/**
 * Created by kyle on 10/7/2015.
 */
public class Rocket extends PlayerBullet {
    public Texture texture = new Texture("rocket.png");
    public Rocket () {
        texture = new Texture("rocket.png");
        width = 24;
        height = 14;
        bulletType = "Rocket";
    }
    public void update(Array<PlayerBullet> playerBullets,Array<BaseTile> baseTiles, Array<Enemy> enemies , Array<Animation> anims, Array<Particle> particles) {
        super.update(playerBullets,baseTiles, enemies, anims, particles);
        for (Enemy enemy : enemies) {
            if (rect.overlaps(enemy.getRect())) {
                enemy.changeHealth(-15);
                destroyed = true;
                EnemyHitSquareAnimation anim = new EnemyHitSquareAnimation();
                anim.x = x;
                anim.y = y;
                anims.add(anim);

                EnemyHitText hitText = new EnemyHitText(x,y,15);
                anims.add(hitText);

                makeExplosion(playerBullets);
            }
        }
        for (BaseTile tile : baseTiles) {
            if (rect.overlaps(tile.rect)) {
                makeExplosion(playerBullets);
            }
        }
    }



    private void makeExplosion(Array<PlayerBullet> playerBullets) {
        Explosion explosion = new Explosion();
        explosion.x = (int)(x + (width / 2) - (128 / 2));
        explosion.y = (int)(y + (height / 2) - (128 / 2));
        explosion.rect.x = explosion.x;
        explosion.rect.y =explosion.y;
        playerBullets.add(explosion);
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y, width, height, 0, 0, 12, 7,!facingRight, false);
    }
}
