package com.mygdx.game.GameClasses.PathfinderAlgorithms;

import com.mygdx.game.GameClasses.GameMap;
import com.mygdx.game.GameClasses.Node;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by fanda on 18.06.2017.
 */

public class DFS extends Pathfinder {

    public DFS(GameMap map) {
        super(map);
    }

    protected boolean pathfind(Node start) {
        //Fronta uzlů k vyhodnocení
        Stack<Node> open = new Stack<Node>();
        open.push(start); // Na začátku open obsahuje pouze počáteční uzel

        while (!open.isEmpty()) { // Dokud fronta uzlů k vyhodnocení není prázna
            Node current = open.pop();

            if (current.equals(map.getEnd())) // pokud je tento uzel koncový
            {
                reconstructPath(current); // přepočítám cestu z něj k počátku
                return true; // a ukončím výpočet
            }
            addNeighborNodes(start, open, map.getAvailableNeighbors(current), current);
        }
        map.clearMap();
        return false;
    }

    private void addNeighborNodes(Node start, Stack<Node> open, ArrayList<Node> neighbors, Node parrent) {
        for (Node n : neighbors) { // projde všechny okolní uzly právě vyhodnocovaného uzlu
            if (n.getParent() == null) { //pokud nemá rodiče
                if (!n.equals(start)) { // a pokud není start který nemá rodiče nikdy
                    n.setParent(parrent); // pak mu nastaví rodiče
                    open.push(n); // a vloží ho na začátek
                }
            }
        }
    }

}
