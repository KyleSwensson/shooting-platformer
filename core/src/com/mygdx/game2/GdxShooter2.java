package com.mygdx.game2;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;


//TODO: have each tile have an additional chance to spawn mgEnemies, have mgEnemies drop experience, have a level system
//TODO: also have some tiles have upgrades in them like guns, etc. monsters can also drop pickups like health and mana
//TODO: game should play like risk of rain + super crate box
//TODO: Play risk of rain, super crate box, new alien invasion game to help get an idea of how the game should feel
//TODO: make particle a main abstract class and have the different particle types extend particle, same with mgPlayerBullets. can have a list
// of mgPlayerBullets that all use the same methods but the methods will do different things in the subclasses
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
-2: faster shooting, extremely low damage, mgPlayerBullets, no knockback
-3: Cannon, extremely slow, shoots cannonballs, knocks you back when you shoot it
-4: Flamethrower, self explanatory, shoots particles like particle1 but slowly rise instead of being affected by gravity, also turn to black over time
have gun be a seperate entity drawn over top of the character, not drawn into character sprite

long sprawling, randomized levels. take the cave randomization generator from my old flash game that was too slow to run


//TODO: okay so when you leave a level of the dungeon you go to a intermission level like the tiny ones in spelunky but it has a door to a shop in it
//TODO: i dunno what ima do abt item room rn cause that shit spawning still broken


//TODO: switch buttons for jump and shoot to make it match other similar games


*/

public class GdxShooter2 extends ApplicationAdapter {



	//This is the object that holds the main game menu that opens when you start the game
	MainMenu mainMenu;
	//This is the object that holds the pause menu that opens when you press P
	PauseMenu pauseMenu;
	//This is the object that holds the menu that pops up when you die asking if you want to retry or quit
	DeathMenu deathMenu;

	//tells the game what to do, if the gamestate is 0 it is on the main menu, 1 is playing normally, 2 is on the pause menu, 3 is on the death menu;
	int gameState;
	// state to tell whether the player is in a shop or a boss fight or item room, 0 = main game, 1 = shop
	int mainState;

	// the gamestate that will be switched to when the transitioner is done fading into black
	int stateWaiting;

	//every item in this array actually represents a large chunk of blocks
	int[][] chunkMap = new int[200][60]; // first number is the y value 0 at top 10 at bottom second number is x value 0 at left 10 at right

	int[][] levelMap = new int[chunkMap.length*5 + 5][chunkMap[0].length*5 + 5];

	int[][] bgMap = new int[levelMap.length / 4][levelMap[0].length / 4];

	int[][] shopMap = new int[60][34];
	int[][] itemRoomMap = new int[34][34];


	int currentLevel = 1;
	int maxLevelReached = 0;

	BitmapFont levelText;
	int levelTextX;
	int levelTextY;
	WeaponAmmoText ammoText;
	CoinText coinText;



	BitmapFont bitFont;


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




	//Textures for mgPlayerBullets and damaging things
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

	//Textures for GUI mgItems
	Texture healthBarImg;
	Texture manaBarImg;
	Texture healthbarHolderImg;
	Texture manabarHolderImg;
	Texture weaponSelectImg;
	Texture selectedWeaponSelectImg;

	Texture fireIconImg;
	Texture grenadeIconImg;
	Texture heavyIconImg;
	Texture machineGunIconImg;
	Texture pistolIconImg;
	Texture rocketIconImg;

	//textures for miscellaneous map mgItems (gates, traps, doors etc.)
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
	private SpriteBatch blackFadeBatch;
	private Player player;
	private WeaponSelectTile wepSelect1;
	private SelectedWeaponSelectTile wepSelect2;
	private WeaponSelectTile wepSelect3;

	//lists of objects for main game
	public Array<PlayerBullet> mgPlayerBullets = new Array<PlayerBullet>();
	public Array<EnemyBullet> mgEnemyBullets = new Array<EnemyBullet>();

	public Array<BaseTile> mgBaseTiles = new Array<BaseTile>();
	public Array<BaseTile> mgPassiveTiles = new Array<BaseTile>();
	public Array<Particle> mgParticle1s = new Array<Particle>();
	public Array<Roboto1> mgRobot1s = new Array<Roboto1>();
	public Array<Item> mgItems = new Array<Item>();
	public Array<Animation> mgAnims = new Array<Animation>();
	public Array<Enemy> mgEnemies = new Array<Enemy>();

	//lists of objects for shop
	public Array<BaseTile> shopBaseTiles = new Array<BaseTile>();
	public Array<BaseTile> shopPassiveTiles = new Array<BaseTile>();
	public Array<Particle> shopParticle1s = new Array<Particle>();
	public Array<Roboto1> shopRobot1s = new Array<Roboto1>();
	public Array<Item> shopItems = new Array<Item>();
	public Array<Animation> shopAnims = new Array<Animation>();
	public Array<Enemy> shopEnemies = new Array<Enemy>();

