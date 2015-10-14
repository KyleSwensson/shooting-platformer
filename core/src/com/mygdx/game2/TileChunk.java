package com.mygdx.game2;

/**
 * Created by kyle on 9/22/2015.
 */
public class TileChunk {
    // an object that contains an array of tiles 5x5 large
    int[][] tiles = new int[5][5];
    int posX = 0; // the chunks x position in relation to other tiles
    int posY = 0; // the chunks y position in relation to other tiles


    public TileChunk(String type, int givenX, int givenY) {

        posX = givenX;
        posY = givenY;
        if (type.equals("Blank")) {
            for (int i = 0; i<tiles.length; i++) {
                for (int p = 0; p < tiles[0].length; p++) {
                    tiles[i][p] = 0;
                }
            }
        } else if (type.equals("Filled")) {
            for (int i = 0; i<tiles.length; i++) {
                for (int p = 0; p < tiles[0].length; p++) {
                    tiles[i][p] = 1;
                }
            }
        }
    }
}
