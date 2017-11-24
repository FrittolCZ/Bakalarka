package com.mygdx.game.GameClasses;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by frittol on 9.3.17.
 */

public class GameMap {

    private Node start, end;
    private int tileSize; // velikost jednoho políčka mapy
    private int mapWidth;
    private int mapHeight;

    private Node[][] mapGraph;

    public GameMap(TiledMap map) {
        this.tileSize = map.getProperties().get("tilewidth", Integer.class);
        this.mapWidth = map.getProperties().get("width", Integer.class);
        this.mapHeight = map.getProperties().get("height", Integer.class);
        this.mapGraph = new Node[mapWidth][mapHeight];
        initializeMap(map);
    }

    /**
     * Creates node for every tile of map and if there is obstacle in Obstacle layer of map deactivates node
     *
     * @param map - TiledMap object loaded from tiled map file
     */
    private void initializeMap(TiledMap map) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("Obstacles");
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                TiledMapTileLayer.Cell cell = layer.getCell(i, j);
                mapGraph[i][j] = new Node(new Vector2(i * tileSize, j * tileSize), null);
                if (cell != null)
                    mapGraph[i][j].deactivate();
            }
        }
        int startX = map.getProperties().get("StartX", Integer.class);
        int startY = map.getProperties().get("StartY", Integer.class);
        int endX = map.getProperties().get("EndX", Integer.class);
        int endY = map.getProperties().get("EndY", Integer.class);

        this.start = mapGraph[startX][startY];
        this.end = mapGraph[endX][endY];
    }

    public ArrayList<Node> getAvailableNeighbors(Node parent) {
        Vector2[] coeficients = {new Vector2(0, 1), new Vector2(1, 0), new Vector2(-1, 0), new Vector2(0, -1)};
        ArrayList<Node> neighbors = new ArrayList<Node>();
        int x = (int) parent.getPosition().x / tileSize;
        int y = (int) parent.getPosition().y / tileSize;
        for (Vector2 v : coeficients) {
            if (x + (int) v.x >= 0 && x + (int) v.x <= mapWidth - 1
                    && y + (int) v.y >= 0 && y + (int) v.y <= mapHeight - 1) {
                Node n = mapGraph[x + (int) v.x][y + (int) v.y];
                if (n.isActive()) {
                    neighbors.add(n);
                }
            }
        }
        return neighbors;
    }

    public void clearMap() {
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                if (mapGraph[i][j].isActive()) {
                    mapGraph[i][j].setH(0);
                    mapGraph[i][j].setParent(null);
                    mapGraph[i][j].setG(0);
                }
            }
        }
    }

    public Node getStart() {
        return start;
    }

    public Node getEnd() {
        return end;
    }

    public int getTileSize() {
        return tileSize;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public Node getNodeOnPosition(int x, int y) {
        return mapGraph[x][y];
    }

    public Node getNodeOnPosition(Vector2 v) {
        return mapGraph[(int) v.x / tileSize][(int) v.y / tileSize];
    }

    public boolean isThisNodeEmpty(Vector2 position) {
        return getNodeOnPosition(position).isActive();
    }
}
