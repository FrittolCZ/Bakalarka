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
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Scores.HighScore;
import com.mygdx.game.Scores.HighScoreManager;

/**
 * Created by fanda on 23.06.2017.
 */

public class ScoreScreen implements Screen {

    private SpriteBatch batch;
    private Stage stage;
    private Skin skin;

    private Label highScoreLbl, nameLbl, scoreLbl;

    private TextButton marathon, timed, diffOne, diffTwo, diffThree, backToMenu;

    private ButtonGroup mods, difficulty;

    private Table layout, modsButtons, difficultyButtons, scoreTable;

    private BitmapFont font;
    private Label.LabelStyle labelS;
    private FreeTypeFontGenerator generator;

    private InputProcessor inputProcessor;

    private HighScoreManager scoreManager;

    private NinePatchDrawable ninePatchDrawable;

    private String diffLvl = "1";
    private String mod = "Marathon";

    private float width, height;

    public ScoreScreen(Skin skin, SpriteBatch batch, HighScoreManager scoreManager) {
        this.skin = skin;
        this.batch = batch;
        stage = new Stage();

        this.scoreManager = scoreManager;

        highScoreLbl = new Label("High scores", skin);

        nameLbl = new Label("Player name", skin);

        scoreLbl = new Label("Score", skin);


        layout = new Table();
        modsButtons = new Table();
        difficultyButtons = new Table();
        scoreTable = new Table();

        //scoreTable.debug();

        marathon = new TextButton("Marathon", skin, "toggle");
        marathon.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mod = "Marathon";
                setScoreTable(mod, diffLvl);
            }
        });

        timed = new TextButton("Timed", skin, "toggle");
        timed.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mod = "Timed";
                setScoreTable(mod, diffLvl);
            }
        });

        diffOne = new TextButton("Level 1", skin, "toggle");
        diffOne.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                diffLvl = "1";
                setScoreTable(mod, diffLvl);
            }
        });
        diffTwo = new TextButton("Level 2", skin, "toggle");
        diffTwo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                diffLvl = "2";
                setScoreTable(mod, diffLvl);
            }
        });
        diffThree = new TextButton("Level 3", skin, "toggle");
        diffThree.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                diffLvl = "3";
                setScoreTable(mod, diffLvl);
            }
        });

        mods = new ButtonGroup(marathon, timed);
        mods.setUncheckLast(true);
        mods.setMaxCheckCount(1);
        marathon.setChecked(true);

        difficulty = new ButtonGroup(diffOne, diffTwo, diffThree);
        difficulty.setMaxCheckCount(1);
        difficulty.setUncheckLast(true);
        diffOne.setChecked(true);

        width = Gdx.graphics.getWidth() / 10;
        height = Gdx.graphics.getHeight() / 10;

        modsButtons.row().height(height).width(width * 3).pad(20);
        modsButtons.add(marathon, timed);

        difficultyButtons.row().height(height).width(width * 2).pad(20);
        difficultyButtons.add(diffOne, diffTwo, diffThree);

        layout.setFillParent(true);
        //layout.debug();
        layout.add(highScoreLbl).top().height(height).pad(10);
        layout.row().height(height).fill().uniform().pad(10);
        layout.add(modsButtons);
        layout.row().height(height).fill().uniform().pad(10);
        layout.add(difficultyButtons);
        layout.row().expandY().pad(10);
        layout.add(scoreTable).fillX().top();

        stage.addActor(layout);

        inputProcessor = new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.BACK) {
                    ((Game) Gdx.app.getApplicationListener()).setScreen(MyGdxGame.screens.get(MyGdxGame.MENU_SCREEN));
                }
                return false;
            }
        };
    }

    private void setScoreTable(String mod, String diffLvl) {
        scoreTable.clearChildren();
        Table scoreLine;
        Label name;
        Label score;

        scoreLine = new Table();
        //scoreLine.debug();
        scoreLine.pad(20).padBottom(40);;
        scoreLine.row().uniform().expandX();
        scoreLine.add(nameLbl).left();
        scoreLine.add(scoreLbl).right();
        scoreTable.add(scoreLine).expandX().fillX().top();

        labelS = new Label.LabelStyle(MyGdxGame.createFont(generator, 20), Color.WHITE);

        for (HighScore highScore : scoreManager.getScores(mod, diffLvl)) {

            scoreLine = new Table();
            name = new Label(highScore.getPlayerName(), skin);
            name.setStyle(labelS);
            score = new Label(highScore.getScore(),skin);
            score.setStyle(labelS);

            scoreLine.pad(20);
            scoreLine.row().uniform().expandX();
            scoreLine.add(name).left();
            scoreLine.add(score).right();
            scoreTable.row();
            scoreTable.add(scoreLine).expandX().fillX().top();
        }
    }

    @Override
    public void show() {
        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/28 Days Later.ttf"));
        this.font = MyGdxGame.createFont(generator, 24);
        labelS = new Label.LabelStyle(font, Color.WHITE);

        marathon.getLabel().setStyle(labelS);
        timed.getLabel().setStyle(labelS);
        diffOne.getLabel().setStyle(labelS);
        diffTwo.getLabel().setStyle(labelS);
        diffThree.getLabel().setStyle(labelS);

        highScoreLbl.setStyle(new Label.LabelStyle(MyGdxGame.createFont(generator, 36), Color.WHITE));
        nameLbl.setStyle(labelS);
        scoreLbl.setStyle(labelS);

        setScoreTable(mod, diffLvl);

        ninePatchDrawable = new NinePatchDrawable(MyGdxGame.getNinePatch("res/drawable-hdpi/btn_default_normal_green_9.9.png"));

        layout.setBackground(ninePatchDrawable);

        InputMultiplexer multiplexer = new InputMultiplexer(stage, inputProcessor);
        Gdx.input.setInputProcessor(multiplexer);
        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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
