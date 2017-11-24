package com.mygdx.game.GameClasses;

import com.badlogic.gdx.math.Vector2;

import java.util.Comparator;

/**
 * Created by frittol on 25.1.17.
 */
public class Node implements Comparator<Node>, Comparable<Node> {
    /**
     * True if this node is active
     */
    private boolean active = true;
    private int g = 0; // délka cesty of počátečního uzlu k tomuto
    private int h = 0; // heuristický odhad
    private Vector2 position; // Pozice uzlu
    private Node parent; // Rodič uzlu

    public Node(Vector2 position, Node parent) {
        this.position = new Vector2(position);
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node2 = (Node) o;

        return position != null ? position.equals(node2.position) : node2.position == null;

    }

    @Override
    public int hashCode() {
        return position != null ? position.hashCode() : 0;
    }

    @Override
    public int compareTo(Node node) {
        return this.getF() < node.getF() ? -1 : 1;
    }

    @Override
    public int compare(Node node1, Node node2) {
        return node1.getF() < node2.getF() ? -1 : 1;
    }

    public int getF() {
        return g + h;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    public boolean isActive() {
        return active;
    }
}
