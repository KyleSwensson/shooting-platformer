package com.mygdx.game2;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.*;


//TODO: have each tile have an additional chance to spawn enemies, have enemies drop experience, have a level system
//TODO: also have some tiles have upgrades in them like guns, etc. monsters can also drop pickups like health and mana
//TODO: game should play like risk of rain + super crate box
//TODO: Play risk of rain, super crate box, new alien invasion game to help get an idea of how the game should feel
//TODO: make particle a main abstract class and have the different particle types extend particle, same with playerBullets. can have a list
// of playerBullets that all use the same methods but the methods will do different things in the subclasses
//TODO: make there be things that spawn more monsters over time when you are in range of them

//TODO: known bugs are: fix collision detection, not detecting collision on tiles inside of concave corners is a problem because
//sometimes grenades can slip through the corner


import java.util.Random;

/*
------------------------PROJECT DEFINITION-----------------------------
-Game similar in game-feel to super crate box + cave story
-you are a ??? with a nut-shooting gun (maybe you are some weird mythical being sort of looking like midna
-SETTING: Tribal forest with many tiki men as well as some strange robots that don't seem to belong
-you can get a bunch of different crazy guns
-1: fast shooting, low damage, low knockback,standard
-2: faster shooting, extremely low damage, playerBullets, no knockback
-3: Cannon, extremely slow, shoots cannonballs, knocks you back when you shoot it
-4: Flamethrower, self explanatory, shoots particles like particle1 but slowly rise instead of being affected by gravity, also turn to black over time
have gun be a seperate entity drawn over top of the character, not drawn into character sprite

long sprawling, randomized levels. take the cave randomization generator from my old flash game that was too slow to run




*/

public class GdxShooter2 extends ApplicationAdapter {



	//every item in this array actually represents a large chunk of blocks
	int[][] chunkMap = new int[200][60]; // first number is the y value 0 at top 10 at bottom second number is x value 0 at left 10 at right

	int[][] levelMap = new int[chunkMap.length*5 + 5][chunkMap[0].length*5 + 5];

	int[][] bgMap = new int[levelMap.length / 4][levelMap[0].length / 4];


	int currentLevel = 0;
	int maxLevelReached = 0;

	BitmapFont levelText;
	int levelTextX;
	int levelTextY;
	WeaponAmmoText ammoText;


	//Textures for idle player
	Texture[] playerIdleImgs = new Texture[4];
	Texture playerWallClingImg;
	Texture playerJumpImg;
	Texture playerFallImg;
	Texture[] playerRunImgs = new Texture[10];


	//Textures for characters
	Texture boxManImg1;
	Texture robot1Img;
	Texture robot1FlyImg;

	//Textures for accessories for characters (guns etc.)
	Texture baseGunImg;
	Texture machineGunImg;
	Texture flamethrowerImg;
	Texture cannonImg;




	//Textures for playerBullets and damaging things
	Texture bullet1img;
	Texture bullet2img;
	Texture cannonBallImg;
	Texture rocketImg;
	Texture grenadeImg;

	//Textures for particles
	Texture particle1Img;
	Texture sparkParticleImg;
	Texture flameParticleImg;
	Texture experienceParticleImg;
	Texture dustParticleImg;
	Texture techParticleImg;

	//Textures for tiles
	Texture groundTileImg;

	Texture bgTileImg;

	Texture dirtTile;
	Texture dirtTileGrassCorner;
	Texture dirtTileGrassTop;
	Texture dirtTileSide;
	Texture dirtTileBottom;

	Texture tileMapImg;
	//TODO: delete parralax scrolly bg that dont work

	//Textures for GUI items
	Texture healthBarImg;
	Texture manaBarImg;
	Texture barHolderImg;
	Texture weaponSelectImg;

	//textures for miscellaneous map items (gates, traps, doors etc.)
	Texture endLevelDoorImg;
	Texture spikesImg;


	//bg and other non-interactive textures
	Texture jungleBg;
	Texture jungleBg2;

	Random random = new Random();

	int shakeFrames = 0;
	int cameraXAdjustment;
	int cameraYAdjustment;

	//TODO: figure out why can go thru floor
	//TODO: probably make max down velocity lower

	Gate endGate;

	float time = 0;

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private SpriteBatch hudBatch;
	private SpriteBatch bgBatch;
	private Player player;
	private WeaponSelectTile wepSelect1;
	private WeaponSelectTile wepSelect2;
	private WeaponSelectTile wepSelect3;

	public Array<PlayerBullet> playerBullets = new Array<PlayerBullet>();
	public Array<EnemyBullet> enemyBullets = new Array<EnemyBullet>();

