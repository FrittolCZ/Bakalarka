package com.mygdx.game.GameClasses.WaveManager;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameClasses.Enemy;
import com.mygdx.game.GameClasses.GameMode;
import com.mygdx.game.GameClasses.PathfinderAlgorithms.Pathfinder;

import java.util.ArrayList;

public class WaveManager {

    private float timeToNextWave; // How long since the last wave ended
    private ArrayList<Wave> waves; // A queue of all our waves
    private Pathfinder pathfinder;
    private int moneyEarnedAll;
    private int enemiesAtEndAll;

    private GameMode mode;

    private boolean spawn = false;

    public WaveManager(Pathfinder pathfinder, GameMode mode) {
        this.pathfinder = pathfinder;
        waves = new ArrayList<Wave>();

        this.moneyEarnedAll = 0;
        this.enemiesAtEndAll = 0;
        this.mode = mode;
        if (mode == GameMode.MARATHON) {
            spawn = true;
            this.timeToNextWave = 30;
        } else {
            this.timeToNextWave = 1;
        }
    }

    public int getMoneyEarnedAll() {
        return moneyEarnedAll;
    }

    public int getEnemiesAtEndAll() {
        return enemiesAtEndAll;
    }

    public float getTimeToNextWave() {
        return timeToNextWave;
    }

    public ArrayList<Wave> getWaves() {
        return waves;
    }

    public Enemy enemyAtThisPosition(Vector2 position) {
        Enemy e = null;
        for (Wave w : waves) {
            e = w.enemyAtThisPosition(position);
        }
        return e;
    }

    public void recalcPath() {
        for (Wave w : waves) {
            for (Enemy e : w.getEnemies()) {
                Vector2 newStart = e.getNext();
                e.setPath(pathfinder.getPath(newStart), false);
            }
        }
    }

    private void startNextWave() {
        if (mode == GameMode.TIMED) {
            waves.add(new Wave(waves.size(), 1, pathfinder));
            timeToNextWave = 3;
        } else {
            waves.add(new Wave(waves.size(), 6, pathfinder));
            timeToNextWave = 30;
        }
    }

    public void update(float delta) {
        enemiesAtEndAll = 0;
        moneyEarnedAll = 0;
        for (Wave wave : waves) {
            if (!wave.waveEnd()) {
                wave.update(delta);
            } else {
                enemiesAtEndAll += wave.getEnemiesAtEnd();
                moneyEarnedAll += wave.getEarnedMoney();
                wave.zeroCollectors();
            }
        }
        if (spawn) {
            timeToNextWave -= delta;
        }
        if (timeToNextWave <= 0) {
            startNextWave();
        }
    }

    public void startSpawn() {
        spawn = true;
    }

    public boolean isSpawning() {
        return spawn;
    }

    public void draw(Batch batch) {
        for (Wave wave : waves) {
            if (!wave.waveEnd()) {
                wave.draw(batch);
            }
        }
    }

}
