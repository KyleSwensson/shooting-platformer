package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by kyles on 11/12/2016.
 */
public class Lance extends EnemyBullet {
    SwordsmanBoss parentBoss;
    boolean isFacingLeft;

    Texture[] lanceTextures = new Texture[4];
    Texture[] lanceParticleTextures = new Texture[3];
    Texture lt1 = new Texture("SPA/Enemy/Boss/Swordsman/Lance/1.png");
    Texture lt2 = new Texture("SPA/Enemy/Boss/Swordsman/Lance/2.png");
    Texture lt3 = new Texture("SPA/Enemy/Boss/Swordsman/Lance/3.png");
    Texture lt4 = new Texture("SPA/Enemy/Boss/Swordsman/Lance/4.png");
    Texture pt1 = new Texture("SPA/Enemy/Boss/Swordsman/Lance/ThrustParticles/1.png");
    Texture pt2 = new Texture("SPA/Enemy/Boss/Swordsman/Lance/ThrustParticles/2.png");
    Texture pt3 = new Texture("SPA/Enemy/Boss/Swordsman/Lance/ThrustParticles/3.png");

    public Lance(SwordsmanBoss parentBoss) {
        this.parentBoss = parentBoss;
        this.isFacingLeft = !parentBoss.facingRight;
        this.x = parentBoss.x;
        this.y = parentBoss.y;
        this.width = 42;
        this.height = 12;
        lanceTextures[0] = lt1;
        lanceTextures[1] = lt2;
        lanceTextures[2] = lt3;
        lanceTextures[3] = lt4;
        lanceParticleTextures[0] = pt1;
        lanceParticleTextures[1] = pt2;
        lanceParticleTextures[2] = pt3;


        this.destroyOnHit = false;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public boolean getIsDangerous() {
        return this.isDangerous;
    }

    public boolean getDestroyOnHit() {
        return this.destroyOnHit;
    }



    public Rectangle getRect() {
        return this.rect;
    }

    public void update() {

        this.isFacingLeft = !parentBoss.facingRight;

        if (this.isFacingLeft) {
            this.x = parentBoss.x  - 40;
            this.y = parentBoss.y + 5;
        } else {
            this.x = parentBoss.x + 27;
            this.y = parentBoss.y + 5;
        }
    }

    public void draw(SpriteBatch batch) {

        batch.draw(lanceTextures[1], x, y, width, height, 0, 0, 21, 6, isFacingLeft, false);
    }
}
