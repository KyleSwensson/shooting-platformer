package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by kyles on 8/3/2016.
 */
public class DeathContinueButton extends AbstractButton {
    Texture image = new Texture("deathScreenBackButton.png");
    Texture selectedImage = new Texture("deathScreenBackButtonPressed.png");
    public DeathContinueButton(int x, int y) {
        width = 100;
        height = 40;
        this.x = x;
        this.y = y;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(image, x,y,width,height);
    }

    public void drawSelected(SpriteBatch batch) {
        batch.draw(selectedImage, x,y,width,height);
    }
}
