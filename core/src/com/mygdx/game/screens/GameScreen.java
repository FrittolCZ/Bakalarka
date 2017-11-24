package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.GameClasses.GUI.UIStage;
import com.mygdx.game.GameClasses.GameManager;
import com.mygdx.game.GameClasses.GameMode;


/**
 * GameScreen is the class for main game Screen where user will play the game.
 */
public class GameScreen implements Screen, GestureListener {

    private Skin skin;
    public static TextureAtlas myTextures = new TextureAtlas(Gdx.files.internal("myTextures/MySpriteSheet.txt"));
    //private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private GameManager gameManager;
    private float mapWidth, mapHeight;
    private float tileSize;
    private SpriteBatch batch;
    private SpriteBatch hudBatch;
    private UIStage uiStage;
    private GameMode mode;
    private TiledMap map;
    private int diffLvl;
    private boolean gameRunning = false;
    private int pathfinderIndex = 0;

    /**
     * Creates a new GameScreen
     *
     * @param skin  object containing graphics skin for game GUI
     * @param batch object of SpriteBatch for drawing
     */
    public GameScreen(Skin skin, SpriteBatch batch) {
        this.batch = batch;
        this.hudBatch = new SpriteBatch();
        this.skin = skin;
    }

    /**
     * Sets game map and creates new map renderer
     *
     * @param map Object of TiledMap
     */
    public void setMap(TiledMap map) {
        this.map = map;
        TiledMapTileLayer mainLayer = (TiledMapTileLayer) map.getLayers().get(0);
        tileSize = mainLayer.getTileWidth();
        mapWidth = mainLayer.getWidth() * tileSize;
        mapHeight = mainLayer.getHeight() * tileSize;
        renderer = new OrthogonalTiledMapRenderer(map);
    }

    /**
     * Initializes new game with selected game mode and difficulty level
     * @param mode selected from GameMode
     * @param diffLvl an integer from 1 to 3
     */
    public void initGame(GameMode mode, int diffLvl) {
        gameManager = new GameManager(map, mode, diffLvl);
        gameManager.setPathfinder(pathfinderIndex);
        uiStage = new UIStage(skin, gameManager);
    }

    /**
     * @return whether game was started and is still running
     */
    public boolean isGameRunning() {
        return gameRunning;
    }

    /**
     * Sets the game state to running
     */
    public void startGame() {
        gameRunning = true;
    }

    /**
     * Sets the game state to stopped
     */
    public void stopGame() {
        gameRunning = false;
    }

    /**
     * Sets the active pathfinder for game
     *
     * @param pathfinder ID of selected pathfinder
     */
    public void setPathfinder(int pathfinder) {
        this.pathfinderIndex = pathfinder;
    }

    /**
     * Called when GameScreen becomes the current Game screen.
     */
    @Override
    public void show() {
        gameRunning = true;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, mapWidth * 0.8f, mapHeight * 0.8f);
        camera.update();


        GestureDetector gd = new GestureDetector(this);
        InputMultiplexer im = new InputMultiplexer(uiStage, gd);
        Gdx.input.setInputProcessor(im);
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.setView(camera);
        renderer.render();
        batch.setProjectionMatrix(camera.combined);

        try {
            gameManager.update(delta);
            uiStage.act(delta);
        }catch (Exception e)
        {
            Gdx.app.log("Update", "Chyba v updatu", e);
        }

        batch.begin();
        try {
            gameManager.draw(batch);
        }
        catch (Exception e)
        {
            Gdx.app.log("Draw", "Chyba vykresleni", e);
        }
        batch.end();

        hudBatch.begin();
        uiStage.draw();
        hudBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        uiStage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return true;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        Vector3 pos = new Vector3(x, y, 0);
        camera.unproject(pos);
        Vector2 posInGame = new Vector2((int) (pos.x / tileSize) * tileSize, (int) (pos.y / tileSize) * tileSize);
        try {
            uiStage.onClick(posInGame);

        } catch (Exception e) {
            Gdx.app.log("Chyba", "tap", e);
        }
        return true;
    }

    @Override
    public boolean longPress(float x, float y) {
        return true;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return true;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        if ((camera.position.x - (camera.viewportWidth / 2) - deltaX) > 0 && (camera.position.x + (camera.viewportWidth / 2) - deltaX) < mapWidth) {
            camera.translate(-deltaX, 0);
            camera.update();
        }
        if ((camera.position.y - (camera.viewportHeight / 2) + deltaY) > 0 && (camera.position.y + (camera.viewportHeight / 2) + deltaY) < mapHeight + (tileSize * 0.8f)) {
            camera.translate(0, deltaY);
            camera.update();
        }
        return true;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return true;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
                         Vector2 pointer1, Vector2 pointer2) {
        return true;
    }

    @Override
    public void pinchStop() {

    }
}
