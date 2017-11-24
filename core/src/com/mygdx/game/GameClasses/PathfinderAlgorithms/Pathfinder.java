package com.mygdx.game.GameClasses.PathfinderAlgorithms;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Queue;
import com.mygdx.game.GameClasses.Enemy;
import com.mygdx.game.GameClasses.GameMap;
import com.mygdx.game.GameClasses.Node;
import com.mygdx.game.GameClasses.WaveManager.Wave;

import java.util.ArrayList;

public abstract class Pathfinder {
    protected GameMap map;
    protected Queue<Vector2> path;

    public Pathfinder(GameMap map) {
        path = new Queue<Vector2>();
        this.map = map;
    }

    /**
     * Checks if creation of barrier on position, does'nt prevent some enemy from reaching end of map
     *
     * @param position
     * @return
     */
    public boolean checkPathExist(Vector2 position, ArrayList<Wave> waves) {
        // ověřím zda bod kde chci vytvořit překážku není start nebo konec
        if (map.getStart().getPosition().equals(position) || map.getEnd().getPosition().equals(position)) {
            return false;
        }

        // deaktivuji bod na kterém chci postavit překážku

        map.getNodeOnPosition(position).deactivate();

        // Pak ověří zda vytvořením překážky nezanikne cesta z počátku
        if (!pathfind(map.getStart())) {
            map.getNodeOnPosition(position).activate();
            return false;
        }

        // Poté ověřím pro každého enemy jestli vytvořením překážky mu nezabráním dosažení cíle
        for (Wave w : waves) {
            for (Enemy e : w.getEnemies()) {
                // Zálohuji si předchozí cestu
                Queue<Vector2> lastPath = new Queue<Vector2>();
                for (Vector2 v : e.getCurrentPath()) {
                    lastPath.addLast(v);
                }

                // Pak zkusím najít novou cestu, pokud cestu nenajdu
                if (!pathfind(map.getNodeOnPosition(e.getNext()))) {
                    // vrátím původní cestu
                    e.setPath(lastPath, false);
                    // aktivuji původní bod cesty
                    map.getNodeOnPosition(position).activate();
                    return false;
                }
            }
        }
        map.getNodeOnPosition(position).activate();
        return true;
    }

    protected abstract boolean pathfind(Node start);

    protected void reconstructPath(Node node) {
        Node local = new Node(node.getPosition(), node.getParent());
        local.setParent(node.getParent());
        do {
            path.addFirst(local.getPosition());
            local = local.getParent();
        } while (local.getParent() != null);
        if (local != null) {
            path.addFirst(local.getPosition());
        }
        map.clearMap();
    }

    //Vrátí trasu z počátku mapy
    public Queue<Vector2> getPath() {
        path.clear();
        pathfind(map.getStart());
        return path;
    }

    //Vrátí trasu z aktuální pozice enemy
    public Queue<Vector2> getPath(Vector2 actualPos) {
        path.clear();
        pathfind(map.getNodeOnPosition(actualPos));
        return path;
    }
}
