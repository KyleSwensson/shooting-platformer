package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

/**
 * Created by kyles on 10/4/2016.
 */
public class PlayerSpiralParticle extends Particle {

    float startVal;
    int startX;
    Texture image = new Texture("achievementParticle.png");

    public PlayerSpiralParticle(int newX, int newY, float newVelX, float newVelY, String pType, int newDestroyMax) {

        super(newX, newY, newVelX, newVelY, pType, newDestroyMax);
        this.width = 4;
        this.height = 4;
        this.startVal = -3.14f;
        this.startX = (int)this.x;
    }

    public PlayerSpiralParticle(int newX, int newY, float newVelX, float newVelY, String pType, int newDestroyMax, float startVal) {
        super(newX, newY, newVelX, newVelY, pType, newDestroyMax);
        this.width = 4;
        this.height = 4;
        this.startVal = startVal;
        this.startX = (int)this.x;

    }

    public void update(Array<Particle> particles, Array<BaseTile> baseTiles, Array<Roboto1> robot1s, int playerX, int playerY) {

        super.update(particles, baseTiles,robot1s,playerX, playerY);
        //TODO: every 3 frames or so spawn a little fadey particle
        //make this oscillate back and forth over 24 pixels, so the sin function should have a amplitude of 12
        startVal+= .23;
        this.x = (float)(startX + Math.cos(startVal)*14);

        velY = 1.5f;
        System.out.println("particle updating");

        if(this.destroyTime % 3 == 0) {
            GenericFadingParticle fadePart = new GenericFadingParticle((int)this.x, (int)this.y, 0, 0, "GenericFadingParticle", 50);
            particles.add(fadePart);
        }



    }

    public void draw(SpriteBatch batch) {
        batch.draw(image, x,y,width,height);

    }
}
