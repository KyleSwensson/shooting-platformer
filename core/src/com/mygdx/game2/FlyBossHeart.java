package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by kyle on 10/16/2015.
 */
public class FlyBossHeart extends Enemy implements Boss {


    int xDist; // distance from block to character x plane
    int yDist; // distance from block to character y plane
    int drawDist = 500; // max distance from player that this should still be drawn and updated
    boolean isActive; // boolean to tell whether it is too far away and should be drawn
    Texture crystalImage = new Texture("topazCrystalCenter.png");
    Texture imageBroken1 = new Texture("crystalBroken1.png");
    Texture imageBroken2 = new Texture("crystalBroken2.png");
    Texture imageBroken3 = new Texture("crystalBroken3.png");


    int numAliveSubCrystals;


    boolean isInvincible; // this starts as true, becomes false when all 4 sub crystals are dead


    ArrayList<SubCrystalBoss> subCrystals;

    int timeUntilAttack = 300;

    Random random = new Random();

    public FlyBossHeart(int x, int y) {

        health = 40;
        width = 106;
        height = 102;

        this.x = x - width/2;
        this.y = y;
        enemyType = "FlyBoss";
        isInvincible = true;
        subCrystals = new ArrayList<SubCrystalBoss>();
        numAliveSubCrystals = 4;
    }

    public void addSubCrystal(SubCrystalBoss sub) {
        subCrystals.add(sub);
        sub.setHeart(this);
    }

    public Rectangle getRect() {
        return rect;
    }

    public void notifyOfDeath(SubCrystalBoss sub) {

        numAliveSubCrystals --;
        subCrystals.remove(sub);
    }

    public void update(Array<Enemy> enemies,Array<EnemyBullet> enemyBullets, Array<PlayerBullet> bullets,Array<Item> items, Array<BaseTile> baseTiles, Array<Particle> particle1s, int playerX, int playerY) {
        xDist = (int)Math.abs(playerX - this.x);
        yDist = (int)Math.abs(playerY - this.y);
        //if (Math.sqrt((xDist * xDist) + (yDist * yDist)) > drawDist) isActive = false;
        //else isActive = true;
        isActive = true;



        if (isActive) {

            //moveThis(playerX, playerY);


            if (health <= 0) {
                destroyThis(particle1s, items);
            }
            //x += velX;
            //y += velY;

            rect.x = x;
            rect.y = y;
            rect.width = width;
            rect.height = height;

            checkTilesHit(baseTiles);

            if (isInvincible) {
                if (timeUntilAttack > 0) {
                    timeUntilAttack--;
                } else {
                    int attackType = random.nextInt(2);
                    if (attackType == 0) {
                        int crystalToAttack = random.nextInt(numAliveSubCrystals) + 1;
                        int i = 1;
                        for (SubCrystalBoss sub : subCrystals) {
                            if (i == crystalToAttack) {
                                sub.notifyOfAttack(1);
                            }

                            i++;
                        }
                        timeUntilAttack = 180;
                    } else if (attackType == 1) {
                        if (numAliveSubCrystals > 1) {
                            int crystal1ToAttack = random.nextInt(numAliveSubCrystals) + 1;
                            int crystal2ToAttack = crystal1ToAttack;
                            while (crystal1ToAttack == crystal2ToAttack) {
                                crystal2ToAttack = random.nextInt(numAliveSubCrystals) + 1;
                            }

                            SubCrystalBoss giveToCrystal1 = null;
                            int i = 1;
                            for (SubCrystalBoss sub : subCrystals) {
                                if (i == crystal2ToAttack) {
                                    giveToCrystal1 = sub;
                                }

                                i++;
                            }

                            i = 1;
                            for (SubCrystalBoss sub : subCrystals) {
                                if (i == crystal1ToAttack) {
                                    sub.notifyOfAttack(2, giveToCrystal1);
                                }
                                //if (sub.cornerNumber == crystal1ToAttack) {
                                //    sub.notifyOfAttack(2, giveToCrystal1);
                                //}
                                i++;
                            }
                        }
                    }
                }
            }

        }
    }

    public void draw(SpriteBatch batch) {
        if (isInvincible) {
            batch.draw(crystalImage,x,y,width,height);

        } else {
            if (health > 39) {
                batch.draw(imageBroken1, x, y, width, height);
            } else if (health > 29) {
                batch.draw(imageBroken2,x,y,width,height);
            } else {
                batch.draw(imageBroken3,x,y,width,height);
            }
        }
    }

    private void destroyThis(Array<Particle> particle1s, Array<Item> items) {
        destroyed = true;
        //TODO: other things that hap when boss dies
    }

    private void moveThis(int playerX, int playerY) {
        if (playerX > this.x) velX += .02;
        else if (playerX < this.x) velX -= .02;

        if (velX > 1) velX = 1;
        else if (velX < -1) velX = -1;

        if (playerY > this.y) velY += .02;
        if (playerY < this.y) velY -= .02;
        if (velY > 1) velY = 1;
        if (velY < -1) velY = -1;
    }


    public void changeHealth(int addToHealth) {

        if (!isInvincible) {
            health += addToHealth;
        }
    }

    public boolean getDestroyed() {
        return destroyed;
    }

    public void checkTilesHit(Array<BaseTile> baseTiles) {
        for (BaseTile tile : baseTiles) {
            if (tile.isActive) {
                if (rect.overlaps(tile.rect)) {

                    float rect1Top = rect.y + rect.height;
                    float rect1Bot = rect.y;
                    float rect1Left = rect.x;
                    float rect1Right = rect.x + rect.width;
                    float rect2Top = tile.rect.y + tile.rect.height;
                    float rect2Bot = tile.rect.y;
                    float rect2Left = tile.rect.x;
                    float rect2Right = tile.rect.x + tile.rect.width;
                    float leftPassedDistance = Math.abs(rect2Right - rect1Left);
                    float rightPassedDistance = Math.abs(rect1Right - rect2Left);
                    float topPassedDistance = Math.abs(rect1Top - rect2Bot);
                    float bottomPassedDistance = Math.abs(rect2Top - rect1Bot);
                    String directionLeastPassed;
                    if (leftPassedDistance < rightPassedDistance &&
                            leftPassedDistance < topPassedDistance &&
                            leftPassedDistance < bottomPassedDistance) directionLeastPassed = "Left";
                    else if (rightPassedDistance < topPassedDistance &&
                            rightPassedDistance < bottomPassedDistance) directionLeastPassed = "Right";
                    else if (topPassedDistance < bottomPassedDistance) directionLeastPassed = "Top";
                    else directionLeastPassed = "Bottom";


                    if (directionLeastPassed.equals("Bottom")) {
                        rect.y = rect2Top;
                        velY = 0;

                    } else if (directionLeastPassed.equals("Top")) {
                        rect.y = rect2Bot - rect.height;
                        velY = 0;
                    } else if (directionLeastPassed.equals("Left")) {
                        rect.x = rect2Right;
                        velX = 0;

                    } else {
                        rect.x = rect2Left - rect.width;
                        velX = 0;

                    }
                }
                y = (int) rect.y;
                x = (int) rect.x;

            }
        }
    }
}
