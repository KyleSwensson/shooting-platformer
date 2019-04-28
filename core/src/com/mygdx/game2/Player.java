package com.mygdx.game2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

/**
 * Created by kyle on 9/16/2015.
 */
public class Player extends Character {

    private final static double GRAVITY_ACCELERATION = 0.4;

    boolean facingRight = false; // if facing = false player is facing left, if facing = true player = facing right
    float x = 0;
    float y = 0;

    int[] heldWeapons = new int[10]; // array telling the game which weapons you are capable of using, you start with only the pistol unlocked
    // 0 is empty (no unlock)
    // 1 is a pistol
    // 2 is a cannon
    // 3 is machine gun
    // 4 is flamethrower
    // 5 is rocket launcher
    // 6 is grenades

    //entire array will shift downwards or upwards (not including 0 part) when weapons are rotated (like a looping array), active weapon is the weapon in spot [0]
    // to clarify if the next spot is 0 the current item will go to the front of the array instead of pushing the next spot up

    //TODO: implement looping of array
    //TODO: implement drawing correct weapons based on array
    //TODO: make it so player cannot select weapons he doesn't own
    //TODO: make it so pickups unlock weapons

    static final float GRAPPLE_PULL_VELOCITY = 14;
    static final float GRAPPLE_HOOK_VELOCITY = 40;
    static final float GRAPPLE_HOOK_DIAGONAL_VELX = (float)Math.sqrt((GRAPPLE_HOOK_VELOCITY*GRAPPLE_HOOK_VELOCITY)/2);
    static final float GRAPPLE_HOOK_DIAGONAL_VELY = (float)Math.sqrt((GRAPPLE_HOOK_VELOCITY*GRAPPLE_HOOK_VELOCITY)/2);


    //tells whether a grappling hook is currently travelling through the air, if it is then another grappling hook should not be shot
    boolean grappleHookTravelling;
    //tells whether a grappling hook has landed and is currently pulling the player
    boolean beingPulledByGrapplingHook;
    //the location that the player is being pulled to by the grappling hook
    float grapplingHookX;
    float grapplingHookY;

    GrappleHook activeGrapplingHook;

    // when player goes into a shop his x and y reset for the shop, the oldx and oldy keep track of where the player was when in main game
    float oldX = 0;
    float oldY = 0;

    int fuel = 0; //player has fuel for a floating spell that shoots him up, regains 1 fuel per second, flying costs 3 fuel per second
    int maxFuel = 500;

    int numCoins = 0; // number of coins player holds

    String drawType = "Idle"; // tells the game what animation type to have the player be in
    // types that exist are Jump, Fall, Run, Idle, WallCling, Roll

    int runningImage = 0; // which image the character is on in the running animation
    int runningTimer = 0; // timer counting up to frameChangeTime
    int runningFrameChangeTime = 5; // frames it takes to change to next running image

    int idleImage = 0; // which image the character is on in the running animation
    int idleTimer = 0; // timer counting up to frameChangeTime
    int idleFrameChangeTime = 20; // frames it takes to change to next running image

    float bgXOffset = 0;
    float bgYOffset = 0;

    int flameAmmo;
    int maxFlameAmmo = 200;
    int machineAmmo;
    int maxMachineAmmo = 125;
    int cannonAmmo;
    int maxCannonAmmo = 40;
    int rocketAmmo;
    int maxRocketAmmo = 10;
    int grenadeAmmo;
    int maxGrenadeAmmo = 15;


    int wallSlidingDustFrames = 0; // how many frames the player is sliding down a wall before dust is made
    int wallSlidingDustMax = 5;

    boolean isHit = false; // if it is true that means player is in an animation that makes him invincible for a few seconds after being hit
    int hitTime = 0; // how many frames are left in the animation that the player is hit in

    int width;
    int height;
    float velX;
    float velY;

    boolean gameOver;

    int health;
    int maxHealth = 10;

    Random random = new Random();

    int gunType = 1; // can currently be 1 is normal, 2 is machine, 3 is cannon, 4 is flame

