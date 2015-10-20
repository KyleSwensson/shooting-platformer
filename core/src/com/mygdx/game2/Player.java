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
    boolean facingRight = false; // if facing = false player is facing left, if facing = true player = facing right
    int x = 0;
    int y = 0;
    int fuel = 0; //player has fuel for a floating spell that shoots him up, regains 1 fuel per second, flying costs 3 fuel per second
    int maxFuel = 500;

    String drawType = "Idle"; // tells the game what animation type to have the player be in
    // types that exist are Jumping, Falling, Running, Idle, WallCling

    int runningImage = 0; // which image the character is on in the running animation
    int runningTimer = 0; // timer counting up to frameChangeTime
    int runningFrameChangeTime = 5; // frames it takes to change to next running image

    int idleImage = 0; // which image the character is on in the running animation
    int idleTimer = 0; // timer counting up to frameChangeTime
    int idleFrameChangeTime = 20; // frames it takes to change to next running image

    float bgXOffset = 0;
    float bgYOffset = 0;

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
    boolean canJump = false;

    int bulletSpawnCounter = 0;
    final int bullet1SpawnTime = 14;
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
    int rightWallFrames = 0; // if they hit 0 touchingLeftWall turns false
    Rectangle rect = new Rectangle();

    public Player() {
        health = maxHealth;
    }

    public void update(Array<PlayerBullet> bullets, Array<BaseTile> baseTiles, Array<Particle> particle1s, Array<Enemy> enemies, Array<Animation> anims, Array<Item> items, Array<EnemyBullet> enemyBullets) {
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

        // tells the game what animation type to draw the player in
        if (touchingGround) {
            if (Math.abs(velX) >= 2) {
                drawType = "Run";
            }
            else drawType = "Idle";
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
        if(Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            gunType ++;
            if (gunType > 6) gunType = 1;
        }

        if (touchingGround) {
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                if (velX >= 0) {
                    for (int i = 0; i < 3; i++) {
                        DustParticle part = new DustParticle(x + ((i + 3) * (width / 6)), y + (random.nextInt(10) - 2), 0, 0, "dust", 100);
                        particle1s.add(part);
                    }
                }

                facingRight = false;
                if (velX > -6)
                    velX -= 2;
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                if (velX <= 0) {
                    for (int i = 0; i < 3; i++) {
                        DustParticle part = new DustParticle(x + (i * (width / 6)), y + (random.nextInt(10) - 2), 0, 0, "dust", 100);
                        particle1s.add(part);
                    }
                }

                facingRight = true;
                if (velX < 6) velX += 2;
            }
            else if (velX > 0) velX -= 2;
            else if (velX < 0) velX += 2;
        } else {
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                facingRight = false;
                if (velX > -6) velX -= .5;
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                facingRight = true;
                if (velX < 6) velX += .5;
            }
            else if (velX > 0) velX -= .5;
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
                if (touchingLeftWall) {
                    DustParticle part = new DustParticle(x, y + height / 2, 0, 0, "dust", 100);
                    particle1s.add(part);
                } else {
                    DustParticle part = new DustParticle(x + width - 4, y + height / 2, 0, 0, "dust", 100);
                    particle1s.add(part);
                }
                wallSlidingDustFrames = 0;
            }
            if (velY < -3) velY = -3;
        }
            if (velY < 15) velY -= .3;

        if (velY > 8) velY = 8;

        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            if (canJump) {
                if (touchingGround) {
                    y += 2;
                    velY = 7.5f;
                    canJump = false;
                    for(int i = 0; i < 5; i++) {
                        DustParticle part = new DustParticle(x + (i * (width / 5)), y + (random.nextInt(10) - 2), 0, 0, "dust", 100);
                        particle1s.add(part);
                    }

                } else {
                    if (touchingLeftWall) {
                        y += 2;
                        velY = 7.5f;
                        velX = 4f;
                        canJump = false;
                        for(int i = 0; i < 5; i++) {
                            DustParticle part = new DustParticle(x + (i * (width / 5)), y + (random.nextInt(10) - 2), 0, 0, "dust", 100);
                            particle1s.add(part);
                        }
                    }
                    if (touchingRightWall) {
                        y += 2;
                        velY = 7.5f;
                        velX = -4f;
                        canJump = false;
                        for(int i = 0; i < 5; i++) {
                            DustParticle part = new DustParticle(x + (i * (width / 5)), y + (random.nextInt(10) - 2), 0, 0, "dust", 100);
                            particle1s.add(part);
                        }
                    }
                }


            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.K)) {
            health = 0;
        }


        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            if (fuel > 0) {
                fuel -=3;
                velY += .5;
            }
            if (fuel < 0) fuel = 0;
        }
        if (fuel > maxFuel) fuel = maxFuel;
        if (health > maxHealth) health = maxHealth;



        x += velX;

        y += velY;

        rect.x = x;
        rect.y = y;
        rect.width = width;
        rect.height = height;

        for (Item item : items) {
            if (rect.overlaps(item.getRect())) {
                if (item.getType().equals("Health")) {
                    item.setDestroyed(true);
                    health ++;
                } else if (item.getType().equals("Mana")) {
                    item.setDestroyed(true);
                    fuel += 100;
                }
            }
        }


        handleMakeBullets(bullets, particle1s);


        for (Bullet bullet : bullets) {
            if (bullet.enemyBullet) {
                if (bullet.rect.overlaps(rect)) {
                    System.out.println("hit");
                }
            }
        }

        canJump = false;
        if (leftWallFrames > 0) {
            leftWallFrames --;
            canJump = true;
        } else {
            touchingLeftWall = false;
        }
        if (rightWallFrames > 0) {
            rightWallFrames --;
            canJump = true;
        } else {
            touchingRightWall = false;
        }

        touchingCeiling = false;
        touchingGround = false;
        checkTilesHit(baseTiles, particle1s);

        checkRobotsHit(enemies, anims, enemyBullets);


        bgXOffset += velX/3;
        bgYOffset += velY/3;

    }

    private void checkRobotsHit(Array<Enemy> enemies, Array<Animation> anims, Array<EnemyBullet> enemyBullets) {
        if (!isHit) {
            for (Enemy enemy : enemies) {
                if (this.rect.overlaps(enemy.getRect())) {
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
                if (this.rect.overlaps(bul.getRect())) {
                    isHit = true;
                    hitTime = 70;
                    health--;

                    bul.setDestroyed(true);

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
            hitTime --;
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

        if(Gdx.input.isKeyPressed(Input.Keys.Z)) {
            if (gunType == 1) {
                if (bulletSpawnCounter > bullet1SpawnTime) {
                    shakeFrames = 2;
                    bulletSpawnCounter = 0;
                    if (facingRight) {
                        PistolBullet bullet = new PistolBullet();
                        bullet.x = this.x;
                        bullet.y = this.y + 8;
                        bullet.velX = 12;
                        bullet.width = 8;
                        bullet.height = 8;
                        bullet.facingRight = true;
                        bullet.bulletType = "Normal";
                        bullets.add(bullet);




                        ShellParticle part = new ShellParticle(this.x,
                                this.y+(this.height/2),
                                -4 + random.nextInt(3) - 1,
                                5+random.nextInt(3) - 1,
                                "Shell",
                                120);
                        particle1s.add(part);
                    } else {
                        PistolBullet bullet = new PistolBullet();
                        bullet.x = this.x;
                        bullet.y = this.y + 8;
                        bullet.velX = -12;
                        bullet.width = 8;
                        bullet.height = 8;
                        bullet.facingRight = false;
                        bullet.bulletType = "Normal";
                        bullets.add(bullet);



                        ShellParticle part = new ShellParticle(this.x,
                                this.y+(this.height/2),
                                4 + random.nextInt(3) - 1,
                                5+random.nextInt(3) - 1,
                                "Shell",
                                120);

                        particle1s.add(part);
                    }
                }


            }
            else if (gunType == 2) {
                    if (bulletSpawnCounter > flameSpawnTime) {
                        shakeFrames = 1;
                        bulletSpawnCounter = 0;
                        if (facingRight) {


                            for (int i = 0; i<3; i++) {
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
            } else if (gunType == 3) {
                if (bulletSpawnCounter > machineSpawnTime) {
                    shakeFrames = 2;
                    bulletSpawnCounter = 0;
                    if (facingRight) {
                        MiniBullet bullet = new MiniBullet();
                        bullet.x = this.x;
                        bullet.y = this.y + 8;
                        bullet.velX = 12;
                        bullet.width = 6;
                        bullet.height = 6;
                        bullet.facingRight = true;
                        bullet.bulletType = "Mini";
                        bullets.add(bullet);

                        this.rect.x -= 2;



                        ShellParticle part = new ShellParticle(this.x,
                                this.y+(this.height/2),
                                -4 + random.nextInt(3) - 1,
                                5+random.nextInt(3) - 1,
                                "Shell",
                                10);
                        particle1s.add(part);
                    } else {
                        MiniBullet bullet = new MiniBullet();
                        bullet.x = this.x;
                        bullet.y = this.y + 8;
                        bullet.velX = -12;
                        bullet.width = 6;
                        bullet.height = 6;
                        bullet.facingRight = false;
                        bullet.bulletType = "Mini";
                        bullets.add(bullet);

                        this.rect.x += 2;



                        ShellParticle part = new ShellParticle(this.x,
                                this.y+(this.height/2),
                                4 + random.nextInt(3) - 1,
                                5+random.nextInt(3) - 1,
                                "Shell",
                                10);

                        particle1s.add(part);
                    }
                }
            } else if (gunType == 4) {
                if (bulletSpawnCounter > cannonSpawnTime) {
                    shakeFrames = 5;
                    bulletSpawnCounter = 0;

                    if (facingRight) {
                        CannonBall bullet = new CannonBall();
                        bullet.x = this.x;
                        bullet.y = this.y + 8;
                        bullet.velX = 8;
                        bullet.width = 12;
                        bullet.height = 12;
                        bullet.bulletType = "Cannon";
                        bullet.facingRight = true;
                        bullets.add(bullet);
                        this.rect.x -= 5;


                        for (int i = 0; i<3; i++) {
                            SparkParticle part = new SparkParticle(this.x,
                                    this.y + (this.height / 2),
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
                        bullet.velX = -8;
                        bullet.width = 12;
                        bullet.height = 12;
                        bullet.bulletType = "Cannon";


                        this.rect.x += 5;

                        bullet.facingRight = false;
                        bullets.add(bullet);


                        for (int i = 0; i<3; i++) {
                            SparkParticle part = new SparkParticle(this.x,
                                    this.y + (this.height / 2),
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

            } else if (gunType == 5) {
                if (bulletSpawnCounter > rocketSpawnTime) {
                    shakeFrames = 6;
                    bulletSpawnCounter = 0;
                    //TODO: make rocket spawn
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
            } else if (gunType == 6) {
                if (bulletSpawnCounter > grenadeSpawnTime) {
                    bulletSpawnCounter = 0;
                    if (facingRight) {
                        Grenade bullet = new Grenade();
                        bullet.x = this.x;
                        bullet.y = this.y + 8;
                        bullet.velX = 7;
                        bullet.velY = 5;
                        bullet.bulletType = "Grenade";
                        bullet.facingRight = true;
                        bullets.add(bullet);
                    } else {
                        Grenade bullet = new Grenade();
                        bullet.x = this.x;
                        bullet.y = this.y + 8;
                        bullet.velX = -7;
                        bullet.velY = 5;
                        bullet.bulletType = "Grenade";
                        bullet.facingRight = true;
                        bullets.add(bullet);
                    }
                }
            }
        }
    }

    public void checkTilesHit(Array<BaseTile> baseTiles,Array<Particle> particle1s) {

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

                    if (directionLeastPassed.equals("Left")) {

                        //TODO: the problem with wall collision is that the player phases into the wall and then when it falls it hits
                        // the top of the next block
                        rect.x = rect2Right;
                        velX = 0;
                        canJump = true;
                        touchingLeftWall = true;
                        leftWallFrames = 5;

                    } else if (directionLeastPassed.equals("Right")) {
                        rect.x = rect2Left - rect.width;
                        velX = 0;
                        canJump = true;
                        touchingRightWall = true;
                        rightWallFrames = 5;


                    } else if (directionLeastPassed.equals("Bottom")) {
                        rect.y = rect2Top;
                        if (velY < -2) {
                            for (int i = 0; i < 3; i++) {
                                DustParticle part = new DustParticle(x + (i * (width / 6)), y + (random.nextInt(10) - 2), 0, 0, "dust", 100);
                                particle1s.add(part);
                            }
                        }
                        velY = 0;
                        canJump = true;
                        touchingGround = true;
                        if (velX < 2 && velX > -2) {
                            velX = 0;
                        }

                    } else if (directionLeastPassed.equals("Top")) {
                        rect.y = rect2Bot - rect.height;
                        velY = 0;
                        touchingCeiling = true;

                    }



                }
                y = (int) rect.y;
                x = (int) rect.x;

            }
        }
    }
}