	//lists of bojects for item room
	public Array<BaseTile> irBaseTiles = new Array<BaseTile>();
	public Array<BaseTile> irPassiveTiles = new Array<BaseTile>();
	public Array<Particle> irParticle1s = new Array<Particle>();
	public Array<Roboto1> irRobot1s = new Array<Roboto1>();
	public Array<Item> irItems = new Array<Item>();
	public Array<Animation> irAnims = new Array<Animation>();
	public Array<Enemy> irEnemies = new Array<Enemy>();


	//currently active list of objects
	public Array<BaseTile> activeBaseTiles = new Array<BaseTile>();
	public Array<BaseTile> activePassiveTiles = new Array<BaseTile>();
	public Array<Particle> activeParticle1s = new Array<Particle>();
	public Array<Roboto1> activeRobot1s = new Array<Roboto1>();
	public Array<Item> activeItems = new Array<Item>();
	public Array<Animation> activeAnims = new Array<Animation>();
	public Array<Enemy> activeEnemies = new Array<Enemy>();
	
	@Override
	public void create () {

		gameState = 0;
		mainState = 0;
		bitFont = new BitmapFont(Gdx.files.internal("SPA/font.fnt"), false);
		camera = new OrthographicCamera();
		camera.setToOrtho(false,800,480);
		batch = new SpriteBatch();
		hudBatch = new SpriteBatch();
		blackFadeBatch = new SpriteBatch();

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


		//Textures for mgPlayerBullets and damaging things
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

		//Textures for GUI mgItems
		healthBarImg = new Texture("healthBarFull.png");
		manaBarImg = new Texture("manaBarFull.png");
		healthbarHolderImg = new Texture("manaBarHolder.png");
		manabarHolderImg = new Texture("healthBarHolder.png");
		weaponSelectImg = new Texture("SPA/HUD/weaponBox.png");
		selectedWeaponSelectImg = new Texture("SPA/HUD/selectedWeaponBox.png");
		fireIconImg = new Texture("SPA/HUD/fireIcon.png");
		grenadeIconImg = new Texture("SPA/HUD/grenadeIcon.png");
		heavyIconImg = new Texture("SPA/HUD/heavyIcon.png");
		machineGunIconImg = new Texture("SPA/HUD/machineGunIcon.png");
		rocketIconImg = new Texture("SPA/HUD/rocketIcon.png");
		pistolIconImg = new Texture("SPA/HUD/pistolIcon.png");

		//bg and other non-interactive textures
		jungleBg = new Texture("jungleBg.png");
		jungleBg2 = jungleBg;

		//Textures for misc map mgItems like doors and shit
		endLevelDoorImg = new Texture("levelEndGate.png");
		spikesImg = new Texture("SPA/tile-map/spikes.png");

		player = new Player();
		player.x = 800 / 2 - 64 / 2;
		player.y = (levelMap[1].length / 2) * 32;
		player.width = 24;
		player.height = 36;

		wepSelect1 = new WeaponSelectTile(weaponSelectImg, 26, 445,pistolIconImg, heavyIconImg, machineGunIconImg, fireIconImg, rocketIconImg, grenadeIconImg);
		wepSelect2 = new SelectedWeaponSelectTile(selectedWeaponSelectImg,57,443, pistolIconImg, heavyIconImg, machineGunIconImg, fireIconImg, rocketIconImg, grenadeIconImg);
		wepSelect3 = new WeaponSelectTile(weaponSelectImg,92,445,pistolIconImg, heavyIconImg, machineGunIconImg, fireIconImg, rocketIconImg, grenadeIconImg);
		ammoText = new WeaponAmmoText(30,430);
		coinText = new CoinText(715, 460);


		createStartMenu();

	}

	private void createStartMenu() {
		mainMenu = new MainMenu(800,480);
	}


	private void createEntireLevel() {
		createChunkMap(currentLevel);

		addTileTypesToChunkMap();

		createLevelMap();


		instantiateLevelMap();


		addMiscItems();

		setPlayerToMainGameSpawnPos();

		clearOldShop();
		makeShopMap();
		instantiateShopMap();

		clearOldItemRoom();
		makeItemRoomMap();
		instantiateItemRoomMap();
	}

	private void setMainState(int newState) {
		mainState = newState;
		if (newState == 0) {
			player.x = player.oldX;
			player.y = player.oldY;
		}
		if (newState == 1) {
			player.oldX = player.x;
			player.oldY = player.y;
			setPlayerToShopPos();

		} else if (newState == 2) {
			player.oldX = player.x;
			player.oldY = player.y;
			setPlayerToItemRoomPos();

		}
	}

	private void setPlayerToItemRoomPos() {
		setPlayerToSpawnPos(itemRoomMap);
	}

	private void setPlayerToShopPos() {
		setPlayerToSpawnPos(shopMap);
	}

	private void setPlayerToMainGameSpawnPos() {
		setPlayerToSpawnPos(levelMap);
	}

	private void setPlayerToSpawnPos(int[][] givenMap) {
		for (int i = 0; i < givenMap.length; i++) {
			for (int j = 0; j < givenMap[0].length; j++) {
				if (givenMap[i][j] == 100) {
					player.x = i * 32 + 100;
					player.y = -j * 32 + (givenMap[1].length * 32);
				}
			}
		}
	}