    int shakeFrames = 0;
    Array<BaseTile> collidedTiles = new Array<BaseTile>();

    int bulletSpawnCounter = 0;
    final int bullet1SpawnTime = 16;
    final int flameSpawnTime = 1;
    final int machineSpawnTime = 5;
    final int cannonSpawnTime = 25;
    final int rocketSpawnTime = 50;
    final int grenadeSpawnTime = 35;


    Boolean touchingGround = false;
    Boolean touchingCeiling = false;
    Boolean touchingLeftWall = false;
    int leftWallFrames = 0; // if they hit 0 touchingLeftWall turns false
    Boolean touchingRightWall = false;
    int rightWallFrames = 0; // if they hit 0 touchingRightWall turns false
    Rectangle rect = new Rectangle();

    boolean isRolling;
    boolean isRollingLeft;
    boolean rollCoolingDown;
    int rollTime;
    int rollCooldownTime;
    final int maxRollTime = 25;
    final int maxRollCooldownTime = 15;
    int rollImage = 0;
    int rollFrameChangeTimer = 0;
    final int rollFrameChangeTime = 5;

    public Player() {
        health = maxHealth;
        fuel = maxFuel;
        flameAmmo = maxFlameAmmo;
        machineAmmo = maxMachineAmmo;
        cannonAmmo = maxCannonAmmo;
        rocketAmmo = maxRocketAmmo;
        grenadeAmmo = maxGrenadeAmmo;
    }

