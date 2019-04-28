package com.mygdx.game2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

/**
 * Created by kyle on 9/16/2015.
 */
public class SwordsmanBoss extends Enemy implements Boss {
    boolean facingRight = false; // if facing = false player is facing left, if facing = true player = facing right
    int x = 0;
    int y = 0;
    int width;
    int height;
    float velX;
    float velY;
    int health;
    static final int MAX_HEALTH = 350;
    boolean destroyed = false;
    boolean flying = false;

    boolean isVisible;

    Texture standingImage = new Texture("SPA/Enemy/Boss/ShadowBoss/Idle/1.png");
    Texture fallingImage = new Texture("SPA/Enemy/Boss/ShadowBoss/Fall/Fall.png");

    Texture[] runningImages = new Texture[10];
    Texture run1 = new Texture("SPA/Enemy/Boss/ShadowBoss/Run/1.png");
    Texture run2 = new Texture("SPA/Enemy/Boss/ShadowBoss/Run/2.png");
    Texture run3 = new Texture("SPA/Enemy/Boss/ShadowBoss/Run/3.png");
    Texture run4 = new Texture("SPA/Enemy/Boss/ShadowBoss/Run/4.png");
    Texture run5 = new Texture("SPA/Enemy/Boss/ShadowBoss/Run/5.png");
    Texture run6 = new Texture("SPA/Enemy/Boss/ShadowBoss/Run/6.png");
    Texture run7 = new Texture("SPA/Enemy/Boss/ShadowBoss/Run/7.png");
    Texture run8 = new Texture("SPA/Enemy/Boss/ShadowBoss/Run/8.png");
    Texture run9 = new Texture("SPA/Enemy/Boss/ShadowBoss/Run/9.png");
    Texture run10 = new Texture("SPA/Enemy/Boss/ShadowBoss/Run/10.png");

    Texture[] teleportImages = new Texture[9];
    Texture tp1 = new Texture("SPA/Enemy/Boss/ShadowBoss/Tele/1.png");
    Texture tp2 = new Texture("SPA/Enemy/Boss/ShadowBoss/Tele/2.png");
    Texture tp3 = new Texture("SPA/Enemy/Boss/ShadowBoss/Tele/3.png");
    Texture tp4 = new Texture("SPA/Enemy/Boss/ShadowBoss/Tele/4.png");
    Texture tp5 = new Texture("SPA/Enemy/Boss/ShadowBoss/Tele/5.png");
    Texture tp6 = new Texture("SPA/Enemy/Boss/ShadowBoss/Tele/6.png");
    Texture tp7 = new Texture("SPA/Enemy/Boss/ShadowBoss/Tele/7.png");
    Texture tp8 = new Texture("SPA/Enemy/Boss/ShadowBoss/Tele/8.png");
    Texture tp9 = new Texture("SPA/Enemy/Boss/ShadowBoss/Tele/9.png");

    Texture[] appearImages = new Texture[11];
    Texture tpa1 = new Texture("SPA/Enemy/Boss/ShadowBoss/Tele/Appear/1.png");
    Texture tpa2 = new Texture("SPA/Enemy/Boss/ShadowBoss/Tele/Appear/2.png");
    Texture tpa3 = new Texture("SPA/Enemy/Boss/ShadowBoss/Tele/Appear/3.png");
    Texture tpa4 = new Texture("SPA/Enemy/Boss/ShadowBoss/Tele/Appear/4.png");
    Texture tpa5 = new Texture("SPA/Enemy/Boss/ShadowBoss/Tele/Appear/5.png");
    Texture tpa6 = new Texture("SPA/Enemy/Boss/ShadowBoss/Tele/Appear/6.png");
    Texture tpa7 = new Texture("SPA/Enemy/Boss/ShadowBoss/Tele/Appear/7.png");
    Texture tpa8 = new Texture("SPA/Enemy/Boss/ShadowBoss/Tele/Appear/8.png");
    Texture tpa9 = new Texture("SPA/Enemy/Boss/ShadowBoss/Tele/Appear/9.png");
    Texture tpa10 = new Texture("SPA/Enemy/Boss/ShadowBoss/Tele/Appear/10.png");
    Texture tpa11 = new Texture("SPA/Enemy/Boss/ShadowBoss/Tele/Appear/11.png");


