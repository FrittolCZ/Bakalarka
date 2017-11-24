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

import java.util.ArrayList;


public class SettingsScreen implements Screen {

    private SpriteBatch batch;
    private Stage stage;

    private BitmapFont font;
    private Label.LabelStyle labelS;
    private FreeTypeFontGenerator generator;

    private Label settingsLbl;

    private Table layout;

    private ButtonGroup algorithmsGroup;

    private String[] algorithms = {"DFS", "Dijkstra", "A Star", "Greedy Best-first"};

    private ArrayList<TextButton> algorithmsButtons = new ArrayList<TextButton>();

    //private SelectBox<String> pathfinderSelect;

    private TextButton backBtn;

    private InputProcessor inputProcessor;

    private NinePatchDrawable ninePatchDrawable;

    public SettingsScreen(Skin skin, SpriteBatch batch) {
        this.batch = batch;
        stage = new Stage();


        settingsLbl = new Label("Settings", skin);

        layout = new Table();

        algorithmsGroup = new ButtonGroup();
        algorithmsGroup.setMaxCheckCount(1);
        algorithmsGroup.setUncheckLast(true);

        backBtn = new TextButton("Back to menu", skin);
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                backToMenu();
            }
        });

        float heigth = Gdx.graphics.getHeight() / 10;
        float width = Gdx.graphics.getWidth() / 3;

        //layout.debug();
        layout.setFillParent(true);

        layout.add(settingsLbl).pad(20).height(heigth).colspan(2);
        layout.row().uniform().fill().height(heigth).width(width).pad(20);

        int i = 0;
        for (String alg : algorithms) {
            TextButton btn = new TextButton(alg, skin, "toggle");
            algorithmsGroup.add(btn);
            algorithmsButtons.add(btn);
            layout.add(btn);
            i++;
            if (i == 2) layout.row().uniform().fill().height(heigth).width(width).pad(20);
        }


        layout.row().fill().pad(20).height(heigth);
        layout.add(backBtn).colspan(2);

        stage.addActor(layout);

        inputProcessor = new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.BACK) {
                    backToMenu();
                }
                return false;
            }
        };

    }

    private void backToMenu() {
        GameScreen gameScreen = (GameScreen) MyGdxGame.screens.get(MyGdxGame.GAME_SCREEN);
        int i = algorithmsGroup.getCheckedIndex();
        gameScreen.setPathfinder(i);
        ((Game) Gdx.app.getApplicationListener()).setScreen(MyGdxGame.screens.get(MyGdxGame.MENU_SCREEN));
    }

    @Override
    public void show() {

        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/28 Days Later.ttf"));
        this.font = MyGdxGame.createFont(generator, 24);
        labelS = new Label.LabelStyle(font, Color.WHITE);

        settingsLbl.setStyle(new Label.LabelStyle(MyGdxGame.createFont(generator, 36), Color.WHITE));

        for (TextButton btn : algorithmsButtons) {
            btn.getLabel().setStyle(labelS);
        }

        ninePatchDrawable = new NinePatchDrawable(MyGdxGame.getNinePatch("res/drawable-hdpi/btn_default_normal_green_9.9.png"));

        layout.setBackground(ninePatchDrawable);

        backBtn.getLabel().setStyle(labelS);

        InputMultiplexer multiplexer = new InputMultiplexer(stage, inputProcessor);
        Gdx.input.setInputProcessor(multiplexer);
        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        stage.act();
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
