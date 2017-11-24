package com.mygdx.game.GameClasses.GUI;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.mygdx.game.GameClasses.GameManager;
import com.mygdx.game.GameClasses.GameMode;
import com.mygdx.game.GameClasses.Tower;
import com.mygdx.game.MyGdxGame;


public class UIStage extends Stage {

    float height, width;
    Label moneyLabelValue, livesLabelValue, timeLabelValue, timeToWaveLabelValue;
    private GameManager gameManager;
    private Skin skin;
    private Table hud;
    private Container wrapper;
    private Table hgroup;
    private TowerCreateWindow towerCreate;
    private TowerEditWindow towerEdit;
    private BitmapFont font;
    private Label.LabelStyle labelS;
    private FreeTypeFontGenerator generator;
    private NinePatchDrawable backgroundImg;

    private TextButton start;

    public UIStage(Skin skin, final GameManager gameManager) {
        this.width = Gdx.graphics.getWidth();
        this.height = Gdx.graphics.getHeight();
        this.skin = skin;
        this.gameManager = gameManager;
        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/28 Days Later.ttf"));
        this.font = MyGdxGame.createFont(generator, 16);
        labelS = new Label.LabelStyle(font, Color.WHITE);
        Label moneyLabel = new Label("Money: ", labelS);
        moneyLabelValue = new Label(String.valueOf(gameManager.getMoney()), labelS);
        Label livesLabel = new Label("Lives: ", labelS);
        livesLabelValue = new Label(String.valueOf(gameManager.getLives()), labelS);

        Label timeLabel = new Label("", labelS);
        timeLabelValue = new Label("", labelS);
        if (gameManager.getGameMode() == GameMode.MARATHON) {
            timeLabel.setText("Score ");
            timeLabelValue.setText(String.valueOf(gameManager.getWaveManager().getTimeToNextWave()));
        } else if (gameManager.getGameMode() == GameMode.TIMED) {
            timeLabel.setText("Time ");
            timeLabelValue.setText(String.valueOf(gameManager.getTimer()));
        }


        Label timeToWaveLabel = new Label("Next wave in: ", labelS);
        timeToWaveLabelValue = new Label(String.valueOf(0), labelS);

        start = new TextButton("Start", skin);
        start.getLabel().setStyle(labelS);
        start.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameManager.getWaveManager().startSpawn();
                start.setVisible(false);
            }
        });

        TextButton menuButton = new TextButton("Menu", skin);
        menuButton.getLabel().setStyle(labelS);
        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(MyGdxGame.screens.get(MyGdxGame.MENU_SCREEN));
            }
        });
        hud = new Table();
        hgroup = new Table();
        wrapper = new Container();
        backgroundImg = new NinePatchDrawable(MyGdxGame.getNinePatch("res/drawable-hdpi/btn_default_normal_green_9.9.png"));

        wrapper.setFillParent(true);
        wrapper.width(width);
        wrapper.height(height / 10);
        hud.row().height(height / 10);
        hud.setBackground(backgroundImg);
        hud.getBackground().setLeftWidth(10);
        hud.getBackground().setRightWidth(10);

        hud.add(moneyLabel).left().expandX();
        hud.add(moneyLabelValue).right().padRight(30);
        hud.add(livesLabel).left().expandX();
        hud.add(livesLabelValue).right().padRight(30);
        hud.add(timeLabel).left().expandX();
        hud.add(timeLabelValue).right().padRight(30);
        if (gameManager.getGameMode() == GameMode.MARATHON) {
            hud.add(timeToWaveLabel).left().expandX();
            hud.add(timeToWaveLabelValue).right().padRight(30);
        } else {
            hud.add(start).left().width(width / 6).height(height / 10 - 20).padBottom(10);
        }
        hud.add(menuButton).right().width(width / 6).height(height / 10 - 20).padBottom(10);
        wrapper.setActor(hud);

        wrapper.top().left();

        towerCreate = new TowerCreateWindow(skin, gameManager, new Label.LabelStyle(MyGdxGame.createFont(generator, 12), Color.WHITE), width, height);
        towerCreate.setHeight(height / 2);
        towerEdit = new TowerEditWindow(skin, gameManager, new Label.LabelStyle(MyGdxGame.createFont(generator, 12), Color.WHITE), width, height);
        towerEdit.setHeight(height / 2);
        this.addActor(wrapper);
        this.addActor(towerCreate);
        this.addActor(towerEdit);
    }

    public void onClick(Vector2 position) {
        Tower t = gameManager.getTowerManager().towerAtThisPosition(position);
        if (t != null) {
            showTowerEditWindow(position, t);
        } else if (gameManager.getGameMap().isThisNodeEmpty(position) &&
                gameManager.getCheckPathfinder().checkPathExist(position, gameManager.getWaveManager().getWaves())) {
            if (gameManager.getGameMode() != GameMode.TIMED || !gameManager.getWaveManager().isSpawning())
                showTowerCreateWindow(position);
        }
    }

    public void showTowerCreateWindow(Vector2 position) {
        towerCreate.setCreatePos(position);
        towerCreate.setVisible(true);
    }

    public void showTowerEditWindow(Vector2 position, Tower t) {
        towerEdit.setTower(t);
        towerEdit.setVisible(true);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (gameManager.getLives() < 30) {
            livesLabelValue.setText(String.valueOf(gameManager.getLives()));
        }
        moneyLabelValue.setText(String.valueOf(gameManager.getMoney()));
        if (gameManager.getGameMode() == GameMode.MARATHON) {
            timeLabelValue.setText(String.valueOf(gameManager.getScore()));
        } else if (gameManager.getGameMode() == GameMode.TIMED) {
            timeLabelValue.setText(String.valueOf((int) gameManager.getTimer()));
        }
        timeToWaveLabelValue.setText(String.valueOf((int) gameManager.getWaveManager().getTimeToNextWave()));
    }

    @Override
    public void dispose() {
        super.dispose();
        generator.dispose();
        font.dispose();
    }
}
