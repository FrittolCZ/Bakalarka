package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.mygdx.game.GameClasses.GameMode;
import com.mygdx.game.MyGdxGame;

public class PreGameScreen implements Screen {

    private SpriteBatch batch;
    private Skin skin;

    private Stage stage;

    private Table layout, modsLayout, diffsLayout;

    private Label modsLbl, diffsLbl;

    private TextButton marathonMod, timedMod;

    private ButtonGroup mods;

    private TextButton diffOne, diffTwo, diffThree;

    private ButtonGroup difficulty;

    private TextButton startGame;

    private BitmapFont font;
    private Label.LabelStyle labelS;
    private FreeTypeFontGenerator generator;
    private NinePatchDrawable ninePatchDrawable;

    private GameMode mode = GameMode.MARATHON;

    private int diffLvl = 1;

    private InputProcessor inputProcessor;

    public PreGameScreen(Skin skin, SpriteBatch batch) {
        this.batch = batch;
        this.skin = skin;

        stage = new Stage();

        layout = new Table();

        modsLayout = new Table();

        diffsLayout = new Table();

        modsLbl = new Label("Select mode: ", skin);
        diffsLbl = new Label("Select difficulty: ", skin);

        marathonMod = new TextButton("Marathon", skin, "toggle");
        marathonMod.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mode = GameMode.MARATHON;
            }
        });

        timedMod = new TextButton("Timed", skin, "toggle");
        timedMod.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mode = GameMode.TIMED;
            }
        });

        mods = new ButtonGroup(marathonMod, timedMod);
        mods.setMaxCheckCount(1);
        mods.setUncheckLast(true);

        diffOne = new TextButton("Level 1", skin, "toggle");
        diffOne.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                diffLvl = 1;
            }
        });

        diffTwo = new TextButton("Level 2", skin, "toggle");
        diffTwo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                diffLvl = 2;
            }
        });


        diffThree = new TextButton("Level 3", skin, "toggle");
        diffThree.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                diffLvl = 3;
            }
        });

        difficulty = new ButtonGroup(diffOne, diffTwo, diffThree);
        difficulty.setMaxCheckCount(1);
        difficulty.setUncheckLast(true);

        startGame = new TextButton("Start Game", skin);
        startGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameScreen screen = (GameScreen) MyGdxGame.screens.get(MyGdxGame.GAME_SCREEN);
                screen.initGame(mode,diffLvl);
                ((Game) Gdx.app.getApplicationListener()).setScreen(screen);
            }
        });

        float heigth = Gdx.graphics.getHeight() / 10;
        float width = Gdx.graphics.getWidth() / 5;

        //modsLayout.debug();
        modsLayout.row().height(heigth);
        modsLayout.add(modsLbl).colspan(2);
        modsLayout.row().height(heigth).width(width).fill().pad(20);
        modsLayout.add(marathonMod, timedMod);

        //diffsLayout.debug();
        diffsLayout.row().height(heigth);
        diffsLayout.add(diffsLbl).colspan(3);
        diffsLayout.row().height(heigth).width(width).fill().pad(20);
        diffsLayout.add(diffOne, diffTwo, diffThree);

        layout.setFillParent(true);
        //layout.debug();
        layout.row().pad(20);
        layout.add(modsLayout);
        layout.row().pad(20);
        layout.add(diffsLayout);
        layout.row().pad(20);
        layout.add(startGame).height(Gdx.graphics.getHeight() / 10).pad(40).colspan(3).fill();

        stage.addActor(layout);

        inputProcessor = new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.BACK) {
                    ((Game) Gdx.app.getApplicationListener()).setScreen(MyGdxGame.screens.get(MyGdxGame.MAP_SELECT_SCREEN));
                }
                return false;
            }
        };


    }

    @Override
    public void show() {

        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/28 Days Later.ttf"));
        this.font = MyGdxGame.createFont(generator, 24);
        labelS = new Label.LabelStyle(font, Color.WHITE);

        modsLbl.setStyle(labelS);
        diffsLbl.setStyle(labelS);
        marathonMod.getLabel().setStyle(labelS);
        timedMod.getLabel().setStyle(labelS);
        diffOne.getLabel().setStyle(labelS);
        diffTwo.getLabel().setStyle(labelS);
        diffThree.getLabel().setStyle(labelS);
        startGame.getLabel().setStyle(labelS);


        ninePatchDrawable = new NinePatchDrawable(MyGdxGame.getNinePatch("res/drawable-hdpi/btn_default_normal_green_9.9.png"));

        layout.setBackground(ninePatchDrawable);

        InputMultiplexer multiplexer = new InputMultiplexer(stage, inputProcessor);
        Gdx.input.setInputProcessor(multiplexer);
        Gdx.input.setCatchBackKey(true);
        marathonMod.setChecked(true);
        mode = GameMode.MARATHON;
        diffOne.setChecked(true);
        diffLvl = 1;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();

        batch.begin();

        stage.draw();

        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
