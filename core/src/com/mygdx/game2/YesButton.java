package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by kyles on 8/7/2016.
 */
public class YesButton extends AbstractButton {
    Texture image = new Texture("YesButton.png");
    Texture selectedImage = new Texture("YesButtonSelected.png");

    public YesButton(int x, int y) {
        width = 100;
        height = 40;
        this.x = x;
        this.y = y;
    }


    public void draw(SpriteBatch batch) {
        batch.draw(image,x,y,width,height);
    }

    public void drawSelected(SpriteBatch batch) {
        batch.draw(selectedImage, x,y,width,height);
    }
}