    boolean turnRectCollided;
    Rectangle turnAroundRect = new Rectangle(0,0,25,25); // rectangle that is 1 robot width to the direction the robot is moving and 1 robot width down
    //if it is not intersecting any blocks then the robot will fall off edge soon so turn around

    Random random = new Random();

    boolean canJump = false;

    int bulletSpawnCounter = 0;
    final int bullet1SpawnTime = 8;

    int xDist; // distance from block to character x plane
    int yDist; // distance from block to character y plane
    int drawDist = 1000; // max distance from player that this should still be drawn and updated
    boolean isActive; // boolean to tell whether it is too far away and should be drawn



    Boolean touchingGround = false;
    Boolean touchingCeiling = false;
    Rectangle rect = new Rectangle();


    //ATTACK STUFF ////////////////////////////////////////////////////////////////////////////
    // A1 = horizontal slash
    // A2 = horizontal dash
    // A3 = vertical upward dash
    // A4 = downward vertical slash
    // A5 = sword blast
    // AW = air walk spell

    boolean isPreparingAttack; // active if any of the preparing bools are active
    boolean isAttacking; // active if any of the using bools are active (not including air walk)

    int attackCooldownTime; // cool down timer between attacks, starts as set to 180, when hits 0 the character can attack again

    int currentAttack; // 0 = none, 1 = a1, etc

    boolean[] preparingAttacksArray; // 1 = a1, 2 = a2, etc etc
    boolean[] usingAttacksArray;
    int[] prepAttackTimeArray;
    int[] usingAttackTimeArray;



    boolean preparingAW;
    boolean isAirWalking;
    boolean isMoving;

    int timeUntilNextMovementImage;
    int maxTimeUntilNextMovementImage = 5;
    int currMoveImage;

    int prepAWTime;
    int usingAWTime;

    String type;




    public String getType() {
        return type;
    }


    public SwordsmanBoss(int x, int y) {
        health = MAX_HEALTH;
        this.x = x;
        this.y = y;
        this.width = 24;
        this.height = 36;

        this.type = "SwordsmanBoss";


        this.isVisible = true;


        preparingAttacksArray = new boolean[5];
        usingAttacksArray = new boolean[5];
        prepAttackTimeArray = new int[5];
        usingAttackTimeArray = new int[5];
        for (int i = 0; i < 5; i++) {
            preparingAttacksArray[i] = false;
            usingAttacksArray[i] = false;
            prepAttackTimeArray[i] = 0;
            usingAttackTimeArray[i] = 0;
        }

        attackCooldownTime = 10;

        preparingAW = false;
        isAirWalking = false;
        prepAWTime = 0;
        usingAWTime = 0;

        runningImages[0] = run1;
        runningImages[1] = run2;
        runningImages[2] = run3;
        runningImages[3] = run4;
        runningImages[4] = run5;
        runningImages[5] = run6;
        runningImages[6] = run7;
        runningImages[7] = run8;
        runningImages[8] = run9;
        runningImages[9] = run10;

        teleportImages[0] = tp1;
        teleportImages[1] = tp2;
        teleportImages[2] = tp3;
        teleportImages[3] = tp4;
        teleportImages[4] = tp5;
        teleportImages[5] = tp6;
        teleportImages[6] = tp7;
        teleportImages[7] = tp8;
        teleportImages[8] = tp9;

        appearImages[0] = tpa1;
        appearImages[1] = tpa2;
        appearImages[2] = tpa3;
        appearImages[3] = tpa4;
        appearImages[4] = tpa5;
        appearImages[5] = tpa6;
        appearImages[6] = tpa7;
        appearImages[7] = tpa8;
        appearImages[8] = tpa9;
        appearImages[9] = tpa10;
        appearImages[10] = tpa11;

        currMoveImage = 0;
        timeUntilNextMovementImage = maxTimeUntilNextMovementImage;
        isMoving = false;



    }

    public Rectangle getRect() {
        return rect;
    }

    public void changeHealth(int addToHealth) {
        health += addToHealth;
    }

    public float getHealthPercentage() {
        return ((float)health / (float)MAX_HEALTH) * 100;
    }

