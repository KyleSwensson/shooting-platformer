package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by kyles on 8/2/2016.
 */
public class PauseMenu extends GenericMenu {
    Texture menuImage = new Texture("pauseScreen.png");

    PauseResumeButton resumeButton;
    PauseControlsButton controlsButton;
    PauseExitButton exitButton;

    int buttonSelected; // 0 = exit, 1 = resume, 2 = controls
    int width;
    int height;
    int x;
    int y;

    int resumeWidth;
    int resumeHeight;
    int controlWidth;
    int controlHeight;
    int exitWidth;
    int exitHeight;

    public PauseMenu() {
        buttonSelected = 1;
        width = 400;
        height = 342;
        x = 400 - (width / 2);
        y = 240 - (height / 2);
        exitWidth = 40;
        exitHeight = 40;
        controlWidth = 38;
        controlHeight = 28;
        resumeWidth = 20;
        resumeHeight = 32;

        resumeButton = new PauseResumeButton(x + (width / 2) - (PauseResumeButton.WIDTH / 2), y + 150);
        exitButton = new PauseExitButton(x + (width / 2) - (PauseExitButton.WIDTH / 2), y + 90);
        controlsButton = new PauseControlsButton(x + (width / 2) - (PauseControlsButton.WIDTH / 2), y + 30);

    }

    public void update() {
        resumeButton.update();
        exitButton.update();
        controlsButton.update();
    }

    public int getButtonSelected() {
        return buttonSelected;
    }

    public void changeButtonSelected(int changeSelectedBy) {
        assert(Math.abs(changeSelectedBy) == 1);
        int previousButtonSelected = buttonSelected;
        buttonSelected += changeSelectedBy;
        while (buttonSelected < 0) {
            buttonSelected += 3;
        }
        while (buttonSelected > 2) {
            buttonSelected -= 3;
        }

        switch (buttonSelected) {
            case 0:
                if (previousButtonSelected == 1) {
                    controlsButton.setSelected(false,true);
                    exitButton.setSelected(false, true);
                    resumeButton.setSelected(true, false);
                } else if (previousButtonSelected == 2) {
                    controlsButton.setSelected(false,false);
                    exitButton.setSelected(false,true);
                    resumeButton.setSelected(true, true);
                }
                break;
            case 1:
                if (previousButtonSelected == 0) {
                    resumeButton.setSelected(false,false);
                    exitButton.setSelected(true,true);
                    controlsButton.setSelected(false,true);
                } else if (previousButtonSelected == 2) {
                    resumeButton.setSelected(false,true);
                    exitButton.setSelected(true,false);
                    controlsButton.setSelected(false,true);
                }
                break;
            case 2:
                if (previousButtonSelected == 1) {
                    resumeButton.setSelected(false,true);
                    exitButton.setSelected(false,false);
                    controlsButton.setSelected(true,true);
                } else if (previousButtonSelected == 0) {
                    resumeButton.setSelected(false,true);
                    exitButton.setSelected(false,true);
                    controlsButton.setSelected(true,false);
                }
                break;
        }

    }


    public void draw(SpriteBatch batch) {
        batch.draw(menuImage, x, y, width, height);

        resumeButton.draw(batch);
        exitButton.draw(batch);
        controlsButton.draw(batch);
    }
}