	public Array<BaseTile> baseTiles = new Array<BaseTile>();
	public Array<BaseTile> passiveTiles = new Array<BaseTile>();
	public Array<Particle> particle1s = new Array<Particle>();
	public Array<Roboto1> robot1s = new Array<Roboto1>();
	public Array<Item> items = new Array<Item>();
	public Array<Animation> anims = new Array<Animation>();
	public Array<Enemy> enemies = new Array<Enemy>();

	
	@Override
	public void create () {
		camera = new OrthographicCamera();
		camera.setToOrtho(false,800,480);
		batch = new SpriteBatch();
		hudBatch = new SpriteBatch();
		bgBatch = new SpriteBatch();

		levelText = new BitmapFont();
		levelTextX = 30;
		levelTextY = 330;

		//Textures for idle player
		playerIdleImgs[0] = new Texture("SPA/Player/Iddle/1.png");
		playerIdleImgs[1] = new Texture("SPA/Player/Iddle/2.png");
		playerIdleImgs[2] = new Texture("SPA/Player/Iddle/3.png");
		playerIdleImgs[3] = new Texture("SPA/Player/Iddle/4.png");


		playerFallImg = new Texture("SPA/Player/Fall-Jump-WallJ/Fall.png");
		playerJumpImg = new Texture("SPA/Player/Fall-Jump-WallJ/Jump.png");
		playerWallClingImg = new Texture("SPA/Player/Fall-Jump-WallJ/WallJ.png");
		playerRunImgs[0] = new Texture("SPA/Player/Run/1.png");
		playerRunImgs[1] = new Texture("SPA/Player/Run/2.png");
		playerRunImgs[2] = new Texture("SPA/Player/Run/3.png");
		playerRunImgs[3] = new Texture("SPA/Player/Run/4.png");
		playerRunImgs[4] = new Texture("SPA/Player/Run/5.png");
		playerRunImgs[5] = new Texture("SPA/Player/Run/6.png");
		playerRunImgs[6] = new Texture("SPA/Player/Run/7.png");
		playerRunImgs[7] = new Texture("SPA/Player/Run/8.png");
		playerRunImgs[8] = new Texture("SPA/Player/Run/9.png");
		playerRunImgs[9] = new Texture("SPA/Player/Run/10.png");


		//Textures for characters
		boxManImg1 = new Texture("heroGunHold.png");
		robot1Img = new Texture("robot1.png");
		robot1FlyImg = new Texture("robot1fly.png");

		//Textures for accessories for characters (guns etc.)
		baseGunImg = new Texture("gunOne.png");
		machineGunImg = new Texture("machineGun.png");
		flamethrowerImg = new Texture("flamethrower.png");
		cannonImg = new Texture("cannon.png");


		//Textures for ground tiles
		tileMapImg = new Texture("SPA/tile-map/tilemap.png");


		//Textures for playerBullets and damaging things
		bullet1img = new Texture("bullet1.png");
		bullet2img = new Texture("machineGunBullet.png");
		cannonBallImg = new Texture("cannonBall.png");
		rocketImg = new Texture("rocket.png");
		grenadeImg = new Texture("grenade.png");

		//Textures for particles
		particle1Img = new Texture ("particle1.png");
		sparkParticleImg = new Texture("sparkParticle.png");
		flameParticleImg = new Texture("fireParticle.png");
		experienceParticleImg = new Texture("experiencePellet.png");
		dustParticleImg = new Texture("SPA/Player/player_dust.png");
		techParticleImg = new Texture("techParticle.png");


		//Textures for tiles

		bgTileImg = new Texture("SPA/Decoration/tiled_bg.png");
		groundTileImg = new Texture("groundTile.png");
		dirtTile = new Texture("dirtTile.png");
		dirtTileGrassCorner = new Texture("dirtGrassTopCorner.png");
		dirtTileGrassTop = new Texture("dirtGrassTop.png");
		dirtTileSide = new Texture("dirtSideTile.png");
		dirtTileBottom = new Texture("dirtTileBottom.png");

		//Textures for GUI items
		healthBarImg = new Texture("healthBarFull.png");
		manaBarImg = new Texture("manaBarFull.png");
		barHolderImg = new Texture("barHolder.png");
		weaponSelectImg = new Texture("SPA/HUD/weaponBox.png");

		//bg and other non-interactive textures
		jungleBg = new Texture("jungleBg.png");
		jungleBg2 = jungleBg;

		//Textures for misc map items like doors and shit
		endLevelDoorImg = new Texture("levelEndGate.png");
		spikesImg = new Texture("SPA/tile-map/spikes.png");

		player = new Player();
		player.x = 800 / 2 - 64 / 2;
		player.y = (levelMap[1].length / 2) * 32;
		player.width = 24;
		player.height = 36;

		wepSelect1 = new WeaponSelectTile(weaponSelectImg, 30, 445,baseGunImg, flamethrowerImg, machineGunImg, cannonImg, rocketImg, grenadeImg);
		wepSelect2 = new WeaponSelectTile(weaponSelectImg,60,445,baseGunImg, flamethrowerImg, machineGunImg, cannonImg, rocketImg, grenadeImg);
		wepSelect3 = new WeaponSelectTile(weaponSelectImg,90,445, baseGunImg, flamethrowerImg, machineGunImg, cannonImg, rocketImg, grenadeImg);
		ammoText = new WeaponAmmoText(30,430);

		CreateEntireLevel();

	}

	private void CreateEntireLevel() {
		createChunkMap(currentLevel);

		addTileTypesToChunkMap();

		createLevelMap();

		instantiateLevelMap();

		addMiscItems();
	}