    public void update(Array<PlayerBullet> bullets, Array<BaseTile> baseTiles, Array<Particle> particle1s, Array<Enemy> enemies, Array<Animation> anims, Array<Item> items, Array<EnemyBullet> enemyBullets, int mainState) {


        if (health <= 0) {
            health = 0;
            gameOver = true;
        }

        if (drawType.equals("Run")) {
            runningTimer++;
            if (runningTimer >= runningFrameChangeTime) {
                runningTimer = 0;
                runningImage++;
                if (runningImage > 9) runningImage = 0;
            }
        } else {
            runningTimer = 0;
            runningImage = 0;
        }

        if (drawType.equals("Idle")) {
            idleTimer++;
            if (idleTimer >= idleFrameChangeTime) {
                idleTimer = 0;
                idleImage++;
                if (idleImage > 3) idleImage = 0;
            }
        } else {
            idleTimer = 0;
            idleImage = 0;
        }

        if (drawType.equals("Roll")) {
            rollFrameChangeTimer++;
            if (rollFrameChangeTimer >= rollFrameChangeTime) {
                rollFrameChangeTimer = 0;
                rollImage++;
                if (rollImage > 4) {
                    rollImage = 4;
                }
            }
        } else {
            rollFrameChangeTimer = 0;
            rollImage = 0;
        }

        // tells the game what animation type to draw the player in
        if (isRolling) {
            drawType = "Roll";
        } else if (touchingGround) {
            if (Math.abs(velX) >= 2) {
                drawType = "Run";
            } else drawType = "Idle";
        } else if (touchingRightWall || touchingLeftWall) {
            drawType = "WallCling";
        } else if (velY > 0) {
            drawType = "Jump";
        } else if (velY < 0) {
            drawType = "Fall";
        }

        if (shakeFrames > 0) {
            shakeFrames--;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            if (heldWeapons[1] == 0) {
                // special case where there is only one weapon, dont do anything
            } else {
                int tempHoldingFirstSlot = heldWeapons[0]; // holding this for when it gets overwritten in first non both 0 iteration

                for (int i = heldWeapons.length - 1; i > 0; i--) {
                    if (heldWeapons[i] == 0 && heldWeapons[i - 1] != 0) {

                        heldWeapons[0] = heldWeapons[i - 1];
                    } else if (heldWeapons[i] != 0 && heldWeapons[i - 1] != 0) {
                        heldWeapons[i] = heldWeapons[i - 1];
                    }
                }
                heldWeapons[1] = tempHoldingFirstSlot;
            }

            //PRINT THING
            for (int i = 0; i < heldWeapons.length; i++) {
                System.out.print(heldWeapons[i]);
            }
            System.out.println();
            //end of pritn thing

            gunType = heldWeapons[0];
        }

        if (rollCoolingDown) {
            rollCooldownTime--;
            if (rollCooldownTime < 0) {
                rollCoolingDown = false;
            }
        }

        if (beingPulledByGrapplingHook) {
            float diffX = grapplingHookX - this.x;
            float diffY = grapplingHookY - this.y;

            float dist = (float)(Math.sqrt(diffX*diffX + diffY*diffY));
            float percentX = diffX / dist;
            float percentY = diffY / dist;

            velX = percentX* GRAPPLE_PULL_VELOCITY;
            velY = percentY* GRAPPLE_PULL_VELOCITY;
        } else if (isRolling) {
            if (rollTime > 15) {
                if (isRollingLeft) {
                    velX = -7;
                } else {
                    velX = 7;
                }
            } else if (rollTime > 7) {
                if (isRollingLeft) {
                    velX = -6;
                } else {
                    velX = 6;
                }
            } else {
                if (isRollingLeft) {
                    velX = -5;
                } else {
                    velX = 5;
                }
            }

            rollTime--;
            if (rollTime < 0) {
                isRolling = false;
                rollImage = 0;
                rollCoolingDown = true;
                rollCooldownTime = maxRollCooldownTime;
            }


        } else if (touchingGround) {
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                if (velX >= 0 && !touchingLeftWall) {
                    for (int i = 0; i < 3; i++) {
                        DustParticle part = new DustParticle((int)x + ((i + 3) * (width / 6)), (int)y + (random.nextInt(10) - 2), 0, 0, "dust", 100);
                        particle1s.add(part);
                    }
                }

                facingRight = false;
                if (velX > -6) velX -= 2;
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                if (velX <= 0 && !touchingRightWall) {
                    for (int i = 0; i < 3; i++) {
                        DustParticle part = new DustParticle((int)x + (i * (width / 6)), (int)y + (random.nextInt(10) - 2), 0, 0, "dust", 100);
                        particle1s.add(part);
                    }
                }

                facingRight = true;
                if (velX < 6) velX += 2;
            } else if (velX > 0) velX -= 2;
            else if (velX < 0) velX += 2;
        } else {
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                facingRight = false;
                if (velX > -6) velX -= .5;
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                facingRight = true;
                if (velX < 6) velX += .5;
            } else if (velX > 0) velX -= .5;
            else if (velX < 0) velX += .5;
        }

        if (touchingRightWall) {
            facingRight = false;
        } else if (touchingLeftWall) {
            facingRight = true;
        }


        if (touchingLeftWall || touchingRightWall) {
            if (wallSlidingDustFrames < wallSlidingDustMax) {
                wallSlidingDustFrames++;
            } else {
                if (!touchingGround) {
                    if (touchingLeftWall) {
                        DustParticle part = new DustParticle((int)x, (int)y + height / 2, 0, 0, "dust", 100);
                        particle1s.add(part);
                    } else {
                        DustParticle part = new DustParticle((int)x + width - 4, (int)y + height / 2, 0, 0, "dust", 100);
                        particle1s.add(part);
                    }
                }
                wallSlidingDustFrames = 0;
            }
            if (velY < -3) velY = -3;
        }

        if (!beingPulledByGrapplingHook) {
            if (velY < 15) {
                if (Gdx.input.isKeyPressed(Input.Keys.Z) && velY > 1) {
                    velY -= GRAVITY_ACCELERATION * 0.65;
                } else {
                    velY -= GRAVITY_ACCELERATION;
                }
            }

            if (velY > 8) velY = 8;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            if (playerCanJump()) {
                if (isRolling) {
                    isRolling = false;
                    rollCoolingDown = true;
                    rollCooldownTime = maxRollCooldownTime;
                }
                if (touchingGround) {
                    y += 2;
                    velY = 7.5f;
                    for (int i = 0; i < 5; i++) {
                        DustParticle part = new DustParticle((int)x + (i * (width / 5)), (int)y + (random.nextInt(10) - 2), 0, 0, "dust", 100);
                        particle1s.add(part);
                    }

                } else {
                    if (touchingLeftWall) {
                        y += 2;
                        velY = 7.5f;
                        velX = 4f;
                        touchingLeftWall = false;
                        for (int i = 0; i < 5; i++) {
                            DustParticle part = new DustParticle((int)x + (i * (width / 5)), (int)y + (random.nextInt(10) - 2), 0, 0, "dust", 100);
                            particle1s.add(part);
                        }
                    }
                    if (touchingRightWall) {
                        y += 2;
                        velY = 7.5f;
                        velX = -4f;
                        touchingRightWall = false;
                        for (int i = 0; i < 5; i++) {
                            DustParticle part = new DustParticle((int)x + (i * (width / 5)), (int)y + (random.nextInt(10) - 2), 0, 0, "dust", 100);
                            particle1s.add(part);
                        }
                    }
                }


            }
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
            if (playerCanRoll()) {
                isRolling = true;
                rollTime = maxRollTime;
                isRollingLeft = !facingRight;
                rollImage = 0;
                rollFrameChangeTimer = rollFrameChangeTime;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.K)) {
            health = 0;
        }


        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            if (fuel > 0) {
                fuel -= 3;
                velY += .5;
                FlyWaveParticle part = new FlyWaveParticle((int) (x + random.nextDouble() * width), (int)y, 0, 0, "flyingSpin", 80);
                particle1s.add(part);
            }
            if (fuel < 0) fuel = 0;
        }
        if (fuel > maxFuel) fuel = maxFuel;
        if (health > maxHealth) health = maxHealth;
        if (!grappleHookTravelling) {
            x += velX;
            y += velY;
        }

        rect.x = x;
        rect.y = y;
        rect.width = width;
        rect.height = height;

        for (Item item : items) {
            if (rect.overlaps(item.getRect())) {
                if (item.getType().equals("Health")) {
                    item.setDestroyed(true);
                    health++;
                } else if (item.getType().equals("Mana")) {
                    item.setDestroyed(true);
                    fuel += 100;
                } else if (item.getType().equals("Coin")) {
                    item.setDestroyed(true);
                    numCoins++;

                }
            }
        }


        if (mainState == 0 || mainState == 4) {
            handleMakeBullets(bullets, particle1s);
        }


        for (Bullet bullet : bullets) {
            if (bullet.enemyBullet) {
                if (bullet.rect.overlaps(rect)) {

                }
            }
        }

        if (leftWallFrames > 0) {
            leftWallFrames--;
        } else {
            touchingLeftWall = false;
        }
        if (rightWallFrames > 0) {
            rightWallFrames--;
        } else {
            touchingRightWall = false;
        }

        touchingCeiling = false;
        touchingGround = false;
        checkTilesHit(baseTiles, particle1s);

        checkRobotsHit(enemies, anims, enemyBullets);


        bgXOffset += velX / 3;
        bgYOffset += velY / 3;

    }

    public boolean alreadyHasItem(int itemId) {
        for (int i = 0; i < heldWeapons.length; i++) {
            if (heldWeapons[i] == itemId) return true;
        }
        return false;
    }

    public void addWeapon(int itemId) {
        heldWeapons[findFirstEmptyWeaponSpot()] = itemId;
    }

    public int findFirstEmptyWeaponSpot() {
        for (int i = 0; i < heldWeapons.length; i++) {
            if (heldWeapons[i] == 0) return i;
        }

        return heldWeapons.length - 1;
    }

    private void checkRobotsHit(Array<Enemy> enemies, Array<Animation> anims, Array<EnemyBullet> enemyBullets) {
        if (!playerIsInvincible()) {
            for (Enemy enemy : enemies) {
                if (this.rect.overlaps(enemy.getRect()) && !isHit && enemy.isDangerous) {
                    isHit = true;
                    hitTime = 70;
                    health--;

                    HitSquareAnimation anim = new HitSquareAnimation();
                    anim.x = x;
                    anim.y = y;
                    anims.add(anim);

                    if (enemy.getRect().x > this.x) {
                        velX = -10;
                    } else {
                        velX = 10;
                    }
                    velY = 5;
                }
            }
            for (EnemyBullet bul : enemyBullets) {
                if (this.rect.overlaps(bul.getRect()) && !isHit && bul.getIsDangerous()) {
                    isHit = true;
                    hitTime = 70;
                    health--;

                    if (bul.getDestroyOnHit()) {
                        bul.setDestroyed(true);
                    }

                    HitSquareAnimation anim = new HitSquareAnimation();
                    anim.x = x;
                    anim.y = y;
                    anims.add(anim);

                    if (bul.getRect().x > this.x) {
                        velX = -10;
                    } else {
                        velX = 10;
                    }
                    velY = 5;
                }
            }
        } else {
            hitTime--;
            if (hitTime <= 0) {
                hitTime = 0;
                isHit = false;
            }
        }
    }

    private void handleMakeBullets(Array<PlayerBullet> bullets, Array<Particle> particle1s) {
        //this counts the ticks since the player last shot, which bulletSpawnTime(s) it is bigger than determines what you can shoot
        //during this frame
        bulletSpawnCounter++;

        if (Gdx.input.isKeyPressed(Input.Keys.X)) {
            if (playerCanShoot()) {
                if (gunType == 1) {
                    if (bulletSpawnCounter > bullet1SpawnTime) {

                        shakeFrames = 2;
                        bulletSpawnCounter = 0;
                        if (facingRight) {
                            PistolBullet bullet = new PistolBullet();
                            bullet.x = this.x;
                            bullet.y = this.y + 8;
                            bullet.velX = 12;

                            bullet.facingRight = true;
                            bullet.bulletType = "Normal";
                            bullets.add(bullet);


                            ShellParticle part = new ShellParticle((int)this.x,
                                    (int)this.y + (this.height / 2),
                                    -4 + random.nextInt(3) - 1,
                                    5 + random.nextInt(3) - 1,
                                    "Shell",
                                    120);
                            particle1s.add(part);
                        } else {
                            PistolBullet bullet = new PistolBullet();
                            bullet.x = this.x;
                            bullet.y = this.y + 8;
                            bullet.velX = -12;

                            bullet.facingRight = false;
                            bullet.bulletType = "Normal";
                            bullets.add(bullet);


                            ShellParticle part = new ShellParticle((int)this.x,
                                    (int)this.y + (this.height / 2),
                                    4 + random.nextInt(3) - 1,
                                    5 + random.nextInt(3) - 1,
                                    "Shell",
                                    120);

                            particle1s.add(part);
                        }
                    }


                } else if (gunType == 4 && flameAmmo > 0) {

                    if (bulletSpawnCounter > flameSpawnTime) {
                        flameAmmo--;
                        shakeFrames = 1;
                        bulletSpawnCounter = 0;
                        if (facingRight) {


                            for (int i = 0; i < 3; i++) {
                                PlayerFire bullet = new PlayerFire();
                                bullet.x = this.x;
                                bullet.y = this.y + 8;
                                bullet.velX = 8 + random.nextInt(3) - 1;
                                bullet.velY = random.nextInt(3) - 1;
                                bullet.width = 7;
                                bullet.height = 7;
                                bullet.facingRight = false;
                                bullet.bulletType = "Flame";
                                bullets.add(bullet);
                            }
                        } else {
                            for (int i = 0; i < 3; i++) {
                                PlayerFire bullet = new PlayerFire();
                                bullet.x = this.x;
                                bullet.y = this.y + 8;
                                bullet.velX = -8 + (random.nextInt(30) - 10) / 10;
                                bullet.velY = (random.nextInt(30) - 10) / 10;
                                bullet.width = 7;
                                bullet.height = 7;
                                bullet.facingRight = false;
                                bullet.bulletType = "Flame";
                                bullets.add(bullet);
                            }

                        }
                    }
                } else if (gunType == 3 && machineAmmo > 0) {

                    if (bulletSpawnCounter > machineSpawnTime) {
                        machineAmmo--;
                        shakeFrames = 2;
                        bulletSpawnCounter = 0;
                        if (facingRight) {
                            MiniBullet bullet = new MiniBullet();
                            bullet.x = this.x;
                            bullet.y = this.y + 8;
                            bullet.velX = 12;

                            bullet.facingRight = true;
                            bullet.bulletType = "Mini";
                            bullets.add(bullet);

                            this.rect.x -= 2;


                            ShellParticle part = new ShellParticle((int)this.x,
                                    (int)this.y + (this.height / 2),
                                    -4 + random.nextInt(3) - 1,
                                    5 + random.nextInt(3) - 1,
                                    "Shell",
                                    10);
                            particle1s.add(part);
                        } else {
                            MiniBullet bullet = new MiniBullet();
                            bullet.x = this.x;
                            bullet.y = this.y + 8;
                            bullet.velX = -12;

                            bullet.facingRight = false;
                            bullet.bulletType = "Mini";
                            bullets.add(bullet);

                            this.rect.x += 2;


                            ShellParticle part = new ShellParticle((int)this.x,
                                    (int)this.y + (this.height / 2),
                                    4 + random.nextInt(3) - 1,
                                    5 + random.nextInt(3) - 1,
                                    "Shell",
                                    10);

                            particle1s.add(part);
                        }
                    }
                } else if (gunType == 2 && cannonAmmo > 0) {

                    if (bulletSpawnCounter > cannonSpawnTime) {
                        cannonAmmo--;
                        shakeFrames = 5;
                        bulletSpawnCounter = 0;

                        if (facingRight) {
                            CannonBall bullet = new CannonBall();
                            bullet.x = this.x;
                            bullet.y = this.y + 8;
                            bullet.velX = 4;
                            bullet.bulletType = "Cannon";
                            bullet.facingRight = true;
                            bullets.add(bullet);
                            this.rect.x -= 5;


                            for (int i = 0; i < 3; i++) {
                                SparkParticle part = new SparkParticle((int)this.x,
                                        (int)this.y + (this.height / 2),
                                        random.nextInt(7) - 5,
                                        random.nextInt(7) - 5,
                                        "Spark",
                                        20);
                                part.width = 2;
                                part.height = 2;
                                particle1s.add(part);
                            }


                        } else {
                            CannonBall bullet = new CannonBall();
                            bullet.x = this.x;
                            bullet.y = this.y + 8;
                            bullet.velX = -4;
                            bullet.bulletType = "Cannon";


                            this.rect.x += 5;

                            bullet.facingRight = false;
                            bullets.add(bullet);


                            for (int i = 0; i < 3; i++) {
                                SparkParticle part = new SparkParticle((int)this.x,
                                        (int)this.y + (this.height / 2),
                                        random.nextInt(7) - 5,
                                        random.nextInt(7) - 5,
                                        "Spark",
                                        20);
                                part.width = 2;
                                part.height = 2;
                                particle1s.add(part);
                            }
                        }
                    }

                } else if (gunType == 5 && rocketAmmo > 0) {

                    if (bulletSpawnCounter > rocketSpawnTime) {
                        rocketAmmo--;
                        shakeFrames = 6;
                        bulletSpawnCounter = 0;
                        if (facingRight) {
                            Rocket bullet = new Rocket();
                            bullet.x = this.x;
                            bullet.y = this.y + 8;
                            bullet.velX = 12;
                            bullet.bulletType = "Rocket";
                            bullet.facingRight = true;
                            bullets.add(bullet);
                        } else {
                            Rocket bullet = new Rocket();
                            bullet.x = this.x;
                            bullet.y = this.y + 8;
                            bullet.velX = -12;

                            bullet.bulletType = "Rocket";
                            bullet.facingRight = false;
                            bullets.add(bullet);
                        }
                    }
                } else if (gunType == 6 && grenadeAmmo > 0) {

                    if (bulletSpawnCounter > grenadeSpawnTime) {
                        grenadeAmmo--;
                        bulletSpawnCounter = 0;
                        if (facingRight) {
                            Grenade bullet = new Grenade();
                            bullet.x = this.x;
                            bullet.y = this.y + 8;
                            bullet.velX = 5;
                            bullet.velY = 5;
                            bullet.bulletType = "Grenade";
                            bullet.facingRight = true;
                            bullets.add(bullet);
                        } else {
                            Grenade bullet = new Grenade();
                            bullet.x = this.x;
                            bullet.y = this.y + 8;
                            bullet.velX = -5;
                            bullet.velY = 5;
                            bullet.bulletType = "Grenade";
                            bullet.facingRight = true;
                            bullets.add(bullet);
                        }
                    }
                }
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
            if (beingPulledByGrapplingHook) {
                triggerEndOfGrapplingHookPull();
            } else if (playerCanShoot() && !grappleHookTravelling) {
                shakeFrames = 2;
                //TODO: this case should be if the right bumper is not held down
                if (!Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                    GrappleHook bullet;
                    if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                        bullet = new GrappleHook(this.x, this.y + 14, GRAPPLE_HOOK_DIAGONAL_VELX, GRAPPLE_HOOK_DIAGONAL_VELY, -45);
                    } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
                        bullet = new GrappleHook(this.x, this.y + 14, -GRAPPLE_HOOK_DIAGONAL_VELX, GRAPPLE_HOOK_DIAGONAL_VELY, 45);
                    } else {
                        bullet = new GrappleHook(this.x,this.y+14, 0, GRAPPLE_HOOK_VELOCITY, 0);
                    }
                    bullets.add(bullet);
                    this.activeGrapplingHook = bullet;
                    grappleHookTravelling = true;

                } else {
                    if (facingRight) {
                        GrappleHook bullet = new GrappleHook(this.x, this.y + 14, GRAPPLE_HOOK_VELOCITY, 0, -90);
                        bullets.add(bullet);
                        this.activeGrapplingHook = bullet;
                        grappleHookTravelling = true;
                    } else {
                        GrappleHook bullet = new GrappleHook(this.x, this.y + 14, -GRAPPLE_HOOK_VELOCITY,0, 90);
                        bullets.add(bullet);
                        this.activeGrapplingHook = bullet;
                        grappleHookTravelling = true;
                    }
                }
            }
        }
    }

    public boolean shouldDrawWeapon() {
        return (!drawType.equals("Roll"));
    }

    public void triggerGrapplingHookPull(GrappleHook hook) {
        this.grappleHookTravelling = false;
        this.beingPulledByGrapplingHook = true;
        this.grapplingHookX = hook.x;
        this.grapplingHookY = hook.y;
    }

    public void triggerEndOfGrapplingHookPull() {
        this.beingPulledByGrapplingHook = false;
        this.grappleHookTravelling = false;
        this.activeGrapplingHook = null;
    }

    public void checkTilesHit(Array<BaseTile> baseTiles, Array<Particle> particle1s) {
        for (BaseTile tile : baseTiles) {
            if (tile.isActive) {
                if (rect.overlaps(tile.rect)) {

                    float rect2Top = tile.rect.y + tile.rect.height;
                    float rect2Bot = tile.rect.y;
                    float rect2Left = tile.rect.x;
                    float rect2Right = tile.rect.x + tile.rect.width;


                    if (velY < 0) {
                        if (velX == 0) {
                            moveToTopOfBlock(rect2Top, particle1s);
                        } else if (velX > 0) {
                            if (tile.coveredLeft) {
                                moveToTopOfBlock(rect2Top, particle1s);
                            } else if (tile.coveredTop && tile.coveredBottom) {
                                // for wall sliding
                                moveToLeftSideOfBlock(rect2Left);
                            } else {
                                checkDirectionLeastPassed(tile, particle1s);
                            }
                        } else if (velX < 0) {
                            if (tile.coveredRight) {
                                moveToTopOfBlock(rect2Top, particle1s);
                            } else if (tile.coveredTop && tile.coveredBottom) {
                                // for wall sliding
                                moveToRightSideOfBlock(rect2Right);
                            } else {
                                checkDirectionLeastPassed(tile, particle1s);
                            }
                        }
                    } else if (velY > 0) {
                        if (velX == 0) {
                            moveToBottomOfBlock(rect2Bot);
                        } else if (velX > 0) {
                            if (tile.coveredLeft) {
                                moveToBottomOfBlock(rect2Bot);
                            } else if (tile.coveredTop && tile.coveredBottom) {
                                // for wall sliding
                                moveToLeftSideOfBlock(rect2Left);
                            } else {
                                checkDirectionLeastPassed(tile, particle1s);
                            }
                        } else if (velX < 0) {
                            if (tile.coveredRight) {
                                moveToBottomOfBlock(rect2Bot);
                            } else if (tile.coveredTop && tile.coveredBottom) {
                                // for wall sliding
                                moveToRightSideOfBlock(rect2Right);
                            } else {
                                checkDirectionLeastPassed(tile, particle1s);
                            }
                        }
                    } else if (velY == 0) {
                        if (velX > 0) {
                            moveToLeftSideOfBlock(rect2Left);
                        } else if (velX < 0) {
                            moveToRightSideOfBlock(rect2Right);
                        } else {
                            checkDirectionLeastPassed(tile, particle1s);
                        }
                    }


                }
                y = rect.y;
                x = rect.x;

            }
        }
    }

    private void checkDirectionLeastPassed(BaseTile tile, Array<Particle> particles) {
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
        //TODO: the problem with wall collision is that the player phases into the wall and then when it falls it hits
        // the top of the next block

        //TODO: the problem with the player sometimes teleporting when hitting the floor while going really fast is caused by
        // the player going so deep into the block that he is deeper vertically than he is horizontally, and since
        //it pushes him back the least deep distance, it pushed him horizontally
        if (directionLeastPassed.equals("Left")) {
            moveToRightSideOfBlock(rect2Right);
        } else if (directionLeastPassed.equals("Right")) {
            moveToLeftSideOfBlock(rect2Left);
        } else if (directionLeastPassed.equals("Bottom")) {
            moveToTopOfBlock(rect2Top, particles);
        } else if (directionLeastPassed.equals("Top")) {
            moveToBottomOfBlock(rect2Bot);

        }
    }

    private void moveToTopOfBlock(float rect2Top, Array<Particle> particles) {
        rect.y = rect2Top;
        if (velY < -2) {
            for (int i = 0; i < 3; i++) {
                DustParticle part = new DustParticle((int)x + (i * (width / 6)), (int)y + (random.nextInt(10) - 2), 0, 0, "dust", 100);
                particles.add(part);
            }
        }
        velY = 0;
        touchingGround = true;
        if (velX < 2 && velX > -2) {
            velX = 0;
        }
    }

    private void moveToBottomOfBlock(float rect2Bot) {
        rect.y = rect2Bot - rect.height;
        velY = 0;
        touchingCeiling = true;
    }

    private void moveToRightSideOfBlock(float rect2Right) {
        rect.x = rect2Right;
        velX = 0;
        touchingLeftWall = true;
        leftWallFrames = 5;
    }

    private void moveToLeftSideOfBlock(float rect2Left) {
        rect.x = rect2Left - rect.width;
        velX = 0;
        touchingRightWall = true;
        rightWallFrames = 5;
    }


    private boolean playerCanJump() {
        return ((touchingGround || touchingLeftWall || touchingRightWall));
    }

    private boolean playerCanRoll() {
        return (!isRolling && !rollCoolingDown && touchingGround);
    }

    public boolean playerIsInvincible() {
        return (isHit || isRolling);
    }

    private boolean playerCanShoot() {
        return !isRolling;
    }
}
