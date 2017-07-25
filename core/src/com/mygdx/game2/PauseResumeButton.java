package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by kyles on 8/3/2016.
 */
public class PauseResumeButton extends AbstractButton {
    Texture unselectedImage = new Texture("SPA/Menus/PauseMenu/ResumeButton/unselected.png");
    Texture selectedImage = new Texture("SPA/Menus/PauseMenu/ResumeButton/selectedFull.png");
    Texture[] selectedUpImages = new Texture[]{
            new Texture("SPA/Menus/PauseMenu/ResumeButton/selectedUp1.png"),
            new Texture("SPA/Menus/PauseMenu/ResumeButton/selectedUp2.png"),
            new Texture("SPA/Menus/PauseMenu/ResumeButton/selectedUp3.png"),
            new Texture("SPA/Menus/PauseMenu/ResumeButton/selectedUp4.png"),
            new Texture("SPA/Menus/PauseMenu/ResumeButton/selectedUp5.png"),
            new Texture("SPA/Menus/PauseMenu/ResumeButton/selectedUp6.png"),
            new Texture("SPA/Menus/PauseMenu/ResumeButton/selectedUp7.png"),
            new Texture("SPA/Menus/PauseMenu/ResumeButton/selectedFull.png")
    };
    Texture[] selectedDownImages = new Texture[]{
            new Texture("SPA/Menus/PauseMenu/ResumeButton/selectedDown1.png"),
            new Texture("SPA/Menus/PauseMenu/ResumeButton/selectedDown2.png"),
            new Texture("SPA/Menus/PauseMenu/ResumeButton/selectedDown3.png"),
            new Texture("SPA/Menus/PauseMenu/ResumeButton/selectedDown4.png"),
            new Texture("SPA/Menus/PauseMenu/ResumeButton/selectedDown5.png"),
            new Texture("SPA/Menus/PauseMenu/ResumeButton/selectedDown6.png"),
            new Texture("SPA/Menus/PauseMenu/ResumeButton/selectedDown7.png"),
            new Texture("SPA/Menus/PauseMenu/ResumeButton/selectedFull.png")
    };

    public static final int WIDTH = 100;
    public static final int HEIGHT = 40;

    boolean isSelected;

    boolean selectionDirectionIsUp;

    float selectionFrame;
    static final int MAX_SELECTION_FRAME = 7;

    public PauseResumeButton(int x, int y) {
        width = WIDTH;
        height = HEIGHT;
        this.x = x;
        this.y = y;
        selectionFrame = 0;
        isSelected = false;
    }

    public void update() {
        if (selectionFrame < MAX_SELECTION_FRAME) {
            selectionFrame += 1.5;
            if (selectionFrame > MAX_SELECTION_FRAME) {
                selectionFrame = MAX_SELECTION_FRAME;
            }
        }
    }

    public void setSelected(boolean selected, boolean selectionDirectionIsUp) {
        if (selected != isSelected) {
            isSelected = selected;
            this.selectionDirectionIsUp = selectionDirectionIsUp;
            selectionFrame = 0;
        }
    }

    public void draw(SpriteBatch batch) {
        if (selectionFrame == MAX_SELECTION_FRAME) {
            if (isSelected) {
                batch.draw(selectedImage, x, y, width, height);
            } else {
                batch.draw(unselectedImage, x, y, width, height);
            }
        } else {
            if (!isSelected) {
                if (selectionDirectionIsUp) {
                    batch.draw(selectedUpImages[MAX_SELECTION_FRAME-(int)selectionFrame],x,y,width,height);
                } else {
                    batch.draw(selectedDownImages[MAX_SELECTION_FRAME-(int)selectionFrame],x,y,width,height);
                }
            } else {
                if (selectionDirectionIsUp) {
                    batch.draw(selectedUpImages[(int)selectionFrame],x,y,width,height);
                } else {
                    batch.draw(selectedDownImages[(int)selectionFrame],x,y,width,height);
                }
            }
        }
    }
}
