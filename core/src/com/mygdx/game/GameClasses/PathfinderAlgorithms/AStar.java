package com.mygdx.game.GameClasses.PathfinderAlgorithms;


import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameClasses.GameMap;
import com.mygdx.game.GameClasses.Node;

import java.util.ArrayList;
import java.util.TreeSet;

import static java.lang.Math.abs;


public class AStar extends Dijkstra {


    public AStar(GameMap map) {
        super(map);
        Node start = map.getStart();
        start.setH(manhattanDst(start.getPosition(), map.getEnd().getPosition()));
    }

    private int manhattanDst(Vector2 v, Vector2 u) {
        return (int) (abs(v.x - u.x) + abs(v.y - u.y));
    }

    @Override
    protected void addNeighborNodes(Node start, TreeSet<Node> open, ArrayList<Node> neighbors, Node parrent) {
        for (Node n : neighbors) { // projde všechny okolní uzly právě vyhodnocovaného uzlu
            n.setH(manhattanDst(n.getPosition(), map.getEnd().getPosition()));
            int f = parrent.getG() + map.getTileSize() + n.getH(); // vypočítá hodnotu f pro tento uzel
            if (n.getParent() == null) { //pokud nemá rodiče
                if (!n.equals(start)) { // a pokud není start který nemá rodiče nikdy
                    n.setG(parrent.getG() + map.getTileSize());
                    n.setParent(parrent); // pak mu nastaví rodiče a nastaví G
                    open.add(n);
                }
            } else { // pokud má rodiče znamená to že do tohoto uzlu už sme někdy došli
                if(n.getF() > f) // porovnáme f, pokud sme došli kratší cestou
                {
                    n.setG(parrent.getG() + map.getTileSize()); // nastavíme novou hodnotu G -- F = G + H
                    n.setParent(parrent); // a změníme rodiče
                }
            }
        }
    }
}