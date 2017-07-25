package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by kyles on 1/29/2017.
 */
public class SmallSlimeMenuPointer {
    Texture image = new Texture("SPA/Menus/SmallSlimePointer/1.png");
    int currMenuOption; // variable to tell you what option this should display over on the menu

    int[] xCoords;
    int[] yCoords;

    int width = 16;
    int height = 18;

    public SmallSlimeMenuPointer(int[] menuXCoords, int[] menuYCoords) {
        currMenuOption = 0;

        xCoords = menuXCoords;
        yCoords = menuYCoords;
    }

    public void setOption(int givenOption) {
        this.currMenuOption = givenOption;
    }

    public void update() {

    }

    public void draw(SpriteBatch batch) {
            batch.draw(image, xCoords[currMenuOption], yCoords[currMenuOption], width, height);
    }
}
