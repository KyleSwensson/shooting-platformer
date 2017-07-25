package com.mygdx.game2;

import com.badlogic.gdx.utils.Array;

import java.rmi.server.Skeleton;
import java.util.Random;

/**
 * Created by kyles on 2/26/2017.
 */
public class EnemySpawner {

 //TODO: should either store the list of all top tiles, and store the list of all edge tiles
 //TODO: maybe should remove tiles after have spawned something on them so 2 things wont spawn on them



    private static final int TILE_HEIGHT = 32;

    private static Random random = new Random();

    private static final int NUM_ENEMY_TYPES = 15;

    public void spawnEnemies(int difficultyPoints, Array<BaseTile> baseTiles) {
         while (difficultyPoints > 0) {
             int spawnType = random.nextInt(NUM_ENEMY_TYPES) + 1;
             BaseTile spawnTile;
             switch(spawnType) {
                 case 1:
                     difficultyPoints -= GroundSpikes.DIFFICULTY_POINTS;
                     spawnTile = spawnTopOfTileEnemy(baseTiles, spawnType);
                     spawnSpikesAtTile(spawnTile);
                     break;
                 case 2:
                     difficultyPoints -= Roboto1.DIFFICULTY_POINTS;
                     spawnTile = spawnTopOfTileEnemy(baseTiles, spawnType);
                     spawnSlimeAtTile(spawnTile);
                     break;
                 case 3:
                     //flying robot instead of ground robot here
                     difficultyPoints -= Roboto1.DIFFICULTY_POINTS;
                     //should maybe have different spawning mechanism for flying enemies, maybe only spawns on the bottom of tiles for bats
                     spawnTile = spawnTopOfTileEnemy(baseTiles, spawnType);
                     spawnBatAtTile(spawnTile);
                     break;
                 case 4:
                     difficultyPoints -= ShootingGuy.DIFFICULTY_POINTS;
                     spawnTile = spawnTopOfTileEnemy(baseTiles, spawnType);
                     spawnShootingGuyAtTile(spawnTile);
                     break;
                 case 5:
                     difficultyPoints -= SkeletonFloatingHead.DIFFICULTY_POINTS;
                     spawnTile = spawnTopOfTileEnemy(baseTiles, spawnType);
                     spawnSkeletonFloatingHeadAtTile(spawnTile);
                     break;
                 case 6:
                     difficultyPoints -= Ghosto.DIFFICULTY_POINTS;
                     spawnTile = spawnTopOfTileEnemy(baseTiles, spawnType);
                     spawnGhostoAtTile(spawnTile);
                     break;
                 case 7:
                     difficultyPoints -= SmartEnemy.DIFFICULTY_POINTS;
                     spawnTile = spawnTopOfTileEnemy(baseTiles, spawnType);
                     spawnSkeletonAtTile(spawnTile);
                     break;
                 case 8:
                     difficultyPoints -= Scorpain.DIFFICULTY_POINTS;
                     spawnTile = spawnTopOfTileEnemy(baseTiles, spawnType);
                     spawnScorpainAtTile(spawnTile);
                     break;
                 case 9:
                     difficultyPoints -= FetyrBowman.DIFFICULTY_POINTS;
                     spawnTile = spawnTopOfTileEnemy(baseTiles, spawnType);
                     spawnFetyrBowmanAtTile(spawnTile);
                     break;
                 case 10:
                     difficultyPoints -= FetyrBomber.DIFFICULTY_POINTS;
                     spawnTile = spawnTopOfTileEnemy(baseTiles, spawnType);
                     spawnFetyrBomberAtTile(spawnTile);
                     break;
                 case 11:
                     difficultyPoints -= CirclingBallOnChain.DIFFICULTY_POINTS;
                     spawnTile = spawnAnyEdgeTileEnemy(baseTiles, spawnType);
                     spawnCirclingBallOnChainAtTile(spawnTile);
                     break;
                 case 12:
                     difficultyPoints -= BlowDartShooter.DIFFICULTY_POINTS;
                     spawnTile = spawnAnyEdgeTileEnemy(baseTiles, spawnType);
                     spawnBlowDartShooterAtTile(spawnTile);
                     break;
                 case 13:
                     difficultyPoints -= FireballShooter.DIFFICULTY_POINTS;
                     spawnTile = spawnAnyEdgeTileEnemy(baseTiles, spawnType);
                     spawnFireballShooterAtTile(spawnTile);
                     break;
             }
         }
    }

    private BaseTile spawnTopOfTileEnemy(Array<BaseTile> baseTiles, int enemyType) {
        Array<BaseTile> tileArray = new Array<BaseTile>();
        for (int i = 0; i < baseTiles.size; i++) {
            if (!baseTiles.get(i).coveredBottom && !baseTiles.get(i).isEdgeTile) {
                tileArray.add(baseTiles.get(i));
            }
        }

        return tileArray.get(random.nextInt(tileArray.size));
    }


    private BaseTile spawnAnyEdgeTileEnemy(Array<BaseTile> baseTiles, int enemyType) {
        Array<BaseTile> tileArray = new Array<BaseTile>();
        for (int i = 0; i < baseTiles.size; i++) {
            if (!baseTiles.get(i).isEdgeTile && (!baseTiles.get(i).coveredLeft || !baseTiles.get(i).coveredRight || !baseTiles.get(i).coveredBottom || !baseTiles.get(i).coveredTop)) {
                tileArray.add(baseTiles.get(i));
            }
        }

        return tileArray.get(random.nextInt(tileArray.size));
    }

