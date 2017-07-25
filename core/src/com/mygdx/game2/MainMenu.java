package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by kyles on 8/2/2016.
 */
public class MainMenu extends GenericMenu {
    Texture BGTexture = new Texture("menuBG.png");

    PlayButton playButton;
    OptionsButton optionsButton;
    UnlocksButton unlocksButton;
    MainExitButton exitButton;
    SlimeMenuPointer menuPointer;

    //tells the menu which option is currently selected so it knows what to do when you press enter
    //0= play, 1 = settings, 2 = unlocks and trophies
    int selectedOption;

    public MainMenu(int width, int height) {
        playButton = new PlayButton(-130,-100);
        optionsButton = new OptionsButton(50,-100);
        unlocksButton = new UnlocksButton(230,-100);
        exitButton = new MainExitButton(-310, -107);
        menuPointer = new SlimeMenuPointer();
        this.width = width;
        this.height = height;
        this.x = -(width/2);
        this.y = -(height/2);
        selectedOption = 0;
    }

    public void update() {
        menuPointer.update();
    }


    public void draw(SpriteBatch batch) {
        batch.draw(BGTexture,x,y,width,height);

        switch (selectedOption) {
            case 0:
                playButton.drawSelected(batch);
                optionsButton.draw(batch);
                unlocksButton.draw(batch);
                exitButton.draw(batch);
                break;
            case 1:
                playButton.draw(batch);
                optionsButton.drawSelected(batch);
                unlocksButton.draw(batch);
                exitButton.draw(batch);
                break;
            case 2:
                playButton.draw(batch);
                optionsButton.draw(batch);
                unlocksButton.drawSelected(batch);
                exitButton.draw(batch);
                break;
            case 3:
                playButton.draw(batch);
                optionsButton.draw(batch);
                unlocksButton.draw(batch);
                exitButton.drawSelected(batch);
            default:
                playButton.draw(batch);
                optionsButton.draw(batch);
                unlocksButton.draw(batch);
                break;

        }

        menuPointer.draw(batch);

    }

    public void alterSelected(int change) {
        selectedOption += change;
        if (selectedOption < 0) selectedOption += 4;
        else if (selectedOption > 3) selectedOption -= 4;
        menuPointer.setOption(selectedOption);
    }

}