    public void update(Array<Enemy> enemies, Array<EnemyBullet> enemyBullets, Array<PlayerBullet> bullets,Array<Item> items, Array<BaseTile> baseTiles, Array<Particle> particle1s, int playerX, int playerY) {
        xDist = Math.abs(playerX - this.x);
        yDist = Math.abs(playerY - this.y);
        touchingGround = false;


        if (Math.sqrt((xDist * xDist) + (yDist * yDist)) > drawDist) isActive = false;
        else isActive = true;

        isMoving = (Math.abs(velX) > 1);

        if (isMoving) {
            timeUntilNextMovementImage --;
            if (timeUntilNextMovementImage <= 0) {
                timeUntilNextMovementImage = maxTimeUntilNextMovementImage;
                currMoveImage++;
                if (currMoveImage > 9) {
                    currMoveImage = 0;
                }
            }
        } else {
            timeUntilNextMovementImage = maxTimeUntilNextMovementImage;
            currMoveImage = 0;
        }

        if (isActive) {
            checkIsAttacking();
            checkIsPreparingToAttack();

            if (!isAttacking && !isPreparingAttack) {
                moveThis(playerX);
            } else {
                velX = 0;
            }
            handleAttacks(enemyBullets);
            if (health <= 0) {
                destroyThis(particle1s, items);
            }
            if (velY < 15)  {
                velY -= .3;
            }
            if (preparingAW || isAirWalking || preparingAttacksArray[4] || usingAttacksArray[4] || usingAttacksArray[2]) {
                velY = 0;
            }
            if (velX > 0) facingRight = true;
            else if (velX < 0) facingRight = false;

            x += velX;
            y += velY;

            rect.x = x;
            rect.y = y;
            rect.width = width;
            rect.height = height;
            checkTilesHit(baseTiles);
        }
    }


    public void handleAttacks(Array<EnemyBullet> enemyBullets) {
        if (!isAttacking && !isPreparingAttack) {
            attackCooldownTime --;
            if (attackCooldownTime < 0) {
                if (!isAirWalking) {
                    currentAttack = random.nextInt(3) + 1;

                } else {
                    currentAttack = 4;
                }

                preparingAttacksArray[currentAttack - 1] = true;
                if (currentAttack != 3) {
                    prepAttackTimeArray[currentAttack - 1] = 10;
                } else if (currentAttack == 3) {
                    prepAttackTimeArray[currentAttack - 1] = 20;
                }
            }
        }

        if (isPreparingAttack) {
            if (preparingAW) {
                prepAWTime --;
                if (prepAWTime < 0) {
                    preparingAW = false;
                    isAirWalking = true;
                    usingAWTime = 240;
                    attackCooldownTime = 60;
                }
            } else {
                prepAttackTimeArray[currentAttack - 1]--;
                if (prepAttackTimeArray[currentAttack - 1] < 0) {
                    preparingAttacksArray[currentAttack - 1] = false;
                    usingAttacksArray[currentAttack - 1] = true;
                    if (currentAttack == 5) {
                        usingAttackTimeArray[currentAttack - 1] = 40;
                    } else if (currentAttack == 3) {
                        usingAttackTimeArray[currentAttack - 1] = 50;
                        isVisible = false;
                    } else if (currentAttack == 2) {
                        usingAttackTimeArray[currentAttack-1] = 20;
                    } else {
                        usingAttackTimeArray[currentAttack - 1] = 10;
                    }

                }
            }
        }

        if (isAirWalking) {
            usingAWTime --;
            if (usingAWTime < 0) {
                isAirWalking = false;
            }
        }

        if (isAttacking) {
            usingAttackTimeArray[currentAttack - 1] --;
            if (currentAttack == 1) {
                handleA1Attack(enemyBullets);
            } else if (currentAttack == 2) {
                handleA2Attack(enemyBullets);
            } else if (currentAttack == 3) {
                handleA3Attack(enemyBullets);
            } else if (currentAttack == 4) {
                handleA4Attack(enemyBullets);
            } else if (currentAttack == 5) {
                handleA5Attack(enemyBullets);
            }

            if (usingAttackTimeArray[currentAttack - 1] < 0) {
                if (currentAttack == 3) {
                    createAirAttack();
                } else {
                    usingAttacksArray[currentAttack - 1] = false;
                    if (currentAttack == 4) {
                        attackCooldownTime = 60;
                    } else {
                        attackCooldownTime = 80 + random.nextInt(30);
                    }
                }
            }
        }
    }

    public void handleA1Attack(Array<EnemyBullet> enemyBullets) {
        if (usingAttackTimeArray[0] == 9) {
            // spawn sword slash shockwave here
            System.out.println("on making shot");
            SBSwordSlash attack = new SBSwordSlash(this.x,this.y, !facingRight, false);
            enemyBullets.add(attack);
        }
    }

