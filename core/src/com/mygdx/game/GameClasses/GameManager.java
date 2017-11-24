package com.mygdx.game.GameClasses;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.mygdx.game.GameClasses.PathfinderAlgorithms.AStar;
import com.mygdx.game.GameClasses.PathfinderAlgorithms.DFS;
import com.mygdx.game.GameClasses.PathfinderAlgorithms.Dijkstra;
import com.mygdx.game.GameClasses.PathfinderAlgorithms.GBestFirst;
import com.mygdx.game.GameClasses.PathfinderAlgorithms.Pathfinder;
import com.mygdx.game.GameClasses.WaveManager.WaveManager;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.screens.GameOverScreen;
import com.mygdx.game.screens.GameScreen;


public class GameManager {

    private int lives = 5;
    private int money = 80;

    private int score = 0;

    private float timer = 360;

    private TowerManager towerManager;
    private WaveManager waveManager;
    private Pathfinder pathfinder;
    private Pathfinder checkPathfinder;
    private GameMap gameMap;
    private GameMode mode;
    private int diffLvl;


    /**
     * Creates new game with selected map, game mode and difficulty level
     *
     * @param map     Selected map
     * @param mode    Selected game mode
     * @param diffLvl Selected difficulty levels
     */
    public GameManager(TiledMap map, GameMode mode, int diffLvl) {
        this.gameMap = new GameMap(map);
        this.pathfinder = new AStar(gameMap);
        this.checkPathfinder = new DFS(gameMap);
        this.waveManager = new WaveManager(pathfinder, mode);
        this.towerManager = new TowerManager(waveManager.getWaves());
        this.mode = mode;
        this.diffLvl = diffLvl;
    }

    public TowerManager getTowerManager() {
        return towerManager;
    }

    public WaveManager getWaveManager() {
        return waveManager;
    }

    public void removeTower(Tower t) {
        towerManager.removeTower(t);
        gameMap.getNodeOnPosition(t.getPosition()).activate();
    }

    public void takeMoney(int amount) {
        this.money -= amount;
    }

    public void addMoney(int amount) {
        this.money += amount;
    }

    public int getMoney() {
        return money;
    }

    public int getLives() {
        return lives;
    }

    public GameMode getGameMode() {
        return mode;
    }

    public void update(float delta) {
        lives -= waveManager.getEnemiesAtEndAll();
        money += waveManager.getMoneyEarnedAll();
        score += waveManager.getMoneyEarnedAll();
        towerManager.update(delta);
        waveManager.update(delta);

        if (this.mode == GameMode.TIMED && waveManager.isSpawning()) {
            timer -= delta;
        }

        if (lives <= 0 || timer <= 0) {
            GameScreen game = (GameScreen) MyGdxGame.screens.get(MyGdxGame.GAME_SCREEN);
            game.stopGame();
            GameOverScreen screen = (GameOverScreen) MyGdxGame.screens.get(MyGdxGame.GAME_OVER_SCREEN);
            screen.setDiffLlv(diffLvl);
            screen.setGameMode(mode);
            screen.setScore(score + lives * 50);
            ((Game) Gdx.app.getApplicationListener()).setScreen(screen);
        }
    }

    public void draw(Batch batch) {
        waveManager.draw(batch);
        towerManager.draw(batch);
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public Pathfinder getCheckPathfinder() {
        return checkPathfinder;
    }

    public void setPathfinder(int selPathfinder) {
        switch (selPathfinder) {
            case 0:
                this.pathfinder = new DFS(gameMap);
                break;
            case 1:
                this.pathfinder = new Dijkstra(gameMap);
                break;
            case 2:
                this.pathfinder = new AStar(gameMap);
                break;
            case 3:
                this.pathfinder = new GBestFirst(gameMap);
            default:
                this.pathfinder = new AStar(gameMap);
        }
    }

    public float getTimer() {
        return timer;
    }

    public int getScore() {
        return score;
    }
}



