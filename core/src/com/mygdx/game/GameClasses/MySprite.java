package com.mygdx.game.GameClasses;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * MySprite extends Sprite and add option to create sprite with texture, on specific position.
 * And also add methods working with Vector class.
 */
public class MySprite extends Sprite {

    /**
     * Creates MySprite on specific position
     */
    public MySprite(TextureRegion texture, Vector2 position) {
        super(texture);
        super.setPosition(position.x, position.y);
    }

    /**
     * @return Position as a 2D vector
     */
    public Vector2 getPosition() {
        return new Vector2(super.getX(), super.getY());
    }

    /**
     * Sets position of MySprite to position in vector
     *
     * @param position 2D vector
     */
    public void setPosition(Vector2 position) {
        super.setPosition(position.x, position.y);
    }


    /**
     * Calculates the center position of MySprite
     *
     * @return Position where lies centre of MySprite
     */
    public Vector2 getCenter() {
        return new Vector2(super.getX() + super.getWidth() / 2, super.getY() + super.getHeight() / 2);
    }
}
