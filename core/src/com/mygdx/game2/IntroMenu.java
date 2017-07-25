package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by kyles on 8/2/2016.
 */
public class IntroMenu extends GenericMenu {

    Texture image = new Texture("introSlab.png");

    YesButton yesButton;
    NoButton noButton;

    //int that tells you what item on menu is selected
    // 0 = go to main menu, 1 = restart game
    int selectedOption;


    public IntroMenu() {
        width = 500;
        height = 250;
        x = 0 - (width/2);
        y = 0 - (height/2);
        yesButton = new YesButton(x - 140 + (width/2),y - 60 + (height/2));
        noButton = new NoButton(x + 50 + (width / 2), y - 60 + (height/2));
        selectedOption = 0;
    }
    public void draw(SpriteBatch batch) {
        batch.draw(image, x,y,width,height);
        switch (selectedOption) {
            case 0:
                yesButton.drawSelected(batch);
                noButton.draw(batch);
                break;
            case 1:
                yesButton.draw(batch);
                noButton.drawSelected(batch);
                break;
        }

    }

    public void alterSelected(int change) {
        selectedOption += change;
        if (selectedOption < 0) selectedOption += 2;
        else if (selectedOption > 1) selectedOption -= 2;
    }
}
