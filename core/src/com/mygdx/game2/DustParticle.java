package com.mygdx.game2;

/**
 * Created by kyle on 10/20/2015.
 */
public class DustParticle extends Particle {
    public DustParticle(int newX, int newY, float newVelX, float newVelY, String pType, int newDestroyMax) {
        super(newX, newY, newVelX, newVelY, pType, newDestroyMax);
        width = 8;
        height = 8;
    }

}
