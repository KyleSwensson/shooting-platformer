package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by kyles on 8/2/2016.
 */
public class DeathMenu extends GenericMenu {

    Texture image = new Texture("deathScreen.png");

    DeathContinueButton deathContinueButton;
    DeathRestartButton deathRestartButton;

    //int that tells you what item on menu is selected
    // 0 = go to main menu, 1 = restart game
    int selectedOption;


    public DeathMenu() {
        width = 400;
        height = 250;
        x = 400 - (width/2);
        y = 240 - (height/2);
        deathContinueButton = new DeathContinueButton(x - 140 + (width/2),y - 60 + (height/2));
        deathRestartButton = new DeathRestartButton(x + 50 + (width / 2), y - 60 + (height/2));
        selectedOption = 0;
    }
    public void draw(SpriteBatch batch) {
        batch.draw(image, x,y,width,height);
        switch (selectedOption) {
            case 0:
                deathContinueButton.drawSelected(batch);
                deathRestartButton.draw(batch);
                break;
            case 1:
                deathContinueButton.draw(batch);
                deathRestartButton.drawSelected(batch);
                break;
        }

    }

    public void alterSelected(int change) {
        selectedOption += change;
        if (selectedOption < 0) selectedOption += 2;
        else if (selectedOption > 1) selectedOption -= 2;
    }
}
