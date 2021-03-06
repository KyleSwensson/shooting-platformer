package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

/**
 * Created by kyle on 10/7/2015.
 */
public class PlayerFire extends PlayerBullet {
    public Texture texture = new Texture("fireParticle.png");
    int damage = 1;
    public PlayerFire () {
        bulletType = "Flame";
        //texture = new Texture("fireParticle.png");
    }
    public void update(Array<PlayerBullet> playerBullets,Array<BaseTile> baseTiles, Array<Enemy> enemies , Array<Animation> anims, Array<Particle> particles) {
        super.update(playerBullets,baseTiles, enemies, anims, particles);


        if (velX > 0) velX -= .1;
        else if (velX < 0) velX += .1;

        this.height -= .12;
        this.width -= .12;

        velY += .06;

        if (velY > 2) velY = 2;


        for (Enemy enemy : enemies) {
            if (rect.overlaps(enemy.getRect()) && enemy.isHittable) {
                enemy.changeHealth(-damage );

                destroyed = true;
                EnemyHitSquareAnimation anim = new EnemyHitSquareAnimation();
                anim.x = x;
                anim.y = y;
                anims.add(anim);EnemyHitText hitText = new EnemyHitText((int)x,(int)y,damage);
                anims.add(hitText);
            }
        }
        if (width < 0 || height < 0) {
            destroyed = true;
        }
    }
    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y, width, height, 0, 0, (int) width, (int) height, !facingRight, false);
    }
}