	private void instantiateLevelMap() {

		for (int i = 0; i<bgMap.length; i++) {
			for (int p = 0; p < levelMap[0].length; p++) {
				BaseTile tile = new BaseTile(i * 128 + 100, -p * 128 + (bgMap[1].length * 128));
				tile.width = 128;
				tile.height = 128;
				tile.type = "background";
				passiveTiles.add(tile);
			}
		}


		for (int i = 0; i<levelMap.length; i++) {
			for (int p = 0; p < levelMap[0].length; p++) {
				if (levelMap[i][p] == 0) {
					BaseTile tile = new BaseTile(i * 32 + 100,
							-p * 32 + (levelMap[1].length * 32));
					tile.type = "blank";
					if (i > 0 && p > 0 && i < levelMap.length - 1 && p < levelMap[0].length - 1) {
						if (levelMap[i + 1][p] == 0 || levelMap[i + 1][p] == 2) {

							tile.coveredRight = true;
						}
						if (levelMap[i][p + 1] == 0 || levelMap[i][p + 1] == 2) {

							tile.coveredTop = true;
						}
						if (levelMap[i - 1][p] == 0 || levelMap[i - 1][p] == 2) {

							tile.coveredLeft = true;
						}
						if (levelMap[i][p - 1] == 0 || levelMap[i][p - 1] == 2) {

							tile.coveredBottom = true;
						}
					}


					// setting coords for all tiles in tilemaps
					if (tile.coveredLeft && tile.coveredRight && tile.coveredTop && !tile.coveredBottom) {
						//grass top
						tile.tileMapOffsetX = 32;
						tile.tileMapOffsetY = 0;
						tile.isFlipped = false;

					} else if (!tile.coveredLeft && tile.coveredRight && tile.coveredTop && !tile.coveredBottom) {
						//grass corner top left
						tile.tileMapOffsetX = 0;
						tile.tileMapOffsetY = 0;
						tile.isFlipped = false;
					} else if (tile.coveredLeft && !tile.coveredRight && tile.coveredTop && !tile.coveredBottom) {
						//grass corner top right
						tile.tileMapOffsetX = 0;
						tile.tileMapOffsetY = 0;
						tile.isFlipped = true;
					} else if (!tile.coveredLeft && tile.coveredRight && tile.coveredTop && tile.coveredBottom) {
						// side tile left
						tile.tileMapOffsetX = 16;
						tile.tileMapOffsetY = 16;
						tile.isFlipped = false;
					} else if (tile.coveredLeft && !tile.coveredRight && tile.coveredTop && tile.coveredBottom) {
						// side tile right
						tile.tileMapOffsetX = 16;
						tile.tileMapOffsetY = 16;
						tile.isFlipped = true;

					} else if (tile.coveredLeft && tile.coveredRight && !tile.coveredTop && tile.coveredBottom) {
						//bottom tile
						tile.tileMapOffsetX = 0;
						tile.tileMapOffsetY = 16;
						tile.isFlipped = false;
					} else if (!tile.coveredLeft && tile.coveredRight && !tile.coveredTop && tile.coveredBottom) {
						// bottom left corner
						tile.tileMapOffsetX = 0;
						tile.tileMapOffsetY = 0;
						tile.isFlipped = false;
						tile.isFlippedVertical = true;
					} else if (tile.coveredLeft && !tile.coveredRight && !tile.coveredTop && tile.coveredBottom) {
						//bottom right corner
						tile.tileMapOffsetX = 0;
						tile.tileMapOffsetY = 0;
						tile.isFlipped = true;
						tile.isFlippedVertical = true;
					} else if (!tile.coveredLeft && !tile.coveredRight && tile.coveredTop && !tile.coveredBottom) {
						//only covered on bottom
						tile.tileMapOffsetX = 16;
						tile.tileMapOffsetY = 48;
						tile.isFlipped = false;
						tile.isFlippedVertical = false;
					} else {
						//center tile
						tile.tileMapOffsetX = 64;
						tile.tileMapOffsetY = 32;
					}

					baseTiles.add(tile);
				} else if (levelMap[i][p] == 2) {
					BaseTile tile = new BaseTile (i*32 + 100,
							-p*32 + (levelMap[1].length * 32));
					tile.type = "blank";
					tile.passiveTile = true;
					passiveTiles.add(tile);

				} else if (levelMap[i][p] == 3) {
					BaseTile tile = new BaseTile(i*32 + 100,
							-p * 32 + (levelMap[1].length * 32));
					tile.type = "grassTop";
					baseTiles.add(tile);
				} else if (levelMap[i][p] == 99) {
					endGate = new Gate(i * 32 + 100,
							-p * 32 + (levelMap[1].length * 32));
				} else if (levelMap[i][p] == 100) {
					player.x = i * 32 + 100;
					player.y = -p * 32 + (levelMap[1].length * 32);

					NewLevelText text = new NewLevelText(player.x, player.y + 50,currentLevel);
					anims.add(text);
				} else if (levelMap[i][p] == 80551) {
					FlyBossHeart enemy = new FlyBossHeart((i*32 + 100), -p * 32 + (levelMap[1].length*32));
					enemy.enemyType = "FlyBoss";
					enemies.add(enemy);

					for (int q = 0; q < 20; q++) {
						BossFly e = new BossFly((i*32 + 100) + (random.nextInt(100) - 50), (-p * 32 + (levelMap[1].length * 32) + (random.nextInt(100) - 50)));
						e.enemyType = "FlyBossMinion";
						enemies.add(e);
					}
				}

			}

		}
	}

