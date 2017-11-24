package com.mygdx.game.GameClasses;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameClasses.TowerTypes.TowerType;
import com.mygdx.game.GameClasses.WaveManager.Wave;

import java.util.ArrayList;

public abstract class Tower extends MySprite {
    /**
     * The purchase price of the tower
     */
    protected int price;

    /**
     * Amount of damage that tower deals on hit
     */
    protected int damage;

    /**
     * Level of damage upgrade
     */
    protected int damageUpLvl = 1;

    /**
     * Maximum distance to whitch the tower can shoot
     */
    protected float range;

    /**
     * Level of range upgrade
     */
    protected int rangeUpLvl = 1;

    /**
     * The amount of time between shots
     */
    protected float firerate;

    /**
     * Level of fire rate upgrade
     */
    protected int rateUpLvl = 1;

    /**
     * Texture of projectile shoot by this tower, different types of tower can have different projectiles
     */
    protected TextureRegion projectileTexture;

    /**
     * Enemy on which this tower is focused
     */
    protected Enemy target;

    /**
     * Fire timer, if timer is bigger than rate, then this tower fires at enemy
     */
    protected float projectileTimer;

    /**
     * List of projectile that was shoot by this tower and are still alive
     */
    protected ArrayList<Projectile> projectiles = new ArrayList<Projectile>();

    /**
     * Type of tower
     */
    protected TowerType type;

    //Constructor

    /**
     * Creates tower with specific texture, projectile texture and type on position
     *
     * @param projectileTexture
     * @param pos
     */
    public Tower(TextureRegion projectileTexture, Vector2 pos, TowerType type) {
        super(type.getTexture(), pos);
        this.type = type;
        this.projectileTexture = projectileTexture;
        this.damage = this.type.getDamage();
        this.price = this.type.getPrice();
        this.range = this.type.getRange();
        this.firerate = this.type.getFirerate();
    }

    /**
     * @return Current tower price, consisting of purchase price of tower and price of all upgrades
     */
    public int getPrice() {
        return price;
    }


    /**
     * Sets the price of this tower to new value
     *
     * @param price New price of tower
     */
    public void setPrice(int price) {
        this.price = price;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public float getRange() {
        return range;
    }

    public void setRange(float range) {
        this.range = range;
    }

    public float getFirerate() {
        return firerate;
    }

    public void setFirerate(float firerate) {
        this.firerate = firerate;
    }

    public int getDamageUpLvl() {
        return damageUpLvl;
    }

    public void addDamageUpLvl() {
        this.damageUpLvl++;
    }

    public int getRangeUpLvl() {
        return rangeUpLvl;
    }

    public void addRangeUpLvl() {
        this.rangeUpLvl++;
    }

    public int getRateUpLvl() {
        return rateUpLvl;
    }

    public TowerType getType() {
        return type;
    }

    public void addRateUpLvl() {
        this.rateUpLvl++;
    }

    public Enemy getTarget() {
        return target;
    }

    public void setTarget(Enemy e) {
        this.target = e;
    }

    /**
     * @param position
     * @return true if position is in tower's range
     */
    public boolean isInRange(Vector2 position) {
        return (super.getCenter().dst(position) <= range);
    }


    /**
     * From all enemies in game selects closest one and set him as new target
     *
     * @param waves All waves of enemies that are currently in game
     */
    public void getClosestEnemy(ArrayList<Wave> waves) {
        target = null;
        float smallestRange = range;

        for (Wave w : waves) {
            for (Enemy e : w.getEnemies()) {
                if (super.getCenter().dst(e.getCenter()) < smallestRange) {
                    smallestRange = super.getCenter().dst(e.getCenter());
                    target = e;
                }
            }
        }
    }

    /**
     * Rotates tower to face targeted enemy
     */
    protected void faceTarget() {
        Vector2 direction = super.getCenter().sub(target.getCenter());
        direction.nor();
        super.setOriginCenter();
        super.setRotation((float) (Math.atan2(direction.x, -direction.y) * 180 / Math.PI));
    }

    /**
     * Updates time to fire, changes rotation to target if have one and checks if actual target is still in range
     *
     * @param delta
     */
    public void update(float delta) {
        projectileTimer += delta;
        if (target != null) {
            faceTarget();

            if (!isInRange(target.getCenter()) || !target.isAlive()) {
                target = null;
                projectileTimer = 0;
            }
        }
    }

    @Override
    public void draw(Batch batch) {
        for (Projectile p : projectiles) {
            p.draw(batch);
        }
        super.draw(batch);
    }
}

