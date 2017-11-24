package com.mygdx.game.GameClasses;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameClasses.TowerTypes.CannonTower;
import com.mygdx.game.GameClasses.TowerTypes.DoubleCannonTower;
import com.mygdx.game.GameClasses.TowerTypes.TowerType;
import com.mygdx.game.GameClasses.WaveManager.Wave;
import com.mygdx.game.screens.GameScreen;

import java.util.ArrayList;


public class TowerManager {

    private static ArrayList<Tower> towers;
    private TextureRegion towerBase;
    private Tower selected = null;
    private ArrayList<Wave> waves;

    public TowerManager(ArrayList<Wave> waves) {
        towers = new ArrayList<Tower>();
        this.waves = waves;
        towerBase = GameScreen.myTextures.findRegion("towerBase2");
    }

    public Tower towerAtThisPosition(Vector2 pos) {
        Tower tower = null;
        for (Tower t : towers) {
            if (t.getPosition().dst(pos) < 64) {
                tower = t;
            }
        }
        return tower;
    }

    public void createTower(Vector2 pos, TowerType type) {
        Tower t;
        switch (type) {
            case CANNON:
                t = new CannonTower(pos, type);
                break;
            case DOUBLE_CANNON:
                t = new DoubleCannonTower(pos, type);
                break;
            default:
                t = new CannonTower(pos, type);
                break;
        }
        towers.add(t);
    }

    public void setSelected(Tower t) {
        selected = t;
    }

    public void target(Enemy e) {
        for (Tower t : towers) {
            if (t.isInRange(e.getPosition())) {
                t.setTarget(e);
            }
        }
    }

    public void update(float delta) {
        for (Tower t : towers) {
            if (t.getTarget() == null) {
                t.getClosestEnemy(waves);
            }
            t.update(delta);
        }
    }

    public void draw(Batch batch) {
        for (Tower t : towers) {
            batch.draw(towerBase, t.getX(), t.getY());
            t.draw(batch);
        }
    }

    public void removeTower(Tower t) {
        towers.remove(t);
    }
}