	private void createLevelMap() {



		levelMap = new int[chunkMap.length*5 + 5][chunkMap[0].length*5 + 5];
		bgMap = new int[levelMap.length / 2][levelMap[0].length / 2];
		for (int i = 0; i < chunkMap.length; i++) {
			for (int p = 0; p < chunkMap[0].length; p++) {
				if (chunkMap[i][p] == 1) {

					//draw a 5x5 square of 32x32 tiles
					for (int q = 1; q <= 5; q++) {
						for (int w = 1; w <= 5; w++) {
							levelMap[(i*5) + (q)][((p*5) + (w))] = 1;
						}
					}
				} else if (chunkMap[i][p] == 0) {
					//dont fill in anything
				} else if (chunkMap[i][p] == 2) {
					for (int q = 1; q <= 5; q++) {
						for (int w = 1; w <= 5; w++) {
							levelMap[(i*5) + q][((p*5) + w)] = 1;
						}
					}
					levelMap[(i*5) + 2][(p*5) + 2] = 99;// make a win gate
				} else if (chunkMap[i][p] == 69) {
					for (int q = 1; q <= 5; q++) {
						for (int w = 1; w <= 5; w++) {
							levelMap[(i*5) + q][((p*5) + w)] = 1;
						}
					}
					levelMap[(i*5) + 1][(p*5) + 1] = 100;// make a player spawn spot
				} else if (chunkMap[i][p] == 11) {
					for (int q = 1; q <= 5; q++) {
						for (int w = 1; w <= 5; w++) {
							levelMap[(i*5) + q][((p*5) + w)] = 1;
						}
					}

					levelMap[(i*5 + 1)][p*5 + 5] = 0;
					levelMap[(i*5 + 2)][p*5 + 5] = 0;
					levelMap[(i*5 + 3)][p*5 + 5] = 0;
					levelMap[(i*5 + 4)][p*5 + 5] = 0;
					levelMap[(i*5 + 5)][p*5 + 5] = 0;


				} else if (chunkMap[i][p] == 12) {
					for (int q = 1; q <= 5; q++) {
						for (int w = 1; w <= 5; w++) {
							levelMap[(i*5) + q][((p*5) + w)] = 1;
						}
					}

					levelMap[(i*5 + 1)][p*5 + 5] = 0;

					levelMap[(i*5 + 5)][p*5 + 5] = 0;
				} else if (chunkMap[i][p] == 13) {
					for (int q = 1; q <= 5; q++) {
						for (int w = 1; w <= 5; w++) {
							levelMap[(i*5) + q][((p*5) + w)] = 1;
						}
					}

					levelMap[(i*5 + 2)][p*5 + 5] = 0;
					levelMap[(i*5 + 3)][p*5 + 5] = 0;
					levelMap[(i*5 + 4)][p*5 + 5] = 0;

				} else if (chunkMap[i][p] == 14) {
					for (int q = 1; q <= 5; q++) {
						for (int w = 1; w <= 5; w++) {
							levelMap[(i*5) + q][((p*5) + w)] = 1;
						}
					}

					levelMap[(i*5 + 1)][p*5 + 5] = 0;
					levelMap[(i*5 + 2)][p*5 + 5] = 0;
					levelMap[(i*5 + 3)][p*5 + 5] = 0;
					levelMap[(i*5 + 4)][p*5 + 5] = 0;
				}else if (chunkMap[i][p] == 21) {
					for (int q = 1; q <= 5; q++) {
						for (int w = 1; w <= 5; w++) {
							levelMap[(i*5) + q][((p*5) + w)] = 1;
						}
					}



					levelMap[(i*5 + 3)][p*5 + 5] = 0;
					levelMap[(i*5 + 4)][p*5 + 5] = 0;
					levelMap[(i*5 + 5)][p*5 + 5] = 0;
				} else if (chunkMap[i][p] == 22) {
					for (int q = 1; q <= 5; q++) {
						for (int w = 1; w <= 5; w++) {
							levelMap[(i*5) + q][((p*5) + w)] = 1;
						}
					}



					levelMap[(i*5 + 3)][p*5 + 5] = 0;
					levelMap[(i*5 + 4)][p*5 + 5] = 0;
					levelMap[(i*5 + 5)][p*5 + 5] = 0;
					levelMap[(i*5 + 3)][p*5 + 4] = 0;
					levelMap[(i*5 + 4)][p*5 + 4] = 0;
					levelMap[(i*5 + 5)][p*5 + 4] = 0;
				} else if (chunkMap[i][p] == 23) {
					for (int q = 1; q <= 5; q++) {
						for (int w = 1; w <= 5; w++) {
							levelMap[(i*5) + q][((p*5) + w)] = 1;
						}
					}



					levelMap[(i*5 + 3)][p*5 + 5] = 0;
					levelMap[(i*5 + 4)][p*5 + 5] = 0;
					levelMap[(i*5 + 5)][p*5 + 5] = 0;
					levelMap[(i*5 + 4)][p*5 + 4] = 0;
					levelMap[(i*5 + 5)][p*5 + 4] = 0;
					levelMap[(i*5 + 5)][p*5 + 3] = 0;
				}  else if (chunkMap[i][p] == 24) {
					for (int q = 1; q <= 5; q++) {
						for (int w = 1; w <= 5; w++) {
							levelMap[(i*5) + q][((p*5) + w)] = 1;
						}
					}



					levelMap[(i*5 + 1)][p*5 + 5] = 0;
					levelMap[(i*5 + 2)][p*5 + 5] = 0;
					levelMap[(i*5 + 3)][p*5 + 5] = 0;

				} else if (chunkMap[i][p] == 25) {
					for (int q = 1; q <= 5; q++) {
						for (int w = 1; w <= 5; w++) {
							levelMap[(i*5) + q][((p*5) + w)] = 1;
						}
					}



					levelMap[(i*5 + 1)][p*5 + 5] = 0;
					levelMap[(i*5 + 2)][p*5 + 5] = 0;
					levelMap[(i*5 + 3)][p*5 + 5] = 0;
					levelMap[(i*5 + 1)][p*5 + 4] = 0;
					levelMap[(i*5 + 2)][p*5 + 4] = 0;
					levelMap[(i*5 + 3)][p*5 + 4] = 0;

				}  else if (chunkMap[i][p] == 26) {
					for (int q = 1; q <= 5; q++) {
						for (int w = 1; w <= 5; w++) {
							levelMap[(i*5) + q][((p*5) + w)] = 1;
						}
					}



					levelMap[(i*5 + 1)][p*5 + 5] = 0;
					levelMap[(i*5 + 2)][p*5 + 5] = 0;
					levelMap[(i*5 + 3)][p*5 + 5] = 0;
					levelMap[(i*5 + 1)][p*5 + 4] = 0;
					levelMap[(i*5 + 2)][p*5 + 4] = 0;
					levelMap[(i*5 + 1)][p*5 + 3] = 0;

				} else if (chunkMap[i][p] == 30) {
					for (int q = 1; q <= 5; q++) {
						for (int w = 1; w <= 5; w++) {
							levelMap[(i*5) + q][((p*5) + w)] = 1;
						}
					}
				} else if (chunkMap[i][p] == 31) {
					for (int q = 1; q <= 5; q++) {
						for (int w = 1; w <= 5; w++) {
							levelMap[(i*5) + q][((p*5) + w)] = 1;
						}
					}
				} else if (chunkMap[i][p] == 50) {
					for (int q = 1; q <= 5; q++) {
						for (int w = 1; w <= 5; w++) {
							levelMap[(i*5)+q][((p*5) + w)] = 1;
						}
					}
				}  else if (chunkMap[i][p] == 51) {
					for (int q = 1; q <= 5; q++) {
						for (int w = 1; w <= 5; w++) {
							levelMap[(i*5)+q][((p*5) + w)] = 1;
						}
					}
					levelMap[(i*5) + 5][(p*5 + 5)] = 80551;// 80551 stands for boss1



				}
			}
		}

		//this code runs to make there be less blocks in the background, currently it just deletes the blocks but in the future
		// it should make it so that all blocks in the background simply become passable blocks so hit detection code is not run
		// on them, additionally eventually I should make it so that things that aren't on the screen aren't actually drawn
		// or updated but that is for later
		for (int i = 0; i < levelMap.length; i++) {
			for (int p = 0; p < levelMap[0].length; p++) {
				if (i > 0 && p > 0 && i < levelMap.length - 1 && p < levelMap[0].length - 1) { // if i+1 and i-1 and p+1 and p-1 in bounds
					if ((levelMap[i + 1][p] == 0 || levelMap[i + 1][p] == 2) &&
							(levelMap[i][p + 1] == 0 || levelMap[i][p+1] == 2) &&
							(levelMap[i - 1][p] == 0 || levelMap[i-1][p] == 2) &&
							(levelMap[i][p - 1] == 0 || levelMap[i][p-1] == 2))
						levelMap[i][p] = 2; // if the block is surrounded by blocks on all 4 cardinal directions make it so it doesn't exist
				}
			}
		}
	}

