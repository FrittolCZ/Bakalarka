package com.mygdx.game.GameClasses.WaveManager;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Queue;
import com.mygdx.game.GameClasses.Enemy;
import com.mygdx.game.GameClasses.PathfinderAlgorithms.Pathfinder;
import com.mygdx.game.screens.GameScreen;

import java.util.ArrayList;


public class Wave {

    private int numOfEnemies; // Number of enemies to spawn
    private int waveNumber; // Number of wave
    private float spawnTimer; // Timer to spawn an enemy
    private boolean waveEnd;
    private boolean spawningEnemies; // True if we are still spawning enemies
    private int enemiesAtEnd;
    private int earnedMoney;
    private TextureRegion enemyTexture;
    private Pathfinder pathfinder;
    private ArrayList<Enemy> enemies;

    //Constructor

    public Wave(int waveNumber, int numOfEnemies, Pathfinder pathfinder) {
        this.numOfEnemies = numOfEnemies;
        this.waveNumber = waveNumber;
        this.pathfinder = pathfinder;
        this.enemyTexture = GameScreen.myTextures.findRegion("enemyTrooper1");
        this.spawnTimer = 2;
        this.waveEnd = false;
        this.enemiesAtEnd = 0;
        this.earnedMoney = 0;
        this.enemies = new ArrayList<Enemy>();
    }

    //Properties

    public int getEnemiesAtEnd() {
        return enemiesAtEnd;
    }

    public int getEarnedMoney() {
        return earnedMoney;
    }

    public boolean waveEnd() {
        return waveEnd;
    }

    public int getWaveNumber() {
        return waveNumber;
    }

    public Enemy enemyAtThisPosition(Vector2 clickPosition) {
        Enemy enemy = null;
        for (Enemy e : enemies) {
            if (e.getPosition().dst(clickPosition) <= 24) {
                enemy = e;
            }
        }
        return enemy;
    }

    //Methods

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    private void addEnemy() {
        Queue<Vector2> path = pathfinder.getPath();
        Enemy e = new Enemy(enemyTexture, path.first(), 200, 1, 0.5f);
        e.setPath(path, true);
        enemies.add(e);
        spawnTimer = 0;
        numOfEnemies--;
    }

    public void zeroCollectors() {
        earnedMoney = 0;
        enemiesAtEnd = 0;
    }

    public void start() {
        spawningEnemies = true;
    }

    public void update(float delta) {
        earnedMoney = 0;
        enemiesAtEnd = 0;

        if (numOfEnemies > 0) {
            spawnTimer += delta;
            if (spawnTimer >= 2) {
                addEnemy();
            }
        }
        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            if (enemy.getCurrentPath().size <= 0 || !enemy.isAlive()) {
                if (!enemy.isAlive()) {
                    earnedMoney += enemy.getBounty();
                } else {
                    enemiesAtEnd++;
                }
                enemies.remove(enemy);
                i--;
            } else enemy.update();
        }
        if (enemies.size() == 0) {
            waveEnd = true;
        }
    }

    public void draw(Batch batch) {
        for (Enemy e : enemies) {
            e.draw(batch);
        }
    }


}
