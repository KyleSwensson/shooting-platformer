package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

/**
 * Created by kyle on 10/7/2015.
 */
public class CannonBall extends PlayerBullet {

    int partSpawnTime;
    int partSpawnMax = 3;


    public Texture texture = new Texture("cannonBall.png");
    public CannonBall () {
        texture = new Texture("cannonBall.png");

        width = 24;
        height = 12;
        partSpawnTime = 0;

    }
    public void update(Array<PlayerBullet> playerBullets,Array<BaseTile> baseTiles, Array<Enemy> enemies , Array<Animation> anims, Array<Particle> particles) {
        partSpawnTime ++;
        if (partSpawnTime > partSpawnMax) {
            partSpawnTime = 0;
            TechnologyParticle part = new TechnologyParticle((int)x,(int)y+random.nextInt((int)height),0,0,"technology",15);
            particles.add(part);
            if (partSpawnMax > 1) {
                partSpawnMax -= 1;
            }
        }

        super.update(playerBullets,baseTiles, enemies, anims, particles);

        velX *= 1.1;
        for (Enemy enemy : enemies) {
            if (rect.overlaps(enemy.getRect()) && enemy.isHittable) {
                enemy.changeHealth(-15);
                EnemyHitText hitText = new EnemyHitText((int)x,(int)y,15);
                anims.add(hitText);
                destroyed = true;
                EnemyHitSquareAnimation anim = new EnemyHitSquareAnimation();
                anim.x = x;
                anim.y = y;
                anims.add(anim);
            }
        }
    }
    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y, width, height, 0, 0, 12, 4, !facingRight, false);
    }
}
