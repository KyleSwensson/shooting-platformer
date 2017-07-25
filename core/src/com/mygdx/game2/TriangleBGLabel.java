package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Created by kyles on 12/2/2016.
 */
public class TriangleBGLabel extends Item {

    Texture image = new Texture("SPA/Decoration/triangleLabel.png");


    public TriangleBGLabel(int x, int y, float velX, float velY, int width, int height) {
        super(x,y,velX, velY, width,height);

        this.width = 128;
        this.height = 128;
    }

    public String getType() {
        return this.type;
    }

    public  void update(Array<BaseTile> tiles, Rectangle playerRect) {

    }

    public void draw(SpriteBatch batch) {
        batch.draw(image,x,y,width,height);
    }



}
