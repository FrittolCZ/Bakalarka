package com.mygdx.game.GameClasses.PathfinderAlgorithms;

import com.mygdx.game.GameClasses.GameMap;
import com.mygdx.game.GameClasses.Node;

import java.util.ArrayList;
import java.util.TreeSet;


public class Dijkstra extends Pathfinder {


    public Dijkstra(GameMap map) {
        super(map);
    }


    protected void addNeighborNodes(Node start, TreeSet<Node> open, ArrayList<Node> neighbors, Node parrent) {
        for (Node n : neighbors) { // projde všechny okolní uzly právě vyhodnocovaného uzlu
            int f = parrent.getG() + map.getTileSize(); // vypočítá hodnotu f pro tento uzel
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

    protected boolean pathfind(Node start) {
        TreeSet<Node> open = new TreeSet<Node>(); // OPEN list
        open.add(start); // Na začátku open obsahuje pouze počáteční uzel

        while (!open.isEmpty()) {
            Node current = open.pollFirst(); //Vezme první
            if (current.equals(map.getEnd())) // pokud je tento uzel koncový
            {
                reconstructPath(current); // přepočítám cestu z něj k počátku
                return true; // a ukončím výpočet
            }
            addNeighborNodes(start, open,map.getAvailableNeighbors(current), current); // jinak přidá sousední uzly
        }
        map.clearMap();
        return false;
    }

}
