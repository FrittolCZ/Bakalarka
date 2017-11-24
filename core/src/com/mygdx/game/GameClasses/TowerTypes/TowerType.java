package com.mygdx.game.GameClasses.TowerTypes;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.screens.GameScreen;

public enum TowerType {
    CANNON("Fast, medium range tower", "cannonTower", 15, 15, 200, 0.75f),
    DOUBLE_CANNON("Fast, short range tower", "cannonTower-double", 15, 15, 100, 0.5f);
    //SMALL_ROCKET("CannonTower", "Slow, long range", "rocketLauncher-Small-Loaded", 25, 20, 350, 0.3f),
    //BIG_ROCKET("CannonTower", "Slow, long range", "rocketLauncher-Big-Loaded", 35, 30, 350, 0.2f);

    private TextureRegion textureRegion;
    private String description;
    private int damage;
    private int price;
    private float range;
    private float firerate;
    private String className;

    TowerType(String desc, String textName, int dmg, int price, float range, float firerate) {
        this.description = desc;
        this.damage = dmg;
        this.price = price;
        this.range = range;
        this.firerate = firerate;
        this.textureRegion = GameScreen.myTextures.findRegion(textName);
    }

    public TowerType getNext() {
        return values()[(this.ordinal() + 1) % values().length];
    }

    public TowerType getPrev() {
        if (this.ordinal() == 0) return values()[values().length - 1];
        return values()[(this.ordinal() - 1) % values().length];
    }

    public String getDescription() {
        return description;
    }

    public int getDamage() {
        return damage;
    }

    public int getPrice() {
        return price;
    }

    public float getRange() {
        return range;
    }

    public float getFirerate() {
        return firerate;
    }

    public TextureRegion getTexture() {
        return textureRegion;
    }
}
