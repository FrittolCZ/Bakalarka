package com.mygdx.game.GameClasses;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Queue;

/**
 * Holds information about enemy and manages his move through game map
 */
public class Enemy extends MySprite {

    /**
     * Maximum health of the enemy
     */
    private float maxHealth;
    /**
     * Actual life of the enemy
     */
    private float health;
    /**
     * Move speed of the enemy
     */
    private float speed;
    /**
     * Amout of money that the enemy carry
     */
    private int bounty;
    /**
     * Queue of vectors representing path
     */
    private Queue<Vector2> path = new Queue<Vector2>();

    /**
     * Texture visualising amount of health remaining
     */
    private Texture healthBar;


    /**
     * Creates Enemy as MySprite
     *
     * @param position Starting position of enemy
     * @param health   Starting health of enemy
     * @param bounty   Amount of money player will gets for killing this enemy
     * @param speed    Movement speed of this enemy
     */
    public Enemy(TextureRegion texture, Vector2 position, float health, int bounty, float speed) {
        super(texture, position);
        this.maxHealth = health;
        this.health = maxHealth;
        this.bounty = bounty;
        this.speed = speed;
        this.healthBar = new Texture(getTexturePix());
    }

    /**
     * @return Queue of vectors representing path
     */
    public Queue<Vector2> getCurrentPath() {
        return this.path;
    }

    /**
     * @return Returns vector representing point to which heads
     */
    public Vector2 getNext() {
        return path.first();
    }

    /**
     * Causes damage to the enemy
     *
     * @param damage Amount of damage to cause to this enemy
     */
    public void takeHealth(float damage) {
        healthBar = new Texture(getTexturePix());
        health -= damage;
    }

    /**
     * @return True if enemy have still some health
     */
    public boolean isAlive() {
        return health > 0;
    }

    /**
     * @return Money that had this enemy
     */
    public int getBounty() {
        return bounty;
    }

    /**
     * Sets starting path to enemy and places enemy on the starting point
     *
     * @param points New path
     */
    public void setPath(Queue<Vector2> points, boolean setOnFirst) {
        path.clear();
        for (Vector2 pathPoint : points) {
            path.addLast(new Vector2(pathPoint));
        }
        if (setOnFirst) {
            super.setPosition(path.first());
        }
    }

    /**
     * @return Distance to next point in path
     */
    private float distanceToDestination() {
        return super.getPosition().dst(path.first());
    }

    /**
     * Updates status of enemy. First checks if enemy is still alive.
     */
    public void update() {
        if (isAlive()) {
            super.setOriginCenter();
            if (path.size > 0) {
                Vector2 direction = super.getPosition().sub(path.first());
                direction.nor();
                if (distanceToDestination() < speed) {
                    super.setPosition(path.first());
                    super.setOriginCenter();
                    path.removeFirst();
                    /*direction = super.getPosition().sub(path.first());
                    direction.nor();
                    float angle = (float) (Math.atan2(direction.x, direction.y) / Math.PI) + 90;
                    super.setRotation(angle);*/
                } else {
                    super.translate(speed * (-direction.x), speed * (-direction.y));
                }
            }
        }
    }


    /**
     * Generates health bar. Size of bar is calculated from ratio of current and maximum health.
     *
     * @return Pixmap representing health bar of enemy.
     */
    private Pixmap getTexturePix() {
        float healthPercentage = health / maxHealth;
        float red = (healthPercentage < 0.5 ? 1 : 1 - (2 * healthPercentage - 1));
        float green = (healthPercentage > 0.5 ? 1 : (2 * healthPercentage));
        float width = this.getWidth() * healthPercentage;
        Pixmap healthBar = new Pixmap((int) width, (int) this.getHeight() / 10, Pixmap.Format.RGBA8888);
        healthBar.setColor(new Color(red, green, 0, 1));
        healthBar.fill();
        return healthBar;
    }

    /**
     * Draws enemy and texture representing his actual life, if he is still alive
     */
    @Override
    public void draw(Batch batch) {
        if (isAlive()) {
            batch.draw(healthBar, this.getX(), this.getY() + this.getHeight());
            super.draw(batch);
        }
    }
}
