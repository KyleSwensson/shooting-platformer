package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by kyles on 7/24/2017.
 */
public class BossHealthBar {

    Texture containerImage = new Texture("SPA/HUD/BossHealthBar/container.png");
    Texture containerOverlayImage = new Texture("SPA/HUD/BossHealthBar/containerOverlay.png");
    Texture healthImage = new Texture("SPA/HUD/BossHealthBar/barFragment.png");

    static final int WIDTH = 284;
    static final int HEIGHT = 36;

    static final int RED_WIDTH = 270;
    static final int RED_HEIGHT = 20;
    static final int RED_OFFSET_X = 7;
    static final int RED_OFFSET_Y = 8;



    float bossHealthPercentage;

    public BossHealthBar(float bossHealthPercentage) {
        this.bossHealthPercentage = bossHealthPercentage;
    }

    public void setBossHealthPercentage(float newBossHealthPercentage) {
        this.bossHealthPercentage = newBossHealthPercentage;
    }

    public void draw(SpriteBatch batch, int x, int y) {
        batch.draw(containerImage, x, y, WIDTH, HEIGHT);
        batch.draw(healthImage,x + RED_OFFSET_X,y+RED_OFFSET_Y,RED_WIDTH*(bossHealthPercentage/100), RED_HEIGHT);
        batch.draw(containerOverlayImage, x, y, WIDTH, HEIGHT);
    }
}
