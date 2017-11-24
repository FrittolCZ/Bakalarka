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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.mygdx.game.MyGdxGame;

public class MainMenuScreen implements Screen {

    private SpriteBatch batch;
    private Stage stage;
    private InputProcessor inputProcessor;

    private FreeTypeFontGenerator generator;
    private NinePatchDrawable ninePatchDrawable;

    private Table menuLayout;

    private Label.LabelStyle labelS;

    private BitmapFont font;

    private TextButton newGameBtn, settingsBtn, highScoreBtn, exitBtn, resumeBtn;


    public MainMenuScreen(Skin skin, SpriteBatch batch) {
        this.batch = batch;
        stage = new Stage();


        menuLayout = new Table();

        newGameBtn = new TextButton("New Game", skin);
        newGameBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(MyGdxGame.screens.get(MyGdxGame.MAP_SELECT_SCREEN));
            }
        });

        settingsBtn = new TextButton("Settings", skin);
        settingsBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(MyGdxGame.screens.get(MyGdxGame.SETTINGS_SCREEN));
            }
        });

        highScoreBtn = new TextButton("High scores", skin);
        highScoreBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(MyGdxGame.screens.get(MyGdxGame.HIGH_SCORE_SCREEN));
            }
        });

        exitBtn = new TextButton("Exit", skin);
        exitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        resumeBtn = new TextButton("Resume", skin);
        resumeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(MyGdxGame.screens.get(MyGdxGame.GAME_SCREEN));
            }
        });


        //menuLayout.debug();
        menuLayout.setFillParent(true);
        stage.addActor(menuLayout);


        inputProcessor = new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {

                if (keycode == Input.Keys.BACK) {
                    Gdx.app.exit();
                }
                return false;
            }
        };
    }

    @Override
    public void show() {

        ninePatchDrawable = new NinePatchDrawable(MyGdxGame.getNinePatch("res/drawable-hdpi/btn_default_normal_green_9.9.png"));

        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/28 Days Later.ttf"));
        font = MyGdxGame.createFont(generator, 20);
        labelS = new Label.LabelStyle(font, Color.WHITE);

        newGameBtn.getLabel().setStyle(labelS);
        settingsBtn.getLabel().setStyle(labelS);
        highScoreBtn.getLabel().setStyle(labelS);
        exitBtn.getLabel().setStyle(labelS);
        resumeBtn.getLabel().setStyle(labelS);

        menuLayout.setBackground(ninePatchDrawable);

        GameScreen gameScreen = (GameScreen)MyGdxGame.screens.get(MyGdxGame.GAME_SCREEN);
        float width = Gdx.graphics.getWidth() / 2;
        float height = Gdx.graphics.getHeight() / 3 * 2;

        if(gameScreen.isGameRunning())
        {
            menuLayout.clearChildren();
            menuLayout.row().height(height / 4).width(width).pad(10);
            menuLayout.add(resumeBtn).fill();
            addButtons(width, height);
        }
        else
        {
            menuLayout.clearChildren();
            addButtons(width, height);
        }

        InputMultiplexer multiplexer = new InputMultiplexer(stage, inputProcessor);
        Gdx.input.setInputProcessor(multiplexer);
        Gdx.input.setCatchBackKey(true);
    }

    private void addButtons(float width, float height)
    {
        menuLayout.row().height(height / 4).width(width).pad(10);
        menuLayout.add(newGameBtn).fill();
        menuLayout.row().height(height / 4).width(width).pad(10);
        menuLayout.add(settingsBtn).fill();
        menuLayout.row().height(height / 4).width(width).pad(10);
        menuLayout.add(highScoreBtn).fill();
        menuLayout.row().height(height / 4).width(width).pad(10);
        menuLayout.add(exitBtn).fill();
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
        batch.dispose();
    }
}