    public void handleA2Attack(Array<EnemyBullet> enemyBullets) {
        if (usingAttackTimeArray[1] == 19) {
            SBHomingBullet attack = new SBHomingBullet(this.x, this.y, 0, 4);
            enemyBullets.add(attack);
            //SBSwordDash attack = new SBSwordDash(this.x, this.y, !facingRight, false, this);
            //enemyBullets.add(attack);
        }

        if (usingAttackTimeArray[1] == 10) {
            SBHomingBullet attack = new SBHomingBullet(this.x,this.y, 3,3);
            enemyBullets.add(attack);
        }

        if (usingAttackTimeArray[1] == 1) {
            SBHomingBullet attack = new SBHomingBullet(this.x,this.y, -3,3);
            enemyBullets.add(attack);
        }
    }

    public void handleA3Attack(Array<EnemyBullet> enemyBullets) {
        if (usingAttackTimeArray[2] == 49) {
            this.y += 280;
        }
        if (usingAttackTimeArray[2] == 23) {
            this.isVisible = true;
        }
    }

    public void handleA4Attack(Array<EnemyBullet> enemyBullets) {
        if (usingAttackTimeArray[3] == 9) {
            SBAngledSlash attack1 = new SBAngledSlash(this.x, this.y, (float)(0.75*(Math.PI)));
            enemyBullets.add(attack1);
            SBAngledSlash attack2 = new SBAngledSlash(this.x, this.y, (float)(1*Math.PI));
            enemyBullets.add(attack2);
            SBAngledSlash attack3 = new SBAngledSlash(this.x, this.y, (float)(1.25*(Math.PI)));
            enemyBullets.add(attack3);
        }

    }

    public void handleA5Attack(Array<EnemyBullet> enemyBullets) {
        if (usingAttackTimeArray[4] == 39) {
            SBAngledSlash attack1 = new SBAngledSlash(this.x, this.y, 0);
            enemyBullets.add(attack1);
            SBAngledSlash attack2 = new SBAngledSlash(this.x, this.y, (float)(Math.PI));
            enemyBullets.add(attack2);
            SBAngledSlash attack3 = new SBAngledSlash(this.x, this.y, (float)(1.5*(Math.PI)));
            enemyBullets.add(attack3);
            SBAngledSlash attack4 = new SBAngledSlash(this.x, this.y, (float)(0.5*(Math.PI)));
            enemyBullets.add(attack4);
            SBAngledSlash attack5 = new SBAngledSlash(this.x, this.y, (float)(0.25*(Math.PI)));
            enemyBullets.add(attack5);
            SBAngledSlash attack6 = new SBAngledSlash(this.x, this.y, (float)(0.75*(Math.PI)));
            enemyBullets.add(attack6);
            SBAngledSlash attack7 = new SBAngledSlash(this.x, this.y, (float)(1.25*(Math.PI)));
            enemyBullets.add(attack7);
            SBAngledSlash attack8 = new SBAngledSlash(this.x, this.y, (float)(1.75*(Math.PI)));
            enemyBullets.add(attack8);
        }
        if (usingAttackTimeArray[4] == 9) {
            SBAngledSlash attack1 = new SBAngledSlash(this.x, this.y, 0.125f);
            enemyBullets.add(attack1);
            SBAngledSlash attack2 = new SBAngledSlash(this.x, this.y, (float)(Math.PI + 0.125));
            enemyBullets.add(attack2);
            SBAngledSlash attack3 = new SBAngledSlash(this.x, this.y, (float)(1.5*(Math.PI) + 0.125));
            enemyBullets.add(attack3);
            SBAngledSlash attack4 = new SBAngledSlash(this.x, this.y, (float)(0.5*(Math.PI) + 0.125));
            enemyBullets.add(attack4);
            SBAngledSlash attack5 = new SBAngledSlash(this.x, this.y, (float)(0.25*(Math.PI) + 0.125));
            enemyBullets.add(attack5);
            SBAngledSlash attack6 = new SBAngledSlash(this.x, this.y, (float)(0.75*(Math.PI) + 0.125));
            enemyBullets.add(attack6);
            SBAngledSlash attack7 = new SBAngledSlash(this.x, this.y, (float)(1.25*(Math.PI) + 0.125));
            enemyBullets.add(attack7);
            SBAngledSlash attack8 = new SBAngledSlash(this.x, this.y, (float)(1.75*(Math.PI) + 0.125));
            enemyBullets.add(attack8);
        }
    }

