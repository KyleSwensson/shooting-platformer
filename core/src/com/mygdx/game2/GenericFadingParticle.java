package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

/**
 * Created by kyles on 10/4/2016.
 */
public class GenericFadingParticle extends Particle {

    float startVal;
    int startX;
    Texture image = new Texture("achievementParticle.png");

    public GenericFadingParticle(int newX, int newY, float newVelX, float newVelY, String pType, int newDestroyMax) {

        super(newX, newY, newVelX, newVelY, pType, newDestroyMax);
        this.width = 4;
        this.height = 4;
        this.startVal = -1;
        this.startX = (int)this.x;
    }


    public void update(Array<Particle> particles, Array<BaseTile> baseTiles, Array<Roboto1> robot1s, int playerX, int playerY) {

        super.update(particles, baseTiles,robot1s,playerX, playerY);
        this.width -= .1;
        this.height -= .1;




    }

    public void draw(SpriteBatch batch) {
        batch.draw(image, x,y,width,height);

    }
}