	private void clearOldShop() {
		//clear map of item room
		clearGenericMap(shopMap);

		//remove all old item room items
		shopRobot1s.clear();
		shopEnemies.clear();
		shopItems.clear();
		shopBaseTiles.clear();
		shopParticle1s.clear();
		shopPassiveTiles.clear();
		shopAnims.clear();
	}
	private void clearOldItemRoom() {
		//clear map of shop
		clearGenericMap(itemRoomMap);


		//remove all old shop items
		irRobot1s.clear();
		irEnemies.clear();
		irItems.clear();
		irBaseTiles.clear();
		irParticle1s.clear();
		irPassiveTiles.clear();
		irAnims.clear();
	}

	//sets a rectangular 2d array to all 0s
	private void clearGenericMap(int[][] givenMap) {
		for (int i = 0; i < givenMap.length; i++) {
			for (int j = 0; j < givenMap[0].length; j++) {
				givenMap[i][j] = 0;
			}
		}
	}

	private void setBgTiles(int[][] givenMap, Array<BaseTile> passiveTiles) {
		for (int i = 0; i<givenMap.length/4; i++) {
			for (int p = 0; p < givenMap[0].length/4; p++) {
				BaseTile tile = new BaseTile(i * 128 + 100, -p * 128 + ((givenMap[0].length/4) * 128));
				tile.width = 128;
				tile.height = 128;
				tile.type = "background";
				passiveTiles.add(tile);
			}
		}
	}

	private void makeShopMap() {

		setBgTiles(shopMap, shopPassiveTiles);



		for (int i = 0; i < shopMap.length; i++) {
			for (int j = 0; j < shopMap[0].length; j++) {
				shopMap[i][j] = 1;
				if (i <= 12 || i >= shopMap.length - 13 ) {
					shopMap[i][j] = 0;
				} else if (j <= 12 || j >= shopMap[0].length - 13) {
					shopMap[i][j] = 0;
				}
			}
		}

		shopMap[shopMap.length - 15][shopMap[0].length - 14] = 201; // make leave shop door at 25,13
		shopMap[14][shopMap[0].length - 14] = 201; // spawn leaveshopdoor at 14,13
		shopMap[17][shopMap[0].length - 14] = 220; // spawn pickup table at 17, 13
		shopMap[22][shopMap[0].length - 14] = 220;
		shopMap[15][shopMap[0].length - 14] = 100; // spawn player at 15,13

		findInactiveTiles(shopMap);
	}

	private void makeItemRoomMap() {
		setBgTiles(itemRoomMap, irPassiveTiles);

		for (int i = 0; i < itemRoomMap.length; i++) {
			for (int j = 0; j < itemRoomMap[0].length; j++) {
				itemRoomMap[i][j] = 1;
				if (i <= 12 || i >= itemRoomMap.length - 13 ) {
					itemRoomMap[i][j] = 0;
				} else if (j <= 12 || j >= itemRoomMap[0].length - 13) {
					itemRoomMap[i][j] = 0;
				}
			}
		}
		itemRoomMap[19][itemRoomMap[0].length - 14] = 310; // make a new item pedestal with a random item
		itemRoomMap[14][itemRoomMap[0].length - 14] = 201; // make leave itemroom door at 13,13
		itemRoomMap[15][itemRoomMap[0].length - 14] = 100; // spawn player at 14,13
	}