	private void createChunkMap(int level) {



		chunkMap = new int[50][20]; // first number is the y value 0 at top 10 at bottom second number is x value 0 at left 10 at right

		boolean movedPastBounds;
		//randomly generate chunkMap
		chunkMap[3][chunkMap[0].length/2] = 69; // makes the 0th tile over and the 4th tile down be an player spawn tile
		int generateChunkX = 0; // the chunk that is being made empty currently in X dir
		int generateChunkY = chunkMap[0].length/2; // the chunk that is being made empty currently in Y dir
		String dirMove = "Right"; // string that can be "Up" "Down" "Left" or "Right"
		int dirRandom = 0; // int between 0-20 that ends up determining which direction the chunks should move next
		//how to generate randoms random.nextInt(max - min + 1) + min
		for (int i = 0; i<200; i++) { // generates 200 tiles
			movedPastBounds = false;
			dirRandom = random.nextInt(20) + 1;
			if (dirRandom >= 1 && dirRandom < 10) {
				dirMove = "Right";
			} else if (dirRandom < 14) {
				dirMove = "Up";
			} else if (dirRandom <18) {
				dirMove = "Down";
			} else dirMove = "Left";

			//TODO: to make player dust just make it spawn a row ish thing of dust particles and have them get smaller really fast width wise faster

			if (dirMove.equals("Right")) generateChunkX ++; // change generatechunks to the right of current block
			else if (dirMove.equals("Left")) generateChunkX --;// change generatechunks to the left of current block
			else if (dirMove.equals("Up")) generateChunkY ++; // change generatechunks to on top of current block
			else if (dirMove.equals("Down")) generateChunkY --; // change generatechunks to on the bottom of current block

			if (generateChunkX > chunkMap.length - 4) {generateChunkX = chunkMap.length - 4;
				movedPastBounds = true;}
			else if (generateChunkX < 3) {
				generateChunkX = 3;
				movedPastBounds = true;
			}

			if (generateChunkY > chunkMap[0].length - 4) {
				generateChunkY = chunkMap[0].length - 4;
				movedPastBounds = true;
			}
			else if (generateChunkY < 3) {
				generateChunkY = 3;
				movedPastBounds = true;
			}

			if (chunkMap[generateChunkX][generateChunkY] == 0) {
				chunkMap[generateChunkX][generateChunkY] = 1;
			}



			if (i == 199) {
				if (level == 2) {
					chunkMap[generateChunkX][generateChunkY] = 51; // boss tile
					chunkMap[generateChunkX][generateChunkY+1] = 50; // boss tile
					chunkMap[generateChunkX+1][generateChunkY] = 50; // boss tile
					chunkMap[generateChunkX+1][generateChunkY+1] = 50; // boss tile


				} else {
					chunkMap[generateChunkX][generateChunkY] = 2; // makes it an end tile
				}

			}


		}
	}

