package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by kyle on 10/25/2015.
 */
public class WeaponSelectTile {
    int weaponNum = 0; // type of weapon displayed in box
    Texture image;
    int x;
    int y;
    int width;
    int height;
    Texture[] weapons;

    public WeaponSelectTile(Texture img,int x,int y, Texture wep1, Texture wep2, Texture wep3, Texture wep4, Texture wep5, Texture wep6) {
        this.image = img;
        this.y = y;
        this.x = x;
        width = 28;
        height = 28;

        weapons = {wep1,wep2,wep3,wep4,wep5,wep6};
    }

    public void draw(SpriteBatch batch) {
        batch.draw(image,x,y,width,height);
        //TODO: add drawing the guns inside this based on weaponNum
    }
}
