package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Created by kyles on 10/16/2016.
 */
public class SubCrystalBoss extends Enemy {


    int xDist; // distance from block to character x plane
    int yDist; // distance from block to character y plane
    int drawDist = 1000; // max distance from player that this should still be drawn and updated
    boolean isActive; // boolean to tell whether it is too far away and should be drawn
    Texture imageR = new Texture("sidecrystalRedAlive.png");
    Texture imageB = new Texture("sidecrystalBlueAlive.png");
    Texture imageG = new Texture("sidecrystalGreenAlive.png");
    Texture imageP = new Texture("sideCrystalPurpleAlive.png");

    SubCrystalBoss attackPartnerCrystal;

    Texture whiteFlashImage = new Texture("sideCrystalFlash.png");

    FlyBossHeart bh;

    boolean isAttacking; // false or true based on whether this gem is currently attacking
    int attackType; // 0 = no attack, 1 = attack 1...


    int playerWidth = 12;
    int playerHeight = 18;
    int xCenter;
    int yCenter;

    boolean isOnBlink; // says whether to draw normal sprite or blinking sprite
    boolean a1ShootingYet;
    int a1BlinkTimer;
    int a1ShotTimer;

    boolean a2ShootingYet;
    int a2ShotTimer;

    int cornerNumber; // number that says which corner this gem is in, 1 is top left, 2 is top right, 3 is bottom left, 4 is bottom right



    public SubCrystalBoss(int x, int y, int cornerNumber) {

        this.cornerNumber = cornerNumber;
        health = 150;
        width = 64;
        height = 64;

        this.x = x - width/2;
        this.y = y;

        this.xCenter = (int)this.x + width / 2;
        this.yCenter = (int)this.y + height / 2;
        enemyType = "SubCrystalBoss";
        isAttacking = false;
        attackType = 0;
        isOnBlink = false;

        a1ShotTimer = -1;
        a2ShotTimer = -1;

    }

    public void setHeart(FlyBossHeart bh) {
        this.bh = bh;
    }


    public void notifyOfAttack(int attackType) {
        this.isAttacking = true;
        this.attackType = attackType;
    }

    public void notifyOfAttack(int attackType, SubCrystalBoss partnerCrystal) {
        this.isAttacking = true;
        this.attackType = attackType;
        this.attackPartnerCrystal = partnerCrystal;

        if (attackType == 2) {
            a2ShootingYet = true;
        }
    }

    public void changeHealth(int addToHealth) {
        health += addToHealth;
    }

    public boolean getDestroyed() {
        return destroyed;
    }

    private void destroyThis(Array<Particle> particle1s, Array<Item> items) {
        destroyed = true;
        bh.notifyOfDeath(this);
        //TODO: other things that hap when boss dies
    }

    public Rectangle getRect() {
        return rect;
    }



    public void draw(SpriteBatch batch) {

        if (!isOnBlink && !a1ShootingYet) {
            Texture image;
            if (cornerNumber == 1) {
                image = imageB;
            } else if (cornerNumber == 2) {
                image = imageR;
            } else if (cornerNumber == 3) {
                image = imageG;
            } else {
                image = imageP;
            }

            batch.draw(image,x,y,width,height);
        } else {
            batch.draw(whiteFlashImage,x,y,width,height);
        }
    }

    public void update(Array<Enemy> enemies, Array<EnemyBullet> enemyBullets, Array<PlayerBullet> bullets, Array<Item> items, Array<BaseTile> baseTiles, Array<Particle> particle1s, int playerX, int playerY) {
        xDist = (int)Math.abs(playerX - this.x);
        yDist = (int)Math.abs(playerY - this.y);

        isActive = true;



        if (isActive) {

            //moveThis(playerX, playerY);
            if (isAttacking) {
                if (attackType == 1) {
                    if (!a1ShootingYet) {
                        if (a1BlinkTimer == -1) {
                            a1BlinkTimer = 180;
                        }
                         a1BlinkTimer--;
                        if (a1BlinkTimer == 80 || a1BlinkTimer == 160) {
                            isOnBlink = true;
                        }
                        if (a1BlinkTimer == 60 || a1BlinkTimer == 140) {
                            isOnBlink = false;
                        }
                        if (a1BlinkTimer == 0) {
                            a1BlinkTimer = -1;
                            a1ShootingYet = true;
                        }

                    } else {
                        if (a1ShotTimer == -1) {
                            a1ShotTimer = 90;
                        }
                        a1ShotTimer --;
                        if (a1ShotTimer == 18 || a1ShotTimer == 36 || a1ShotTimer == 54 || a1ShotTimer == 72 || a1ShotTimer == 1) {
                            //TODO: make a shot here
                            CrystalShardBullet bullet = new CrystalShardBullet((int)this.x + (this.width / 2), (int)this.y + (this.height / 2), playerX + (playerWidth / 2), playerY + (playerHeight / 2));
                            enemyBullets.add(bullet);
                        }
                        if (a1ShotTimer == 0) {
                            a1ShotTimer = -1;
                            a1ShootingYet = false;
                            isAttacking = false;
                            attackType = 0;
                        }
                    }


                } else if (attackType == 2) {
                    if (a2ShootingYet) {

                        if (a2ShotTimer == -1) {
                            a2ShotTimer = 180;
                        }
                        a2ShotTimer--;
                        if (a2ShotTimer == 179) {
                            float distX = this.xCenter - attackPartnerCrystal.xCenter;
                            float distY = this.yCenter - attackPartnerCrystal.yCenter;
                            float distH = (float)Math.sqrt((distX*distX) + (distY*distY));
                            float angleBetweenCrystals = (float)Math.asin(distX / distH);
                            float scaleX;
                            //if (distX > 0) {
                              //  scaleX = (float)Math.sin(angleBetweenCrystals);
                            //} else {
                                scaleX = -(float)Math.sin(angleBetweenCrystals);
                           // }
                            float scaleY;
                            if (distY > 0) {//this is above the other crystal
                                scaleY = -(float)Math.cos(angleBetweenCrystals);
                            } else {
                                scaleY = (float)Math.cos(angleBetweenCrystals);
                            }
                            for (int i = 0; i < 50; i++) {
                                float distFromCrystal = random.nextFloat() * distH;
                                CrystalLaserSpeck laserPart = new CrystalLaserSpeck((int)(this.xCenter + (distFromCrystal*scaleX)), (int)(this.yCenter + (distFromCrystal*scaleY)), angleBetweenCrystals);
                                enemyBullets.add(laserPart);
                            }
                        }
                        if (a2ShotTimer == 0) {
                            a2ShotTimer = -1;
                            a2ShootingYet = false;
                            isAttacking = false;
                            attackType = 0;
                        }
                    }
                }
            }

            if (health <= 0) {
                destroyThis(particle1s, items);
            }

            rect.x = x;
            rect.y = y;
            rect.width = width;
            rect.height = height;

        }
    }
}
