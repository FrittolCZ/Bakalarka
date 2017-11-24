package com.mygdx.game.GameClasses.TowerTypes;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameClasses.Projectile;
import com.mygdx.game.GameClasses.Tower;
import com.mygdx.game.screens.GameScreen;

public class DoubleCannonTower extends Tower {

    private boolean barrelIndex = true;

    public DoubleCannonTower(Vector2 pos, TowerType type) {
        super(GameScreen.myTextures.findRegion("projectile1"), pos , type);
    }

    public void update(float delta) {
        super.update(delta);
        /*
        If does have target and is time to fire, creates new projectile at position of this
        tower and resets the timer
        */
        Projectile p;
        if (projectileTimer >= firerate && target != null) {
            if(barrelIndex)
            {
                p = new Projectile(projectileTexture, super.getPosition().add(-5,0), super.getRotation(), 6, damage);
                barrelIndex = false;
            }
            else
            {
                p = new Projectile(projectileTexture, super.getPosition().add(5,0), super.getRotation(), 6, damage);
                barrelIndex = true;
            }
            projectiles.add(p);
            projectileTimer = 0;

        }
         /*
         Then checks all projectiles if any of them missed or hit the target, if so,
         kills that projectile
         */
        for (int i = 0; i < projectiles.size(); i++) {
            p = projectiles.get(i);
            p.setRotation(super.getRotation());
            if (!isInRange(p.getCenter())) {
                p.kill();
            }
            if (target != null && p.getCenter().dst(target.getCenter()) < 12) {
                target.takeHealth(this.getDamage());
                p.kill();
            }
            if (p.isDead()) {
                projectiles.remove(p);
                i--;
            }
            p.update();
        }
    }
}
