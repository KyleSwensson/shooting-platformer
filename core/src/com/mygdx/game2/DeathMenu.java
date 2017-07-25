package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by kyles on 8/2/2016.
 */
public class DeathMenu extends GenericMenu {

    Texture image = new Texture("deathScreen.png");


    DeathBackButton deathBackButton;

    DeathRestartButton deathRestartButton;


    //int that tells you what item on menu is selected
    // 0 = go to main menu, 1 = restart game
    int selectedOption;


    public DeathMenu() {
        width = 400;
        height = 250;
        x = 400 - (width / 2);
        y = 240 - (height / 2);
        deathBackButton = new DeathBackButton(x + (width / 2) - (deathBackButton.WIDTH/2), y + 100);
        deathRestartButton = new DeathRestartButton(x + (width / 2) - (deathRestartButton.WIDTH/2), y + 35);
        selectedOption = 0;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(image, x, y, width, height);
        deathBackButton.draw(batch);
        deathRestartButton.draw(batch);
    }

    public void update() {
        deathBackButton.update();
        deathRestartButton.update();
    }


    public void alterSelected(int change) {
        selectedOption += change;
        if (selectedOption < 0) selectedOption += 2;
        else if (selectedOption > 1) selectedOption -= 2;

        if (selectedOption == 0) {
            deathBackButton.setSelected(true,false);
            deathRestartButton.setSelected(false,true);
        } else {
            deathBackButton.setSelected(false,false);
            deathRestartButton.setSelected(true,true);
        }
    }
}
