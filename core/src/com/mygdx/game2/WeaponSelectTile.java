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
    Texture[] weapons = {null,null,null,null,null,null};

     public WeaponSelectTile(Texture img,int x,int y, Texture wep1, Texture wep2, Texture wep3, Texture wep4, Texture wep5, Texture wep6) {
        this.image = img;
        this.y = y;
        this.x = x;
        width = 28;
        height = 28;

        weapons[0] = wep1;
         weapons[1] = wep2;
         weapons[2] = wep3;
         weapons[3] = wep4;
         weapons[4] = wep5;
         weapons[5] = wep6;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(image,x,y,width,height);
        batch.draw(weapons[weaponNum - 1],x,y,width,height);
    }
}