	private void addTileTypesToChunkMap() {
		for (int i = 0; i < chunkMap.length; i++) {
			for (int p = 0; p < chunkMap[0].length; p++) {
				if (chunkMap[i][p] == 1) {
					if (i > 1 && p > 1 && i < chunkMap.length - 2 && p < chunkMap[0].length - 2) {
						if (chunkMap[i][p + 1] == 0) {
							if (chunkMap[i-1][p] == 0) {
								//covered on left
								int tileRandom = random.nextInt(4 - 1 + 1) + 1;
								if (tileRandom == 1) chunkMap[i][p] = 1;
								else if (tileRandom == 2) chunkMap[i][p] = 24;// 1 high step
								else if (tileRandom == 3) chunkMap[i][p] = 25;// 2 high step
								else if (tileRandom == 4) chunkMap[i][p] = 26;//sloped
							}
							else if (chunkMap[i + 1][p] == 0) {
								//covered on left
								int tileRandom = random.nextInt(4 - 1 + 1) + 1;
								if (tileRandom == 1) chunkMap[i][p] = 1;
								else if (tileRandom == 2) chunkMap[i][p] = 21;// 1 high step
								else if (tileRandom == 3) chunkMap[i][p] = 22;// 2 high step
								else if (tileRandom == 4) chunkMap[i][p] = 23;//sloped
							} else {

								//this means its a tile on the ground so it can use ground randomization tile #s, 11,12,13,and 14
								int tileRandom = random.nextInt(7 - 1 + 1) + 1;
								if (tileRandom == 1) chunkMap[i][p] = 1;
								else if (tileRandom == 2) chunkMap[i][p] = 11;
								else if (tileRandom == 3) chunkMap[i][p] = 12;
								else if (tileRandom == 4) chunkMap[i][p] = 13;
								else if (tileRandom == 5) chunkMap[i][p] = 14;
								else if (tileRandom == 6) chunkMap[i][p] = 30;// spawn a robot
								else if (tileRandom == 7) chunkMap[i][p] = 31;// spawn a flying robot
							}
						}
					}
				}
			}
		}
	}

	public void update () {

		if (player.gameOver) {
			makeNewGame();
		}

		if (Gdx.input.isKeyPressed(Input.Keys.P)) {
			camera.zoom += .02;
		} else if (Gdx.input.isKeyPressed(Input.Keys.O)) {
			camera.zoom -= .02;
		}

		if (player.rect.overlaps(endGate.rect)) {
			generateNewLevel();
			currentLevel ++;
		}

		player.update(playerBullets, baseTiles, particle1s, enemies, anims, items, enemyBullets);
		informWeaponSelectTiles();

		shakeFrames += player.shakeFrames;

		//shakeCamera
		for (int i = 0; i < anims.size;){
			Animation anim = anims.get(i);
			anim.update();

			if (anim.getDestroyed()){

				int lastAnim = anims.size-1;
				anims.set(i, anims.get(lastAnim));
				anims.removeIndex(lastAnim);
			} else {
				i++;
			}
		}

		for (int i = 0; i < items.size;){
			Item item = items.get(i);
			System.out.println("updating box");
			item.update(baseTiles, player.rect);
			if (item.getDestroyed()){
				System.out.println("destroying");
				int lastItem = items.size-1;
				items.set(i, items.get(lastItem));
				items.removeIndex(lastItem);
			} else {
				i++;
			}
		}

		for (int i = 0; i < playerBullets.size;){
			PlayerBullet bullet = playerBullets.get(i);
			bullet.update(playerBullets,baseTiles, enemies, anims, particle1s);

			if (bullet.getDestroyed()){
				int lastBullet = playerBullets.size-1;
				playerBullets.set(i, playerBullets.get(lastBullet));
				playerBullets.removeIndex(lastBullet);

			} else {
				i++;
			}
		}

		for (int i = 0; i < enemyBullets.size;){
			EnemyBullet bullet = enemyBullets.get(i);
			bullet.update(baseTiles,player.rect);

			if (bullet.destroyed){
				int lastBullet = enemyBullets.size-1;
				enemyBullets.set(i, enemyBullets.get(lastBullet));
				enemyBullets.removeIndex(lastBullet);
			} else {
				i++;
			}
		}





		for (int i = 0; i < particle1s.size;){
			Particle part = particle1s.get(i);
			part.update(baseTiles, robot1s, player.x, player.y);

			if (part.destroyed){
				int lastParticle = particle1s.size-1;
				particle1s.set(i, particle1s.get(lastParticle));
				particle1s.removeIndex(lastParticle);
			} else {
				i++;
			}
		}

		for (int i = 0; i < robot1s.size;){
			Roboto1 robot = robot1s.get(i);
			robot.update(enemies, enemyBullets, playerBullets,items, baseTiles, particle1s, player.x, player.y);

			if (robot.destroyed){
				int lastRobot = robot1s.size-1;
				robot1s.set(i, robot1s.get(lastRobot));
				robot1s.removeIndex(lastRobot);
			} else {
				i++;
			}
		}

		for (int i = 0; i < enemies.size;){
			Enemy enemy = enemies.get(i);
			enemy.update(enemies,enemyBullets, playerBullets,items, baseTiles, particle1s, player.x, player.y);

			if (enemy.getDestroyed()){
				int lastEnemy = enemies.size-1;
				enemies.set(i, enemies.get(lastEnemy));
				enemies.removeIndex(lastEnemy);
			} else {
				i++;
			}
		}


		for (BaseTile tile : baseTiles) {

			tile.update(player.x, player.y);
		}


	}

	private void informWeaponSelectTiles() {
		wepSelect1.weaponNum = player.gunType - 1;
		if (wepSelect1.weaponNum > 6) wepSelect1.weaponNum = 1;
		else if (wepSelect1.weaponNum < 1) wepSelect1.weaponNum = 6;

		wepSelect2.weaponNum = player.gunType;

		wepSelect3.weaponNum = player.gunType + 1;
		if (wepSelect3.weaponNum > 6) wepSelect3.weaponNum = 1;
		else if (wepSelect3.weaponNum < 1) wepSelect3.weaponNum = 6;
	}


