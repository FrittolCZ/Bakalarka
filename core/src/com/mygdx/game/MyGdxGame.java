package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.GameClasses.GameMode;
import com.mygdx.game.Scores.HighScoreManager;
import com.mygdx.game.screens.GameOverScreen;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.screens.MainMenuScreen;
import com.mygdx.game.screens.MapSelectScreen;
import com.mygdx.game.screens.PreGameScreen;
import com.mygdx.game.screens.ScoreScreen;
import com.mygdx.game.screens.SettingsScreen;

import java.util.ArrayList;


public class MyGdxGame extends Game {

    public static final ArrayList<Screen> screens = new ArrayList<Screen>();
    public static final short GAME_SCREEN = 0;
    public static final short MENU_SCREEN = 1;
    public static final short SETTINGS_SCREEN = 2;
    public static final short MAP_SELECT_SCREEN = 3;
    public static final short PRE_GAME_SCREEN = 4;
    public static final short GAME_OVER_SCREEN = 5;
    public static final short HIGH_SCORE_SCREEN = 6;

    @Override
    public void create() {
        HighScoreManager scoreManager = new HighScoreManager();
        Skin skin = new Skin(Gdx.files.internal("uiskin/uiskin.json"));
        SpriteBatch mainBatch = new SpriteBatch();
        screens.add(new GameScreen(skin, mainBatch));
        screens.add(new MainMenuScreen(skin, mainBatch));
        screens.add(new SettingsScreen(skin, mainBatch));
        screens.add(new MapSelectScreen(skin, mainBatch));
        screens.add(new PreGameScreen(skin, mainBatch));
        screens.add(new GameOverScreen(skin, mainBatch, scoreManager));
        screens.add(new ScoreScreen(skin, mainBatch, scoreManager));

        GameOverScreen screen = (GameOverScreen) MyGdxGame.screens.get(MyGdxGame.GAME_OVER_SCREEN);
        screen.setDiffLlv(1);
        screen.setGameMode(GameMode.MARATHON);
        screen.setScore(1);


        setScreen(screens.get(MENU_SCREEN));
    }

    // metoda která vytvoří font v požadované velikosti
    public static BitmapFont createFont(FreeTypeFontGenerator generator, float dp) {
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        int fontSize = (int) (dp * Gdx.graphics.getDensity());
        parameter.size = fontSize;
        return generator.generateFont(parameter);
    }

    public static NinePatch getNinePatch(String fname) {
        final Texture t = new Texture(Gdx.files.internal(fname));
        return new NinePatch(new TextureRegion(t, 1, 1, t.getWidth() - 2, t.getHeight() - 2), 5, 5, 5, 5);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }


}