    public void createAirAttack() {
        usingAttacksArray[currentAttack - 1] = false;
        attackCooldownTime = -1;
        usingAttacksArray[currentAttack - 1] = false;
        int airAttackType = random.nextInt(2) + 1;
        if (airAttackType == 1) {
            preparingAW = true;
            prepAWTime = 10;
        } else {
            preparingAttacksArray[4] = true;
            prepAttackTimeArray[4] = 10;
            currentAttack = 5;
        }
    }




    public void checkIsAttacking() {
        if (usingAttacksArray[0] || usingAttacksArray[1] || usingAttacksArray[2] || usingAttacksArray[3] || usingAttacksArray[4]) {
            isAttacking = true;
        } else {
            isAttacking = false;
        }
    }

    public void checkIsPreparingToAttack() {
        if (preparingAttacksArray[0] || preparingAttacksArray[1] || preparingAttacksArray[2] || preparingAttacksArray[3] || preparingAttacksArray[4] || preparingAW) {
            isPreparingAttack = true;
        } else {
            isPreparingAttack = false;
        }
    }

    public void draw(SpriteBatch batch) {
        if (isVisible) {
            if (isPreparingAttack && currentAttack == 3) {
                int tpImageNum = 8 - ((prepAttackTimeArray[currentAttack - 1] / 2) - 1);
                if (tpImageNum < 0) {
                    tpImageNum = 0;
                }
                if (tpImageNum > 8) {
                    tpImageNum = 8;
                }
                batch.draw(teleportImages[tpImageNum], x, y, width, height, 0, 0, 13, 18, !facingRight, false);
            } else if (isAttacking && currentAttack == 3) {
                int tpAppearImageNum = 10 - ((usingAttackTimeArray[currentAttack - 1] / 2) - 1);
                if (tpAppearImageNum < 0) {
                    tpAppearImageNum = 0;
                }
                if (tpAppearImageNum > 10) {
                    tpAppearImageNum = 10;
                }
                batch.draw(appearImages[tpAppearImageNum], x, y, width, height, 0, 0, 13, 18, !facingRight, false);

            }   else if (touchingGround) {
                if (!isMoving) {
                    batch.draw(standingImage, x, y, width, height, 0, 0, 13, 18, !facingRight, false);
                } else {
                    batch.draw(runningImages[currMoveImage], x, y, width, height, 0, 0, 13, 18, !facingRight, false);
                }
            } else {
                batch.draw(fallingImage, x, y, width, height, 0, 0, 13, 18, !facingRight, false);
            }
        }

    }

    public void destroyThis(Array<Particle> particle1s, Array<Item> items) {

        destroyed = true;

        HealthCrystal hp = new HealthCrystal(this.x,
                this.y,
                -velX + random.nextInt(3) - 1,
                3 + random.nextInt(3) - 1,
                10,12);
        items.add(hp);

        ManaCrystal mp = new ManaCrystal(this.x,
                this.y,
                -velX + random.nextInt(3) - 1,
                3 + random.nextInt(3) - 1,
                10,12);
        items.add(mp);

        int numCoins = random.nextInt(3); // spawn between 0 and 2 coins

        System.out.print(numCoins);

        if (numCoins >= 1) {
            BasicCoin coin = new BasicCoin(this.x,
                    this.y,
                    random.nextInt(5) - 2,
                    3 + random.nextInt(3) - 1,
                    10, 12);
            items.add(coin);
        }

        if (numCoins >= 2) {
            BasicCoin coin2 = new BasicCoin(this.x,
                    this.y,
                    random.nextInt(5) - 2,
                    3 + random.nextInt(3) - 1,
                    10, 12);
            items.add(coin2);
        }
    }




    public boolean getDestroyed() {
        return destroyed;
    }

    private void moveThis(int playerX) {

        if (playerX > this.x) {
            velX += .5;

        } else if (playerX < this.x) {
            velX -= .5;
        }

        if (!isAirWalking) {
            if (velX > 2) velX = 2;
            else if (velX < -2) velX = -2;
        } else {
            if (velX > 4) velX = 4;
            else if (velX < -4) velX = -4;
        }
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
                        canJump = true;
                        touchingGround = true;
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