	@Override
	public void render() {

		Gdx.gl.glClearColor(0.5f, 0.5f, 0.8f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		update();

		camera.update();
		camera.position.set(player.x + cameraXAdjustment, player.y + cameraYAdjustment, 0);
		shakeCamera();
		batch.setProjectionMatrix(camera.combined);
		Color batchPrevColor = batch.getColor();
		batch.begin();
		batch.draw(jungleBg, player.x - 500, player.y - 500, 1000, 1000);

		for (BaseTile tile : passiveTiles) {
			if (tile.type.equals("background")) {
				batch.draw(bgTileImg, tile.x, tile.y, tile.width, tile.height);
			} else {
				batch.draw(tileMapImg, tile.x, tile.y, tile.width, tile.height, 64, 32, 16, 16, false, false);
			}
		}

		for (PlayerBullet bul : playerBullets) {
			bul.draw(batch);
		}

		for (EnemyBullet bul : enemyBullets) {
			bul.draw(batch);
		}


		for (Item item : items) {
			item.draw(batch);
		}

		for (Enemy enemy : enemies) {
			enemy.draw(batch);
		}

		for (Roboto1 robot : robot1s) {
			if (robot.flying) {
				if (robot.facingRight) {
					batch.draw(robot1FlyImg, robot.x, robot.y, robot.width, robot.height, 0, 0, 32, 32, false, false);
				} else {
					batch.draw(robot1FlyImg, robot.x, robot.y, robot.width, robot.height, 0, 0, 32, 32, true, false);
				}
			} else {
				if (robot.facingRight) {
					batch.draw(robot1Img, robot.x, robot.y, robot.width, robot.height, 0, 0, 32, 32, false, false);
				} else {
					batch.draw(robot1Img, robot.x, robot.y, robot.width, robot.height, 0, 0, 32, 32, true, false);
				}
			}
		}
		for (Particle part : particle1s) {
			if (part.partType.equals("Shell"))
				batch.draw(particle1Img, part.x, part.y, part.width, part.height, 0, 0, 4, 4, false, false);
			else if (part.partType.equals("Flame"))
				batch.draw(flameParticleImg, part.x, part.y, part.width, part.height, 0, 0, 4, 4, false, false);
			else if (part.partType.equals("Spark"))
				batch.draw(sparkParticleImg, part.x, part.y, part.width, part.height, 0,0, 2, 2, false, false);
			else if (part.partType.equals("EXP"))
				batch.draw(experienceParticleImg, part.x, part.y, 10, 10, 0, 0, 10, 10, false, false);
			else if (part.partType.equals("dust"))
				batch.draw(dustParticleImg, part.x, part.y, part.width,part.height);
			else if (part.partType.equals("technology"))
				batch.draw(techParticleImg, part.x, part.y, part.width,part.height);
		}

		for (BaseTile tile : baseTiles) {

			if (tile.isActive) {
				batch.draw(tileMapImg, (tile.x), (tile.y), tile.width, tile.height, tile.tileMapOffsetX, tile.tileMapOffsetY, 16, 16, tile.isFlipped, tile.isFlippedVertical);
			}
		}





		batch.draw(endLevelDoorImg, endGate.x, endGate.y, endGate.width, endGate.height, 0, 0, 32, 32, false, false);
		drawPlayer();
		if (player.facingRight) {

			if (player.gunType == 1) batch.draw(baseGunImg, player.x + 13, player.y + 5, 12, 8, 0, 0, 12,8, false, false);
			else if (player.gunType == 2) batch.draw(flamethrowerImg, player.x + 13, player.y+5, 16, 12, 0, 0, 18,12, false, false);
			else if (player.gunType == 3) batch.draw(machineGunImg, player.x + 13, player.y+5, 16, 7, 0, 0, 18,9,false,false);
			else if (player.gunType == 4) batch.draw(cannonImg, player.x+ 13, player.y+ 5, 16,12, 0,0,18,14,false,false);
		} else {

			if (player.gunType == 1) batch.draw(baseGunImg, player.x + 6, player.y + 5, 12, 8, 0, 0, 12,8, true, false);
			else if (player.gunType == 2) batch.draw(flamethrowerImg, player.x + 6, player.y+5, 16, 12, 0, 0, 18,12, true, false);
			else if (player.gunType == 3) batch.draw(machineGunImg, player.x + 6, player.y+5, 16, 7, 0, 0, 18,9,true,false);
			else if (player.gunType == 4) batch.draw(cannonImg, player.x+ 6, player.y+ 5, 16,12, 0,0,18,14,true,false);

		}

		for (Animation anim : anims) {
			anim.draw(batch);
		}
		batch.end();
		hudBatch.begin();

		hudBatch.draw(manaBarImg, 50, 347, 18, (int) (66 * ((float) player.fuel / (float) player.maxFuel)));

		levelText.draw(hudBatch, "Level: " + currentLevel,levelTextX,levelTextY);

		hudBatch.draw(barHolderImg, 50, 347,18,66);

		drawAmmoText();


		hudBatch.draw(healthBarImg,30, 347,18,66*((float)player.health/(float)player.maxHealth));
		hudBatch.draw(barHolderImg,30,347,18,66);

		wepSelect1.draw(hudBatch);
		wepSelect2.draw(hudBatch);
		wepSelect3.draw(hudBatch);





		hudBatch.end();
	}

	private void drawAmmoText() {
		if (player.gunType == 1) {
			ammoText.draw(hudBatch,696969,696969);
		} else if (player.gunType == 2) {
			ammoText.draw(hudBatch, player.flameAmmo,player.maxFlameAmmo);
		} else if (player.gunType == 3) {
			ammoText.draw(hudBatch, player.machineAmmo, player.maxMachineAmmo);
		} else if (player.gunType == 4) {
			ammoText.draw(hudBatch, player.cannonAmmo, player.maxCannonAmmo);
		} else if (player.gunType == 5) {
			ammoText.draw(hudBatch, player.rocketAmmo, player.maxRocketAmmo);
		} else if (player.gunType == 6) {
			ammoText.draw(hudBatch, player.grenadeAmmo, player.maxGrenadeAmmo);
		}
	}

	public void drawPlayer() {
		if (player.drawType.equals("Idle")) {
			batch.draw(playerIdleImgs[player.idleImage], player.x, player.y, player.width, player.height, 0, 0, 12, 18, !player.facingRight, false);
		} else if (player.drawType.equals("Fall")) {
			batch.draw(playerFallImg, player.x, player.y, player.width, player.height, 0, 0, 12, 18, !player.facingRight, false);
		} else if (player.drawType.equals("Jump")) {
			batch.draw(playerJumpImg, player.x, player.y, player.width, player.height, 0, 0, 12, 18, !player.facingRight, false);
		} else if (player.drawType.equals("WallCling")) {
			batch.draw(playerWallClingImg, player.x, player.y, player.width, player.height, 0, 0, 12, 18, player.facingRight, false);
		} else if (player.drawType.equals("Run")) {
			batch.draw(playerRunImgs[player.runningImage], player.x, player.y, player.width, player.height, 0, 0, 12, 18, !player.facingRight, false);
		}
	}

	public void makeNewGame() {
		player.gameOver = false;
		System.out.println("Game Over!");
		System.out.println("Previous level reached:" + currentLevel);
		if (currentLevel > maxLevelReached) {
			System.out.println("New high score!");
			maxLevelReached = currentLevel;
		}
		currentLevel = 0;
		player.health = 10;
		System.out.println("Max level reached:" + maxLevelReached);
		generateNewLevel();
	}

	public void generateNewLevel() {
		robot1s.clear();
		enemies.clear();
		enemyBullets.clear();
		items.clear();
		baseTiles.clear();
		playerBullets.clear();
		particle1s.clear();
		passiveTiles.clear();

		player.x = 800 / 2 - 64 / 2;
		player.y = (levelMap[1].length / 2) * 32;

		CreateEntireLevel();



	}

	public void addMiscItems() {
		for (BaseTile baseTile : baseTiles) {
			if (!baseTile.coveredBottom) {
				int spawnRoll = random.nextInt(1000);
				int itemFrequency = 50; // tells how often to spawn items on tiles
				if (spawnRoll > itemFrequency) {
					//do nothing, therefore only make items in 1/20 tiles
				} else if (spawnRoll > (4*itemFrequency) / 5) {
					// smart enemy
					SmartEnemy enemy = new SmartEnemy();
					enemy.x = baseTile.x;
					enemy.y = baseTile.y + baseTile.height;
					enemy.width = 32;
					enemy.height = 32;
					enemies.add(enemy);
				} else if (spawnRoll > (3*itemFrequency) / 5) {
					// flying enemy
					Roboto1 robot = new Roboto1();
					robot.x = baseTile.x;
					robot.y = baseTile.y + baseTile.height;
					robot.width = 32;
					robot.height = 32;
					robot.flying = true;
					enemies.add(robot);
				} else if (spawnRoll > (2*itemFrequency) / 5) {
					// shooting enemy
					ShootingGuy enemy = new ShootingGuy();
					enemy.x = baseTile.x;
					enemy.y = baseTile.y + baseTile.height;
					enemy.width = 32;
					enemy.height = 32;
					enemies.add(enemy);
				} else if (spawnRoll > (itemFrequency) / 5) {
					// normal enemy
					Roboto1 robot = new Roboto1();
					robot.x = baseTile.x;
					robot.y = baseTile.y + baseTile.height;
					robot.width = 32;
					robot.height = 32;
					robot.flying = false;
					enemies.add(robot);
				} else {
					// box
					BaseBox box = new BaseBox(baseTile.x, baseTile.y + baseTile.height,0,0,22,22);
					enemies.add(box);
				}

			}
		}
	}

	public void shakeCamera() {
		if (shakeFrames != 0)
		{
			float shakeFreqX = 10;
			float shakeFreqY = 8;
			float shakeFreqY2 = 20;
			float shakeSizeX = 3;
			float shakeSizeY = 3;
			float shakeSizeY2 = 5;
			time += Gdx.graphics.getDeltaTime();
			cameraXAdjustment = (int)(Math.sin( time*shakeFreqX )*shakeSizeX);
			cameraYAdjustment = (int)(Math.sin( time*shakeFreqY )*shakeSizeY + Math.cos( time*shakeFreqY2 )*shakeSizeY2);



			shakeFrames--;


		} else {
			if (cameraXAdjustment > 2) {
				cameraXAdjustment -= 2;
			} else if (cameraXAdjustment < -2){
				cameraXAdjustment += 2;
			} else {
				cameraXAdjustment = 0;
			}

			if (cameraYAdjustment > 2) {
				cameraYAdjustment -= 2;
			} else if (cameraYAdjustment < -2) {
				cameraYAdjustment += 2;
			} else {
				cameraYAdjustment = 0;
			}
			//cameraXAdjustment = 0;
			//cameraYAdjustment = 0;

		}
	}
}
