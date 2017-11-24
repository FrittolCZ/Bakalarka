package com.mygdx.game.GameClasses;

/**
 * Created by fanda on 29.06.2017.
 */

public enum GameMode {
    MARATHON("Marathon"),
    TIMED("Timed");

    private final String name;

    GameMode(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
