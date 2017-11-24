package com.mygdx.game.GameClasses;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Projectile extends MySprite {
    private int age;
    private int speed;

    public Projectile(TextureRegion texture, Vector2 pos, float rotation, int speed, int damage) {
        super(texture, pos);
        super.setOriginCenter();
        super.setRotation(rotation);
        this.speed = speed;
    }

    public boolean isDead() {
        return age > 100;
    }

    public void kill() {
        this.age = 200;
    }

    public void update() {
        age++;
        Vector2 direction = new Vector2();
        direction.x = (float) Math.cos(Math.toRadians(super.getRotation() + 90));
        direction.y = (float) Math.sin(Math.toRadians(super.getRotation() + 90));
        super.translate(direction.x * speed, direction.y * speed);
    }
}
