package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by kyles on 8/6/2016.
 */
public class BlackFadeInBG {
    double opacity; // 0 means invisible, 1 means opaque
    int currentFrame;
    int fullAnimationFrames; // num of frames it takes to go completely black to nothing or nothing to black;
    boolean fadingIn; // if this is true then it is fading into black, if this is false it is fading out from black
    int width;
    int height;
    boolean destroyed;
    boolean isActive; // turns false when done fading to black so that the main function can check on it and switch scenes before re activating it
    Texture image = new Texture("blackBG.png");

    private static BlackFadeInBG fadeInBG = new BlackFadeInBG( );

    private BlackFadeInBG() {
        opacity = 0;
        currentFrame = 0;
        fullAnimationFrames = 60;
        fadingIn = true;
        width = 800;
        height = 480;
        destroyed = false;
        isActive = false;
    }

    public static BlackFadeInBG getInstance( ) {
        return fadeInBG;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(image,0,0,width,height);
    }

    public void update() {
        if (isActive) {
            if (currentFrame < fullAnimationFrames) {
                currentFrame++;
                if (fadingIn) {
                    opacity += 1 / (double)fullAnimationFrames;
                } else {
                    opacity -= 1 / (double)fullAnimationFrames;
                }
                if (opacity > 1) opacity = 1;
                if (opacity < 0) opacity = 0;
            } else {
                isActive = false;
                currentFrame = 0;
                if (fadingIn) {
                    fadingIn = false;
                } else {
                    fadingIn = true;
                }
            }
        }
    }


}