    private void spawnSpikesAtTile(BaseTile tileToSpawnOn) {
        GroundSpikes spikes = new GroundSpikes(tileToSpawnOn.x, tileToSpawnOn.y + TILE_HEIGHT);
        GdxShooter2.mgEnemies.add(spikes);
    }
    private void spawnSlimeAtTile(BaseTile tileToSpawnOn) {
        Roboto1 enemy = new Roboto1(tileToSpawnOn.x, tileToSpawnOn.y + TILE_HEIGHT, false);
        GdxShooter2.mgEnemies.add(enemy);
    }
    private void spawnBatAtTile(BaseTile tileToSpawnOn) {
        Roboto1 enemy = new Roboto1(tileToSpawnOn.x, tileToSpawnOn.y + TILE_HEIGHT, false);
        GdxShooter2.mgEnemies.add(enemy);
    }
    private void spawnSkeletonFloatingHeadAtTile(BaseTile tileToSpawnOn) {
        SkeletonFloatingHead skelehead = new SkeletonFloatingHead(tileToSpawnOn.x, tileToSpawnOn.y + TILE_HEIGHT);
        GdxShooter2.mgEnemies.add(skelehead);
    }
    private void spawnGhostoAtTile(BaseTile tileToSpawnOn) {
        Ghosto ghosto = new Ghosto(tileToSpawnOn.x, tileToSpawnOn.y + TILE_HEIGHT);
        GdxShooter2.mgEnemies.add(ghosto);
    }
    private void spawnShootingGuyAtTile(BaseTile tileToSpawnOn) {
        ShootingGuy enemy = new ShootingGuy(tileToSpawnOn.x, tileToSpawnOn.y + TILE_HEIGHT);
        GdxShooter2.mgEnemies.add(enemy);
    }
    private void spawnSkeletonAtTile(BaseTile tileToSpawnOn) {
        SmartEnemy enemy = new SmartEnemy(tileToSpawnOn.x, tileToSpawnOn.y + TILE_HEIGHT);
        GdxShooter2.mgEnemies.add(enemy);
    }
    private void spawnScorpainAtTile(BaseTile tileToSpawnOn) {
        Scorpain enemy = new Scorpain(tileToSpawnOn.x, tileToSpawnOn.y + TILE_HEIGHT);
        GdxShooter2.mgEnemies.add(enemy);
    }
    private void spawnFetyrBowmanAtTile(BaseTile tileToSpawnOn) {
        FetyrBowman enemy = new FetyrBowman(tileToSpawnOn.x, tileToSpawnOn.y + TILE_HEIGHT);
        GdxShooter2.mgEnemies.add(enemy);
    }
    private void spawnFetyrBomberAtTile(BaseTile tileToSpawnOn) {
        FetyrBomber enemy = new FetyrBomber(tileToSpawnOn.x, tileToSpawnOn.y + TILE_HEIGHT);
        GdxShooter2.mgEnemies.add(enemy);
    }
    private void spawnCirclingBallOnChainAtTile(BaseTile tileToSpawnOn) {
        CirclingBallOnChain enemy = new CirclingBallOnChain(tileToSpawnOn.x + (TILE_HEIGHT / 2), tileToSpawnOn.y + (TILE_HEIGHT / 2));
        GdxShooter2.mgEnemies.add(enemy);
    }
    private void spawnBlowDartShooterAtTile(BaseTile tileToSpawnOn) {

        int orientation = chooseOrientationForTile(tileToSpawnOn);
        BlowDartShooter enemy;
        System.out.println("spawning shooter " + orientation);
        switch (orientation) {
            case 1:
                enemy = new BlowDartShooter(tileToSpawnOn.x + TILE_HEIGHT, tileToSpawnOn.y, orientation);
                GdxShooter2.mgEnemies.add(enemy);
                break;
            case 2:
                enemy = new BlowDartShooter(tileToSpawnOn.x - TILE_HEIGHT, tileToSpawnOn.y, orientation);
                GdxShooter2.mgEnemies.add(enemy);
                break;
            case 3:
                enemy = new BlowDartShooter(tileToSpawnOn.x, tileToSpawnOn.y - TILE_HEIGHT, orientation);
                GdxShooter2.mgEnemies.add(enemy);
                break;
            case 4:
                enemy = new BlowDartShooter(tileToSpawnOn.x, tileToSpawnOn.y + TILE_HEIGHT, orientation);
                GdxShooter2.mgEnemies.add(enemy);
                break;

        }
    }
    private void spawnFireballShooterAtTile(BaseTile tileToSpawnOn) {
        int orientation = chooseOrientationForTile(tileToSpawnOn);
        FireballShooter enemy;
        switch (orientation) {
            case 1:
                enemy = new FireballShooter(tileToSpawnOn.x + 32, tileToSpawnOn.y, orientation);
                GdxShooter2.mgEnemies.add(enemy);
                break;
            case 2:
                enemy = new FireballShooter(tileToSpawnOn.x - 32, tileToSpawnOn.y, orientation);
                GdxShooter2.mgEnemies.add(enemy);
                break;
            case 3:
                enemy = new FireballShooter(tileToSpawnOn.x, tileToSpawnOn.y - 32, orientation);
                GdxShooter2.mgEnemies.add(enemy);
                break;
            case 4:
                enemy = new FireballShooter(tileToSpawnOn.x, tileToSpawnOn.y + 32, orientation);
                GdxShooter2.mgEnemies.add(enemy);
                break;

        }
    }

    private int chooseOrientationForTile(BaseTile tile) {
        boolean[] possibleOrientations = new boolean[]{tile.coveredRight, tile.coveredLeft, tile.coveredTop,tile.coveredBottom};
        int orientation = random.nextInt(4) + 1;
        while (possibleOrientations[orientation - 1]) {
            orientation = random.nextInt(4) + 1;
        }
        return orientation;
    }



}
