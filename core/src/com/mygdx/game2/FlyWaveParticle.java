package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

/**
 * Created by kyles on 8/6/2016.
 */
public class FlyWaveParticle extends Particle {

    double startPos;
    Texture image = new Texture("flyingSpinParticle.png");

    public FlyWaveParticle(int newX, int newY, float newVelX, float newVelY, String pType, int newDestroyMax) {

        super(newX, newY, newVelX, newVelY, pType, newDestroyMax);
        startPos = (random.nextDouble() * 2 * 3.14);
        this.width = 3;
        this.height = 3;

    }

    public void update(Array<BaseTile> baseTiles, Array<Roboto1> robot1s, int playerX, int playerY) {
        velY = -1f;
        velX = (float)Math.sin(startPos) * 2.5f ;
        startPos += .1;
        super.update(baseTiles,robot1s,playerX, playerY);

    }

    public void draw(SpriteBatch batch) {
        batch.draw(image, x,y,width,height);

    }
}
