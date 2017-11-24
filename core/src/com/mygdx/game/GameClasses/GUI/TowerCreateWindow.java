package com.mygdx.game.GameClasses.GUI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.mygdx.game.GameClasses.GameManager;
import com.mygdx.game.GameClasses.TowerTypes.TowerType;

/**
 * Created by fanda on 10.04.2017.
 */

public class TowerCreateWindow extends Window {

    private GameManager gameManager;
    private TowerType towerType = TowerType.CANNON;
    private Label dmgLbl, rangeLbl, firerateLbl, descLbl, priceLbl;
    private Label dmgValLbl, rangeValLbl, firerateValLbl, priceValLbl;
    private Image towerPreview;
    private Button left, right;
    private TextButton close, buy;
    private Table buttons;
    private Vector2 createPos;

    public TowerCreateWindow(Skin skin, GameManager gameManager, Label.LabelStyle labelS, float width, float height) {
        super("", skin);
        this.setSize(width / 3, height / 2);
        this.gameManager = gameManager;

        this.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Nedělá nic, zabrání kliknutí zkrze okno
            }
        });

        dmgLbl = new Label("Damage: ", labelS);
        dmgValLbl = new Label(String.valueOf(towerType.getDamage()), labelS);
        rangeLbl = new Label("Range: ", labelS);
        rangeValLbl = new Label(String.valueOf(towerType.getRange()), labelS);
        firerateLbl = new Label("Fire rate: ", labelS);
        firerateValLbl = new Label(String.valueOf(towerType.getFirerate()), labelS);
        priceLbl = new Label("Price: ", labelS);
        priceValLbl = new Label(String.valueOf(towerType.getPrice()), labelS);

        descLbl = new Label(towerType.getDescription(), labelS);
        descLbl.setWrap(true);
        descLbl.pack();
        towerPreview = new Image(towerType.getTexture());
        left = new Button(skin, "left");
        right = new Button(skin, "right");
        left.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                towerType = towerType.getPrev();
                changeTower();
            }
        });
        right.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                towerType = towerType.getNext();
                changeTower();
            }
        });
        buttons = new Table();
        buy = new TextButton("Buy", skin);
        buy.getLabel().setStyle(labelS);
        buy.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buySelectedTower();
            }
        });

        close = new TextButton("Close", skin);
        close.getLabel().setStyle(labelS);
        close.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setVisible(false);
            }
        });

        float padSize = this.getWidth() / 10;

        buttons.row().width(this.getWidth() / 2 - padSize).height(height / 10);
        buttons.add(buy).left();
        buttons.add(close).right();
        //buttons.debug();


        //this.debug();
        this.center();
        this.pad(padSize, padSize, 0, padSize);
        this.row().size(height / 10).center().spaceBottom(padSize / 5);
        this.add(left).expandX().left();
        this.add(towerPreview);
        this.add(right).expandX().right();
        this.row().spaceBottom(padSize / 5);
        this.add(dmgLbl).left().colspan(2);
        this.add(dmgValLbl).right();
        this.row().spaceBottom(padSize / 5);
        this.add(priceLbl).left().colspan(2);
        this.add(priceValLbl).right();
        this.row().spaceBottom(padSize / 5);
        this.add(rangeLbl).left().colspan(2);
        this.add(rangeValLbl).right();
        this.row().spaceBottom(padSize / 5);
        this.add(firerateLbl).left().colspan(2);
        this.add(firerateValLbl).right();
        this.row().spaceBottom(padSize / 5);
        this.add(descLbl).width(this.getWidth() - padSize * 2).left().colspan(3);
        this.row().spaceBottom(padSize / 5);
        this.add(buttons).colspan(3).width(this.getWidth() - padSize * 2);
        this.setVisible(false);
    }

    public void setCreatePos(Vector2 pos) {
        this.createPos = pos;
    }

    private void buySelectedTower() {
        gameManager.getTowerManager().createTower(createPos, towerType);
        gameManager.takeMoney(towerType.getPrice());
        gameManager.getGameMap().getNodeOnPosition(createPos).deactivate();
        gameManager.getWaveManager().recalcPath();
        this.setVisible(false);
    }


    private void changeTower() {
        towerPreview.setDrawable(new SpriteDrawable(new Sprite(towerType.getTexture())));
        dmgValLbl.setText(String.valueOf(towerType.getDamage()));
        rangeValLbl.setText(String.valueOf(towerType.getRange()));
        firerateValLbl.setText(String.valueOf(towerType.getFirerate()));
        descLbl.setText(towerType.getDescription());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (towerType.getPrice() > gameManager.getMoney()) {
            this.buy.setVisible(false);
            this.priceValLbl.setColor(Color.RED);
            this.priceLbl.setColor(Color.RED);
        } else if (!buy.isVisible()) {
            buy.setVisible(true);
            this.priceValLbl.setColor(Color.WHITE);
            this.priceLbl.setColor(Color.WHITE);
        }
    }
}