	private void instantiateMap(int[][] givenMap, Array<BaseTile> baseTiles, Array<BaseTile> passiveTiles, Array<Enemy> enemies, Array<Animation> anims, Array<Item> items) {
		for (int i = 0; i<givenMap.length; i++) {
			for (int p = 0; p < givenMap[0].length; p++) {
				if (givenMap[i][p] == 0) {
					BaseTile tile = new BaseTile(i * 32 + 100,
							-p * 32 + (givenMap[1].length * 32));
					tile.type = "blank";

					//TODO: i think this is actually the opposite of what its supposed to be but i made it work somehow so im not gonna fuck with it
					if (i > 0 && p > 0 && i < givenMap.length - 1 && p < givenMap[0].length - 1) {
						if (givenMap[i + 1][p] == 0 || givenMap[i + 1][p] == 2) {

							tile.coveredRight = true;
						}
						if (givenMap[i][p + 1] == 0 || givenMap[i][p + 1] == 2) {

							tile.coveredTop = true;
						}
						if (givenMap[i - 1][p] == 0 || givenMap[i - 1][p] == 2) {

							tile.coveredLeft = true;
						}
						if (givenMap[i][p - 1] == 0 || givenMap[i][p - 1] == 2) {

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

					//determining if it is an edge tile (tiles showuld not spawn stuff on them if they are edge tiles as they arent accessible
					if (i > givenMap.length - 2 || i < 2 || p > givenMap[0].length - 2 || p < 2) { // if this returns true tile must be very near edge of map
						tile.isEdgeTile = true;
					}

					baseTiles.add(tile);
				} else if (givenMap[i][p] == 2) {
					BaseTile tile = new BaseTile(i * 32 + 100,
							-p * 32 + (givenMap[1].length * 32));
					tile.type = "blank";
					tile.passiveTile = true;
					passiveTiles.add(tile);

				} else if (givenMap[i][p] == 3) {
					BaseTile tile = new BaseTile(i * 32 + 100,
							-p * 32 + (givenMap[1].length * 32));
					tile.type = "grassTop";
					baseTiles.add(tile);
				} else if (givenMap[i][p] == 99) { // if levelMap == 99 spawn end gate
					endGate = new Gate(i * 32 + 100,
							-p * 32 + (givenMap[1].length * 32));
				} else if (givenMap[i][p] == 100) { // if levelMap == 100 spawn text telling you what level you're entering
						//player.x = i * 32 + 100;
						//player.y = -p * 32 + (givenMap[1].length * 32);


				} else if (givenMap[i][p] == 102) {
					NewLevelText text = new NewLevelText(i * 32 + 100,
							-p * 32 + (givenMap[1].length * 32), currentLevel, bitFont);
					anims.add(text);
				} else if (givenMap[i][p] == 201) {
					LeaveShopDoor door = new LeaveShopDoor(i * 32 + 100,
							-p * 32 + (givenMap[1].length * 32), 0, 0, 32, 32);
					items.add(door);
				} else if (givenMap[i][p] == 220) { // spawn a shop item table with random items
					ShopTable table = new ShopTable((i*32 + 100), -p * 32 + (givenMap[1].length*32), 0, 0 ,128,32);
					items.add(table);

				} else if (givenMap[i][p] == 310) { // spawn a item pedestal with random item
					PickupPedestal pedestal = new PickupPedestal((i*32 + 100), -p * 32 + (givenMap[1].length*32), 0, 0 ,32, 32);
					items.add(pedestal);


				}else if (givenMap[i][p] == 80551) { // if levelMap == 80551 spawn fly boss
					FlyBossHeart enemy = new FlyBossHeart((i*32 + 100), -p * 32 + (givenMap[1].length*32));
					enemy.enemyType = "FlyBoss";
					enemies.add(enemy);

					for (int q = 0; q < 20; q++) {
						BossFly e = new BossFly((i*32 + 100) + (random.nextInt(100) - 50), (-p * 32 + (givenMap[1].length * 32) + (random.nextInt(100) - 50)));
						e.enemyType = "FlyBossMinion";
						enemies.add(e);
					}
				}

			}

		}

	}

	private void instantiateShopMap() {
		instantiateMap(shopMap,shopBaseTiles,shopPassiveTiles, shopEnemies, shopAnims,shopItems);
	}

	private void instantiateItemRoomMap() {
		instantiateMap(itemRoomMap, irBaseTiles, irPassiveTiles, irEnemies, irAnims, irItems);
	}

	private void setActiveObjects(Array<BaseTile> baseTiles, Array<BaseTile> passiveTiles, Array<Particle> particles,
								  Array<Roboto1> robot1s, Array<Item> items, Array<Animation> anims, Array<Enemy> enemies) {
		activeBaseTiles = baseTiles;
		activePassiveTiles = passiveTiles;
		activeParticle1s = particles;
		activeRobot1s = robot1s;
		activeItems = items;
		activeAnims = anims;
		activeEnemies = enemies;
	}

	private void instantiateLevelMap() {
		setBgTiles(levelMap, mgPassiveTiles);
		instantiateMap(levelMap, mgBaseTiles, mgPassiveTiles, mgEnemies,mgAnims,mgItems);
	}

	private void createLevelMap() {

		levelMap = new int[chunkMap.length*5 + 5][chunkMap[0].length*5 + 5];
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
					levelMap[(i*5) + 1][(p*5) + 1] = 100;// make a player spawn spot\
					levelMap[(i*5) + 1][(p*5) + 2] = 102;
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
		findInactiveTiles(levelMap);
	}

	//this function finds all tiles not on edges and makes them passive so hit detection isn't run on them
	private void findInactiveTiles(int[][] givenMap) {
		for (int i = 0; i < givenMap.length; i++) {
			for (int p = 0; p < givenMap[0].length; p++) {
				if (i > 0 && p > 0 && i < givenMap.length - 1 && p < givenMap[0].length - 1) { // if i+1 and i-1 and p+1 and p-1 in bounds
					if ((givenMap[i + 1][p] == 0 || givenMap[i + 1][p] == 2) &&
							(givenMap[i][p + 1] == 0 || givenMap[i][p+1] == 2) &&
							(givenMap[i - 1][p] == 0 || givenMap[i-1][p] == 2) &&
							(givenMap[i][p - 1] == 0 || givenMap[i][p-1] == 2))
						givenMap[i][p] = 2; // if the block is surrounded by blocks on all 4 cardinal directions make it so it doesn't exist
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

		if (BlackFadeInBG.getInstance().isActive && BlackFadeInBG.getInstance().fadingIn) {
			//update only black fade if fading in to black, because its switching scenes so you shouldnt be able to interact with old scene
			updateBlackFadeIn();
		} else {
			if (BlackFadeInBG.getInstance().isActive) {
				//if it's fading out it's already in the new scene so its okay if you interact with the new scene while black is still fading
				updateBlackFadeIn();
			}
			if (gameState == 0) {
				updateMainMenu();
			}
			if (gameState == 1) {
				updateMainGame();
			}
			if (gameState == 3) {
				updateDeathMenu();
			}
		}
	}

	public void updateBlackFadeIn() {
		BlackFadeInBG.getInstance().update();
		if (!BlackFadeInBG.getInstance().isActive) {
			if (!BlackFadeInBG.getInstance().fadingIn) {
				if (stateWaiting == 1) {
					prepareForNewGame();
					createEntireLevel();
				}
				gameState = stateWaiting;
				BlackFadeInBG.getInstance().isActive = true;
			}
		}
	}

	private void updateDeathMenu() {

		if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
			if (deathMenu.selectedOption == 0) {
				BlackFadeInBG.getInstance().isActive = true;
				stateWaiting = 0;
			} else if (deathMenu.selectedOption == 1) {
				BlackFadeInBG.getInstance().isActive = true;
				stateWaiting = 1;
			}
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
			deathMenu.alterSelected(-1);
		} else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
			deathMenu.alterSelected(1);
		}
	}

	private void updateMainMenu() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
			mainMenu.alterSelected(-1);
		} else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
			mainMenu.alterSelected(1);
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
			if (mainMenu.selectedOption == 0) {
				BlackFadeInBG.getInstance().isActive = true;
				stateWaiting = 1;

			} else if (mainMenu.selectedOption == 3) {
				Gdx.app.exit();
			}
		}

	}

