package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by kyles on 1/29/2017.
 */
public class SlimeMenuPointer {
    Texture image1 = new Texture("SPA/Menus/SlimePointer/1.png");
    Texture image2 = new Texture("SPA/Menus/SlimePointer/2.png");
    Texture image3 = new Texture("SPA/Menus/SlimePointer/3.png");
    Texture image4 = new Texture("SPA/Menus/SlimePointer/4.png");
    Texture image5 = new Texture("SPA/Menus/SlimePointer/5.png");

    Texture dripImage1 = new Texture("SPA/Menus/SlimePointer/drip/1.png");
    Texture dripImage2 = new Texture("SPA/Menus/SlimePointer/drip/2.png");
    Texture dripImage3 = new Texture("SPA/Menus/SlimePointer/drip/3.png");
    Texture dripImage4 = new Texture("SPA/Menus/SlimePointer/drip/4.png");
    Texture dripImage5 = new Texture("SPA/Menus/SlimePointer/drip/5.png");
    Texture dripImage6 = new Texture("SPA/Menus/SlimePointer/drip/6.png");
    Texture dripImage7 = new Texture("SPA/Menus/SlimePointer/drip/7.png");
    Texture dripImage8 = new Texture("SPA/Menus/SlimePointer/drip/8.png");
    Texture dripImage9 = new Texture("SPA/Menus/SlimePointer/drip/9.png");
    Texture dripImage10 = new Texture("SPA/Menus/SlimePointer/drip/10.png");
    Texture dripImage11 = new Texture("SPA/Menus/SlimePointer/drip/11.png");

    Texture[] blinkImages;
    Texture[] dripImages;
    int currMenuOption; // variable to tell you what option this should display over on the menu

    boolean isBlinking;
    boolean isDripping;

    int timeUntilBlink;
    int maxTimeUntilBlink = 200;
    int timeUntilDrip;
    int maxTimeUntilDrip = 320;

    final int option1X = -93;
    final int option1Y = -10;

    final int option2X = 77;
    final int option2Y = -10;

    final int option3X = 257;
    final int option3Y = -10;

    final int option4X = -283;
    final int option4Y = -10;

    int[] xCoords;
    int[] yCoords;

    int currImage;

    int timeToNextFrame;
    int maxBlinkingTimeToNextFrame = 14;
    int maxDrippingTimeToNextFrame = 6;


    int width = 48;
    int height = 78;

    public SlimeMenuPointer() {
        blinkImages = new Texture[]{image1, image2, image3, image4, image5};
        dripImages = new Texture[]{dripImage1, dripImage2, dripImage3, dripImage4, dripImage5, dripImage6, dripImage7, dripImage8, dripImage9, dripImage10, dripImage11};
        currMenuOption = 0;
        currImage = 0;
        timeUntilBlink = maxTimeUntilBlink;
        timeUntilDrip = maxTimeUntilDrip / 2;

        timeToNextFrame = 0;

        xCoords = new int[]{option1X, option2X, option3X, option4X};
        yCoords = new int[]{option1Y, option2Y, option3Y, option4Y};
    }

    public void setOption(int givenOption) {
        this.currMenuOption = givenOption;
    }

    public void update() {
        if (!isDripping) {
            timeUntilDrip--;
        }
        if (!isBlinking) {
            timeUntilBlink--;
        }

        if (timeUntilDrip <= 0 && !isBlinking) {
            timeUntilDrip = maxTimeUntilDrip;
            isDripping = true;
            timeToNextFrame = maxDrippingTimeToNextFrame;
        }
        if (timeUntilBlink <= 0 && !isDripping) {
            timeUntilBlink = maxTimeUntilBlink;
            isBlinking = true;
            timeToNextFrame = maxBlinkingTimeToNextFrame;
        }

        if (isBlinking) {
            timeToNextFrame--;
            if (timeToNextFrame <= 0) {
                timeToNextFrame = maxBlinkingTimeToNextFrame;
                currImage++;
                if (currImage > 4) {
                    currImage = 0;
                    isBlinking = false;
                }
            }
        } else if (isDripping) {
            timeToNextFrame--;
            if (timeToNextFrame <= 0) {
                if (currImage >= 8) {
                    timeToNextFrame = maxDrippingTimeToNextFrame - 5;
                } else if (currImage >= 4) {
                    timeToNextFrame = maxDrippingTimeToNextFrame - 4;
                } else {
                    timeToNextFrame = maxDrippingTimeToNextFrame;
                }
                currImage++;
                if (currImage > 10) {
                    currImage = 0;
                    isDripping = false;
                }
            }
        }

    }

    public void draw(SpriteBatch batch) {
        if (isBlinking) {
            batch.draw(blinkImages[currImage], xCoords[currMenuOption], yCoords[currMenuOption], width, height);
        } else if (isDripping) {
            batch.draw(dripImages[currImage], xCoords[currMenuOption], yCoords[currMenuOption], width, height);
        } else {
            batch.draw(image1, xCoords[currMenuOption], yCoords[currMenuOption], width, height);
        }
    }
}
