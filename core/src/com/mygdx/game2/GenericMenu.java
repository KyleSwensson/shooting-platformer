
package com.mygdx.game2;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by kyles on 8/2/2016.
 */
public abstract class GenericMenu {
    //Menus have a few properties:
    //-You can't control the player when they are open
    //-They have one or more clickable buttons
    public int width;
    public int height;
    public int x;
    public int y;


    public abstract void draw(SpriteBatch batch);
}