	private void updateMainGame() {
		if (mainState == 0) {
			setActiveObjects(mgBaseTiles,mgPassiveTiles,mgParticle1s,mgRobot1s,mgItems,mgAnims,mgEnemies);
		} else if (mainState == 1) {
			setActiveObjects(shopBaseTiles,shopPassiveTiles,shopParticle1s,shopRobot1s,shopItems,shopAnims,shopEnemies);
		} else if (mainState == 2) {
			setActiveObjects(irBaseTiles, irPassiveTiles, irParticle1s, irRobot1s, irItems, irAnims, irEnemies);
		}

		if (player.gameOver) {
			gameState = 3;
			deathMenu = new DeathMenu();
		}


		//TODO: This is a testing feature, It should be removed upon release
		if (Gdx.input.isKeyPressed(Input.Keys.P)) {
			camera.zoom += .02;
		} else if (Gdx.input.isKeyPressed(Input.Keys.O)) {
			camera.zoom -= .02;
		}

		if (player.rect.overlaps(endGate.rect)) {
			currentLevel++;
			generateNewLevel();

		}

		player.update(mgPlayerBullets, activeBaseTiles, activeParticle1s, activeEnemies, activeAnims, activeItems, mgEnemyBullets, mainState);
		informWeaponSelectTiles();

		shakeFrames += player.shakeFrames;

		//shakeCamera
		for (int i = 0; i < activeAnims.size; ) {
			Animation anim = activeAnims.get(i);
			anim.update();

			if (anim.getDestroyed()) {

				int lastAnim = activeAnims.size - 1;
				activeAnims.set(i, activeAnims.get(lastAnim));
				activeAnims.removeIndex(lastAnim);
			} else {
				i++;
			}
		}

		for (int i = 0; i < activeItems.size; ) {
			Item item = activeItems.get(i);

			item.update(activeBaseTiles, player.rect);
			if (item.getDestroyed()) {

				int lastItem = activeItems.size - 1;
				activeItems.set(i, activeItems.get(lastItem));
				activeItems.removeIndex(lastItem);
			} else {
				i++;
			}

			if (item.getType() == "pickupPedestal") {
				PickupPedestal newItem = (PickupPedestal) item;
				if (newItem.intersectingPlayer && !newItem.itemTaken) {
					newItem.itemTaken = true;
					//TODO: make unlock stuff happen here
					if (!player.alreadyHasItem(newItem.pickup.pickupType)) { // dont pick up weapon if u already got it
						player.addWeapon(newItem.pickup.pickupType);
					}
				}
			} else if (item.getType() == "shopTable") {
				if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
					ShopTable newItem = (ShopTable) item;
					if (newItem.item1.intersectingPlayer && player.numCoins >= newItem.item1.itemPrice) {
						if (!player.alreadyHasItem(newItem.item1.pickup.pickupType)) {
							player.addWeapon(newItem.item1.pickup.pickupType);
							newItem.item1.itemTaken = true;
							player.numCoins -= newItem.item1.itemPrice;
						}
					} else if (newItem.item2.intersectingPlayer && player.numCoins >= newItem.item2.itemPrice) {
						if (!player.alreadyHasItem(newItem.item2.pickup.pickupType)) {
							player.addWeapon(newItem.item2.pickup.pickupType);
							newItem.item2.itemTaken = true;
							player.numCoins -= newItem.item2.itemPrice;
						}
					} else if (newItem.item3.intersectingPlayer && player.numCoins >= newItem.item3.itemPrice) {
						if (!player.alreadyHasItem(newItem.item3.pickup.pickupType)) {
							player.addWeapon(newItem.item3.pickup.pickupType);
							newItem.item3.itemTaken = true;
							player.numCoins -= newItem.item3.itemPrice;
						}
					}
					// TODO: if the player has the item already or if it costs too much make a little X pop up over them and maybe text saying why from the shopkeep

				}
			}

			if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
				if (mainState == 0) {
					if (item.getType() == "shopDoor") {
						ShopDoor newItem = (ShopDoor) item;
						if (newItem.intersectingPlayer) {
							setMainState(1);
						}
					} else if (item.getType() == "itemRoomDoor") {
						ItemRoomDoor newItem = (ItemRoomDoor) item;
						if (newItem.intersectingPlayer) {
							setMainState(2);
						}
					}
				} else if (mainState == 1 || mainState == 2) {
					if (item.getType() == "leaveShopDoor") {
						LeaveShopDoor newItem = (LeaveShopDoor) item;
						if (newItem.intersectingPlayer) {
							setMainState(0);
						}
					}
				}
			}

		}

		for (int i = 0; i < mgPlayerBullets.size; ) {
			PlayerBullet bullet = mgPlayerBullets.get(i);
			bullet.update(mgPlayerBullets, activeBaseTiles, activeEnemies, activeAnims, activeParticle1s);

			if (bullet.getDestroyed()) {
				int lastBullet = mgPlayerBullets.size - 1;
				mgPlayerBullets.set(i, mgPlayerBullets.get(lastBullet));
				mgPlayerBullets.removeIndex(lastBullet);

			} else {
				i++;
			}
		}

		for (int i = 0; i < mgEnemyBullets.size; ) {
			EnemyBullet bullet = mgEnemyBullets.get(i);
			bullet.update(activeBaseTiles, player.rect);

			if (bullet.destroyed) {
				int lastBullet = mgEnemyBullets.size - 1;
				mgEnemyBullets.set(i, mgEnemyBullets.get(lastBullet));
				mgEnemyBullets.removeIndex(lastBullet);
			} else {
				i++;
			}
		}


		for (int i = 0; i < activeParticle1s.size; ) {
			Particle part = activeParticle1s.get(i);
			part.update(activeBaseTiles, activeRobot1s, player.x, player.y);

			if (part.destroyed) {
				int lastParticle = activeParticle1s.size - 1;
				activeParticle1s.set(i, activeParticle1s.get(lastParticle));
				activeParticle1s.removeIndex(lastParticle);
			} else {
				i++;
			}
		}

		for (int i = 0; i < activeRobot1s.size; ) {
			Roboto1 robot = activeRobot1s.get(i);
			robot.update(activeEnemies, mgEnemyBullets, mgPlayerBullets, activeItems, activeBaseTiles, activeParticle1s, player.x, player.y);

			if (robot.destroyed) {
				int lastRobot = activeRobot1s.size - 1;
				activeRobot1s.set(i, activeRobot1s.get(lastRobot));
				activeRobot1s.removeIndex(lastRobot);
			} else {
				i++;
			}
		}

		for (int i = 0; i < activeEnemies.size; ) {
			Enemy enemy = activeEnemies.get(i);
			enemy.update(activeEnemies, mgEnemyBullets, mgPlayerBullets, activeItems, activeBaseTiles, activeParticle1s, player.x, player.y);

			if (enemy.getDestroyed()) {
				int lastEnemy = activeEnemies.size - 1;
				activeEnemies.set(i, activeEnemies.get(lastEnemy));
				activeEnemies.removeIndex(lastEnemy);
			} else {
				i++;
			}
		}

		for (BaseTile tile : activeBaseTiles) {
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

		Gdx.gl.glClearColor(0,0,0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		update();


		batch.setProjectionMatrix(camera.combined);

		if (gameState == 0) {

			camera.update();
			camera.position.set(0,0,0);
			batch.begin();
			mainMenu.draw(batch);
			batch.end();
		} else if (gameState == 1 || gameState == 3) {
			camera.update();
			camera.position.set(player.x + cameraXAdjustment, player.y + cameraYAdjustment, 0);
			shakeCamera();
			batch.begin();
			for (BaseTile tile : activePassiveTiles) {
				if (tile.type.equals("background")) {
					batch.draw(bgTileImg, tile.x, tile.y, tile.width, tile.height);
				} else {
					batch.draw(tileMapImg, tile.x, tile.y, tile.width, tile.height, 64, 32, 16, 16, false, false);
				}
			}



			for (Item item : activeItems) {
				item.draw(batch);
			}

			//no bullets in the shop
			if (mainState == 0) {
				for (PlayerBullet bul : mgPlayerBullets) {
					bul.draw(batch);
				}

				for (EnemyBullet bul : mgEnemyBullets) {
					bul.draw(batch);
				}
			}


			for (Enemy enemy : activeEnemies) {
				enemy.draw(batch);
			}

			for (Roboto1 robot : activeRobot1s) {
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
			for (Particle part : activeParticle1s) {
				if (part.partType.equals("Shell"))
					batch.draw(particle1Img, part.x, part.y, part.width, part.height, 0, 0, 4, 4, false, false);
				else if (part.partType.equals("Flame"))
					batch.draw(flameParticleImg, part.x, part.y, part.width, part.height, 0, 0, 4, 4, false, false);
				else if (part.partType.equals("Spark"))
					batch.draw(sparkParticleImg, part.x, part.y, part.width, part.height, 0, 0, 2, 2, false, false);
				else if (part.partType.equals("EXP"))
					batch.draw(experienceParticleImg, part.x, part.y, 10, 10, 0, 0, 10, 10, false, false);
				else if (part.partType.equals("dust"))
					batch.draw(dustParticleImg, part.x, part.y, part.width, part.height);
				else if (part.partType.equals("technology"))
					batch.draw(techParticleImg, part.x, part.y, part.width, part.height);
				else if (part.partType.equals("flyingSpin")) {
					part.draw(batch);
				}
			}

			for (BaseTile tile : activeBaseTiles) {

				if (tile.isActive) {
					batch.draw(tileMapImg, (tile.x), (tile.y), tile.width, tile.height, tile.tileMapOffsetX, tile.tileMapOffsetY, 16, 16, tile.isFlipped, tile.isFlippedVertical);
				}
			}


			batch.draw(endLevelDoorImg, endGate.x, endGate.y, endGate.width, endGate.height, 0, 0, 32, 32, false, false);
			drawPlayer();
			if (player.facingRight) {

				if (player.gunType == 1)
					batch.draw(baseGunImg, player.x + 13, player.y + 5, 12, 8, 0, 0, 12, 8, false, false);
				else if (player.gunType == 2)
					batch.draw(flamethrowerImg, player.x + 13, player.y + 5, 16, 12, 0, 0, 18, 12, false, false);
				else if (player.gunType == 3)
					batch.draw(machineGunImg, player.x + 13, player.y + 5, 16, 7, 0, 0, 18, 9, false, false);
				else if (player.gunType == 4)
					batch.draw(cannonImg, player.x + 13, player.y + 5, 16, 12, 0, 0, 18, 14, false, false);
			} else {

				if (player.gunType == 1)
					batch.draw(baseGunImg, player.x + 6, player.y + 5, 12, 8, 0, 0, 12, 8, true, false);
				else if (player.gunType == 2)
					batch.draw(flamethrowerImg, player.x + 6, player.y + 5, 16, 12, 0, 0, 18, 12, true, false);
				else if (player.gunType == 3)
					batch.draw(machineGunImg, player.x + 6, player.y + 5, 16, 7, 0, 0, 18, 9, true, false);
				else if (player.gunType == 4)
					batch.draw(cannonImg, player.x + 6, player.y + 5, 16, 12, 0, 0, 18, 14, true, false);

			}

			for (Animation anim : activeAnims) {
				anim.draw(batch);
			}
			batch.end();
			hudBatch.begin();

			hudBatch.draw(manaBarImg, 52, 349, 16, (int) (66 * ((float) player.fuel / (float) player.maxFuel)));

			levelText.draw(hudBatch, "Level: " + currentLevel, levelTextX, levelTextY);

			hudBatch.draw(manabarHolderImg, 50, 347, 20, 68);

			drawAmmoText();
			drawCoinText();


			hudBatch.draw(healthBarImg, 32, 349, 16, 66 * ((float) player.health / (float) player.maxHealth));
			hudBatch.draw(healthbarHolderImg, 30, 347, 20, 68);

			wepSelect1.draw(hudBatch, player.heldWeapons[findLastIndex()]);
			wepSelect2.draw(hudBatch, player.heldWeapons[0]);
			wepSelect3.draw(hudBatch, player.heldWeapons[1]);
			if (gameState == 3) {
				deathMenu.draw(hudBatch);
			}
			hudBatch.end();



		}
		drawBlackBG();

	}

	public int findLastIndex() {
		for (int i = 0; i < player.heldWeapons.length; i++) {
			if (player.heldWeapons[i] == 0) {
				return (i-1);
			}
		}
		return 0;
	}

	public void drawBlackBG() {
		if (BlackFadeInBG.getInstance().opacity > .05) {
			blackFadeBatch.begin();

			blackFadeBatch.setColor(1.0f, 1.0f, 1.0f, (float)BlackFadeInBG.getInstance().opacity);
			BlackFadeInBG.getInstance().draw(blackFadeBatch);
			blackFadeBatch.end();
		}
	}

	private void drawAmmoText() {
		if (player.gunType == 1) {
			ammoText.draw(hudBatch,696969,696969);
		} else if (player.gunType == 2) {
			ammoText.draw(hudBatch, player.cannonAmmo, player.maxCannonAmmo);
		} else if (player.gunType == 3) {
			ammoText.draw(hudBatch, player.machineAmmo, player.maxMachineAmmo);
		} else if (player.gunType == 4) {
			ammoText.draw(hudBatch, player.flameAmmo,player.maxFlameAmmo);
		} else if (player.gunType == 5) {
			ammoText.draw(hudBatch, player.rocketAmmo, player.maxRocketAmmo);
		} else if (player.gunType == 6) {
			ammoText.draw(hudBatch, player.grenadeAmmo, player.maxGrenadeAmmo);
		}
	}
	private void drawCoinText() {
		coinText.draw(hudBatch,player.numCoins);
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

	//resets level and player's health, clears all objects
	public void prepareForNewGame() {
		player.gameOver = false;
		System.out.println("Game Over!");
		System.out.println("Previous level reached:" + currentLevel);
		if (currentLevel > maxLevelReached) {
			System.out.println("New high score!");
			maxLevelReached = currentLevel;
		}
		currentLevel = 1;
		player.health = 10;
		System.out.println("Max level reached:" + maxLevelReached);
		//resets player weapons
		for(int i= 0; i< player.heldWeapons.length; i++) {
			player.heldWeapons[i] = 0;
		}
		player.heldWeapons[0] = 1; // unlocks pistol
		player.heldWeapons[1] = 2;
		clearOldLevel();
	}

	public void clearOldLevel() {
		mgRobot1s.clear();
		mgEnemies.clear();
		mgEnemyBullets.clear();
		mgItems.clear();
		mgBaseTiles.clear();
		mgPlayerBullets.clear();
		mgParticle1s.clear();
		mgPassiveTiles.clear();

		player.x = 800 / 2 - 64 / 2;
		player.y = (levelMap[1].length / 2) * 32;


	}

	//clears all mgItems from map and resets player's position
	public void generateNewLevel() {
		mgRobot1s.clear();
		mgEnemies.clear();
		mgEnemyBullets.clear();
		mgItems.clear();
		mgBaseTiles.clear();
		mgPlayerBullets.clear();
		mgParticle1s.clear();
		mgPassiveTiles.clear();

		player.x = 800 / 2 - 64 / 2;
		player.y = (levelMap[1].length / 2) * 32;

		createEntireLevel();



	}

	public void addMiscItems() {

		boolean hasSpawnedShopDoor = false; // says whether a shop door has already been spawned so it wont make another one if there arleady is one
		boolean hasSpawnedItemRoomDoor = false; // same as above but for item room
		//NOTE: these make it so spawned shops/item rooms are more likely to be towards the beginning of the level but oh well what can you do



		for (BaseTile baseTile : mgBaseTiles) {
			if (!baseTile.coveredBottom && !baseTile.isEdgeTile) {
				int spawnRoll = random.nextInt(1000);
				int numTypes = 7; // how many types of things there are you can spawn
				int itemFrequency = 50; // tells how often to spawn mgItems on tiles
				if (spawnRoll > itemFrequency) {
					//do nothing, therefore only make mgItems in 1/20 tiles
				} else if (spawnRoll > (6*itemFrequency) / numTypes) {
					System.out.println("trying to spawn item room door");
					if (!hasSpawnedItemRoomDoor) {
						System.out.println("spawned!");
						ItemRoomDoor itemDoor = new ItemRoomDoor(baseTile.x, baseTile.y + baseTile.height, 0, 0, 32, 32);
						mgItems.add(itemDoor);
						hasSpawnedItemRoomDoor = true;
					}
				} else if (spawnRoll > (5*itemFrequency) / numTypes) {
					if (!hasSpawnedShopDoor) {
						ShopDoor sdoor = new ShopDoor(baseTile.x, baseTile.y + baseTile.height, 0, 0, 32, 32);
						mgItems.add(sdoor);
						hasSpawnedShopDoor = true;
					}

				} else if (spawnRoll > (4*itemFrequency) / numTypes) {
					// smart enemy
					SmartEnemy enemy = new SmartEnemy();
					enemy.x = baseTile.x;
					enemy.y = baseTile.y + baseTile.height;
					enemy.width = 32;
					enemy.height = 32;
					mgEnemies.add(enemy);
				} else if (spawnRoll > (3*itemFrequency) / numTypes) {
					// flying enemy
					Roboto1 robot = new Roboto1();
					robot.x = baseTile.x;
					robot.y = baseTile.y + baseTile.height;
					robot.width = 32;
					robot.height = 32;
					robot.flying = true;
					mgEnemies.add(robot);
				} else if (spawnRoll > (2*itemFrequency) / numTypes) {
					// shooting enemy
					ShootingGuy enemy = new ShootingGuy();
					enemy.x = baseTile.x;
					enemy.y = baseTile.y + baseTile.height;
					enemy.width = 32;
					enemy.height = 32;
					mgEnemies.add(enemy);
				} else if (spawnRoll > (itemFrequency) / numTypes) {
					// normal enemy
					Roboto1 robot = new Roboto1();
					robot.x = baseTile.x;
					robot.y = baseTile.y + baseTile.height;
					robot.width = 32;
					robot.height = 32;
					robot.flying = false;
					mgEnemies.add(robot);
				} else {
					// box
					BaseBox box = new BaseBox(baseTile.x, baseTile.y + baseTile.height,0,0,22,22);
					mgEnemies.add(box);
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
