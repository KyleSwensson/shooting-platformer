package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by kyles on 8/2/2016.
 */
public class PlayButton extends AbstractButton {
    Texture image = new Texture("playButton.png");
    Texture selectedImage = new Texture("playButtonPressed.png");

    public PlayButton(int x, int y) {
        this.x = x;
        this.y = y;
        width = 100;
        height = 100;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(image,x,y,width,height);
    }

    public void drawSelected(SpriteBatch batch) {
        batch.draw(selectedImage, x,y,width,height);
    }
}
