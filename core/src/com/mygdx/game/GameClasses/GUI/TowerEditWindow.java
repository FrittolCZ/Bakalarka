package com.mygdx.game.GameClasses.GUI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.GameClasses.GameManager;
import com.mygdx.game.GameClasses.Tower;

/**
 * Created by fanda on 19.06.2017.
 */

public class TowerEditWindow extends Window {

    private GameManager gameManager;
    private Tower tower;
    private Label dmgLbl, rangeLbl, rateLbl;
    private Label dmgValLbl, rangeValLbl, rateValLbl;
    private TextButton dmgUp, rangeUp, rateUp, sell, cancel;
    private Table buttonsWrap;

    public TowerEditWindow(Skin skin, GameManager gameManager, Label.LabelStyle labelS, float width, float height) {
        super("", skin);
        this.setSize(width / 3, height / 2);
        this.gameManager = gameManager;

        this.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Nedělá nic, zabrání kliknutí zkrze editační okno
            }
        });

        this.buttonsWrap = new Table();

        this.dmgLbl = new Label("Damage:", labelS);
        this.rangeLbl = new Label("Range:", labelS);
        this.rateLbl = new Label("Fire rate:", labelS);
        this.dmgValLbl = new Label("0", labelS);
        this.rangeValLbl = new Label("0", labelS);
        this.rateValLbl = new Label("0", labelS);

        this.dmgUp = new TextButton("+", skin);
        this.dmgUp.getLabel().setStyle(labelS);
        this.dmgUp.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                upgradeDmg();
            }
        });

        this.rangeUp = new TextButton("+", skin);
        this.rangeUp.getLabel().setStyle(labelS);
        this.rangeUp.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                upgradeRange();
            }
        });

        this.rateUp = new TextButton("+", skin);
        this.rateUp.getLabel().setStyle(labelS);
        this.rateUp.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                upgradeRate();
            }
        });

        this.sell = new TextButton("Sell", skin);
        this.sell.getLabel().setStyle(labelS);
        this.sell.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sellTower();
            }
        });

        this.cancel = new TextButton("Cancel", skin);
        this.cancel.getLabel().setStyle(labelS);
        this.cancel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setVisible(false);
            }
        });

        float padSize = this.getWidth() / 10;

        this.buttonsWrap.row().width(this.getWidth() / 2 - padSize).height(height / 10);
        this.buttonsWrap.add(sell).left();
        this.buttonsWrap.add(cancel).right();

        //this.debug();
        this.center();
        this.pad(padSize, padSize, 0, padSize);
        this.row().spaceBottom(padSize / 5);
        this.add(dmgLbl).left().expandX();
        this.add(dmgValLbl).right();
        this.add(dmgUp);
        this.row().spaceBottom(padSize / 5);
        this.add(rangeLbl).left();
        this.add(rangeValLbl).right();
        this.add(rangeUp);
        this.row().spaceBottom(padSize / 5);
        this.add(rateLbl).left();
        this.add(rateValLbl).right();
        this.add(rateUp);

        this.row().spaceBottom(padSize / 5);
        this.add(buttonsWrap).colspan(3).width(this.getWidth() - padSize * 2);

        this.setVisible(false);
    }

    private void upgradeDmg() {
        tower.setDamage(tower.getDamage() + (int) Math.round(tower.getDamage() * 0.15));
        tower.addDamageUpLvl();
        gameManager.takeMoney(15);
        dmgValLbl.setText(String.valueOf(tower.getDamage()));
        if (tower.getDamageUpLvl() == 5) {
            dmgUp.setTouchable(Touchable.disabled);
            dmgUp.getLabel().setText("Max");
            dmgUp.getLabel().setColor(Color.RED);
        }

    }

    private void upgradeRange() {
        tower.setRange(tower.getRange() + (tower.getRange() * 0.15f));
        tower.addRangeUpLvl();
        gameManager.takeMoney(15);
        rangeValLbl.setText(String.valueOf(tower.getRange()));
        if (tower.getRangeUpLvl() == 5) {
            rangeUp.setTouchable(Touchable.disabled);
            rangeUp.getLabel().setText("Max");
            rangeUp.getLabel().setColor(Color.RED);
        }
    }

    private void upgradeRate() {
        tower.setFirerate(tower.getFirerate() - (tower.getFirerate() * 0.15f));
        tower.addRateUpLvl();
        gameManager.takeMoney(15);
        rateValLbl.setText(String.valueOf(tower.getFirerate()));
        if (tower.getRateUpLvl() == 5) {
            rateUp.setTouchable(Touchable.disabled);
            rateUp.getLabel().setText("Max");
            rateUp.getLabel().setColor(Color.RED);
        }
    }

    private void sellTower() {
        gameManager.addMoney(Math.round(tower.getPrice() * 0.7f));
        gameManager.removeTower(tower);
        gameManager.getWaveManager().recalcPath();
        this.setVisible(false);
    }

    public void setTower(Tower tower) {
        this.tower = tower;
        this.rateValLbl.setText(String.valueOf(tower.getFirerate()));
        this.dmgValLbl.setText(String.valueOf(tower.getDamage()));
        this.rangeValLbl.setText(String.valueOf(tower.getRange()));

        if (tower.getDamageUpLvl() == 5 || (tower.getDamageUpLvl() * 15 > gameManager.getMoney())) {
            dmgUp.setTouchable(Touchable.disabled);
            dmgUp.getLabel().setColor(Color.RED);
            if (tower.getDamageUpLvl() == 5) {
                dmgUp.getLabel().setText("Max");
            } else {
                dmgUp.getLabel().setText(String.valueOf(tower.getDamageUpLvl() * 15));
            }
        } else {
            dmgUp.setTouchable(Touchable.enabled);
            dmgUp.getLabel().setText(String.valueOf(tower.getDamageUpLvl() * 15));
            dmgUp.getLabel().setColor(Color.WHITE);
        }

        if (tower.getRangeUpLvl() == 5 || (tower.getRangeUpLvl() * 15 > gameManager.getMoney())) {
            rangeUp.setTouchable(Touchable.disabled);
            rangeUp.getLabel().setColor(Color.RED);
            if (tower.getRangeUpLvl() == 5) {
                rangeUp.getLabel().setText("Max");
            } else {
                rangeUp.getLabel().setText(String.valueOf(tower.getRangeUpLvl() * 15));
            }
        } else {
            rangeUp.setTouchable(Touchable.enabled);
            rangeUp.getLabel().setText(String.valueOf(tower.getRangeUpLvl() * 15));
            rangeUp.getLabel().setColor(Color.WHITE);
        }

        if (tower.getRateUpLvl() == 5 || (tower.getRateUpLvl() * 15 > gameManager.getMoney())) {
            rateUp.setTouchable(Touchable.disabled);
            rateUp.getLabel().setColor(Color.RED);
            if (tower.getRateUpLvl() == 5) {
                rateUp.getLabel().setText("Max");
            } else {
                rateUp.getLabel().setText(String.valueOf(tower.getRateUpLvl() * 15));
            }
        } else {
            rateUp.setTouchable(Touchable.enabled);
            rateUp.getLabel().setText(String.valueOf(tower.getRateUpLvl() * 15));
            rateUp.getLabel().setColor(Color.WHITE);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (tower.getDamageUpLvl() * 15 > gameManager.getMoney()) {
            dmgUp.getLabel().setColor(Color.RED);
            dmgUp.setTouchable(Touchable.disabled);
        } else {
            dmgUp.getLabel().setColor(Color.WHITE);
            dmgUp.setTouchable(Touchable.enabled);
        }

        if (tower.getRangeUpLvl() * 15 > gameManager.getMoney()) {
            rangeUp.getLabel().setColor(Color.RED);
            rangeUp.setTouchable(Touchable.disabled);
        } else {
            rangeUp.getLabel().setColor(Color.WHITE);
            rangeUp.setTouchable(Touchable.enabled);
        }

        if (tower.getRateUpLvl() * 15 > gameManager.getMoney()) {
            rateUp.getLabel().setColor(Color.RED);
            rateUp.setTouchable(Touchable.disabled);
        } else {
            rateUp.getLabel().setColor(Color.WHITE);
            rateUp.setTouchable(Touchable.enabled);
        }
    }
}
