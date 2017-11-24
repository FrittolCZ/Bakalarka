package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.GameClasses.GameMode;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Scores.HighScore;
import com.mygdx.game.Scores.HighScoreManager;

/**
 * Created by fanda on 19.06.2017.
 */

public class GameOverScreen implements Screen {

    private SpriteBatch batch;
    private Stage stage;
    private Skin skin;

    private TextField nameField;

    private Label gameOver, score, scoreLbl, diffLlv, diffLbl, gameMode, modeLbl;

    private Table layout;

    private BitmapFont font;
    private Label.LabelStyle labelS;
    private FreeTypeFontGenerator generator;

    private TextButton menu;

    private HighScoreManager scoreManager;

    private NinePatchDrawable ninePatchDrawable;

    public GameOverScreen(Skin skin, SpriteBatch batch, final HighScoreManager scoreManager) {
        this.batch = batch;
        this.skin = skin;
        this.scoreManager = scoreManager;

        nameField = new TextField("", skin);


        gameOver = new Label("Game Over", skin);
        score = new Label("", skin);
        score.setAlignment(Align.right);
        scoreLbl = new Label("Score: ", skin);
        diffLlv = new Label("", skin);
        diffLlv.setAlignment(Align.right);
        diffLbl = new Label("Difficulty: ", skin);
        gameMode = new Label("", skin);
        gameMode.setAlignment(Align.right);
        modeLbl = new Label("Game mode: ", skin);

        menu = new TextButton("Main menu", skin);


        menu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String name = nameField.getText().trim();
                if(name.length() == 0) name = "Default";
                scoreManager.storeNewScore(new HighScore(score.getText().toString(), diffLlv.getText().toString(), gameMode.getText().toString(), name));
                ((Game) Gdx.app.getApplicationListener()).setScreen(MyGdxGame.screens.get(MyGdxGame.MENU_SCREEN));
            }
        });
        stage = new Stage();


        float height = Gdx.graphics.getHeight() / 10;
        float width = Gdx.graphics.getWidth() / 4;

        layout = new Table();
        layout.setFillParent(true);
        //layout.debug();
        layout.row().pad(20);
        layout.add(gameOver).height(height).colspan(2);
        layout.row().height(height).width(width * 2 + 40).pad(20).fill();
        layout.add(nameField).colspan(2);
        layout.row().height(height).width(width).pad(20);
        layout.add(scoreLbl).left();
        layout.add(score).right();
        layout.row().height(height).width(width).pad(20);
        layout.add(diffLbl).left();
        layout.add(diffLlv).right();
        layout.row().height(height).width(width).pad(20);
        layout.add(modeLbl).left();
        layout.add(gameMode).right();
        layout.row().height(height).width(width).pad(20);
        layout.add(menu).colspan(2);

        stage.addActor(layout);

    }

    public void setScore(int score) {
        this.score.setText(String.valueOf(score));
    }

    public void setDiffLlv(int diffLlv) {
        this.diffLlv.setText(String.valueOf(diffLlv));
    }

    public void setGameMode(GameMode mode) {
        this.gameMode.setText(mode.getName());
    }

    @Override
    public void show() {
        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/28 Days Later.ttf"));
        this.font = MyGdxGame.createFont(generator, 24);
        labelS = new Label.LabelStyle(font, Color.WHITE);

        gameOver.setStyle(new Label.LabelStyle(MyGdxGame.createFont(generator, 36), Color.WHITE));
        menu.getLabel().setStyle(labelS);
        score.setStyle(labelS);
        diffLlv.setStyle(labelS);
        gameMode.setStyle(labelS);
        modeLbl.setStyle(labelS);
        scoreLbl.setStyle(labelS);
        diffLbl.setStyle(labelS);

        ninePatchDrawable = new NinePatchDrawable(MyGdxGame.getNinePatch("res/drawable-hdpi/btn_default_normal_green_9.9.png"));

        layout.setBackground(ninePatchDrawable);

        nameField.getStyle().font = this.font;

        Gdx.input.setInputProcessor(this.stage);
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