package com.mygdx.game2;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

/**
 * Created by kyle on 10/26/2015.
 */
public class TechnologyParticle extends Particle {

    public TechnologyParticle(int newX, int newY, float newVelX, float newVelY, String pType, int newDestroyMax) {
        super(newX, newY, newVelX, newVelY, pType, newDestroyMax);
    }

    @Override
    public void update(Array<BaseTile> baseTiles, Array<Roboto1> robot1s, int playerX, int playerY) {
        width -= .05;
        height -= .05;
        super.update(baseTiles,robot1s,playerX,playerY);
    }


}
