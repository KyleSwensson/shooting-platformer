package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

/**
 * Created by kyles on 8/20/2016.
 */
public class Pickup {
    float x;
    float y;
    int width;
    int height;
    int pickupType; // number 0 through 4 that tells what sort of thing you are picking up

    Texture cannonTexture = new Texture("SPA/HUD/heavyIcon.png"); // 2
    Texture machineGunTexture = new Texture("SPA/HUD/machineGunIcon.png"); // 3
    Texture flameThrowerTexture = new Texture("SPA/HUD/fireIcon.png"); // 4
    Texture grenadeTexture = new Texture("SPA/HUD/grenadeIcon.png"); // 5
    Texture rocketLaunchTexture = new Texture("SPA/HUD/rocketIcon.png"); // 6


    Random random = new Random();

    int framesActive; // number of frames that the thing has updated so far

    boolean floatUpnDown = false;



    public Pickup(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        pickupType = random.nextInt(5) + 2;
        framesActive = 0;
        this.floatUpnDown = true;
    }

    public Pickup(float x, float y, int width, int height, boolean floatUpnDown) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        pickupType = random.nextInt(5) + 2;
        framesActive = 0;
        this.floatUpnDown = floatUpnDown;
    }

    public void draw(SpriteBatch batch) {
        switch(pickupType) {
            case 2:
                batch.draw(cannonTexture,x,y,width,height);
                break;
            case 3:
                batch.draw(machineGunTexture,x,y,width,height);
                break;
            case 4:
                batch.draw(flameThrowerTexture,x,y,width,height);
                break;
            case 5:
                batch.draw(grenadeTexture,x,y,width,height);
                break;
            case 6:
                batch.draw(rocketLaunchTexture,x,y,width,height);
                break;
            default:
                break;

        }
    }

    public void update() {
        framesActive++;
        if (floatUpnDown) {
            y += Math.sin((double) framesActive / 40) / 8;
        }
    }
}
