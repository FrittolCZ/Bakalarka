package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.mygdx.game.MyGdxGame;

import java.util.ArrayList;

public class MapSelectScreen implements Screen {

    private ArrayList<TiledMap> maps = new ArrayList<TiledMap>();

    private TiledMap selectedMap;
    private int selectedMapIndex = 0;

    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private float mapWidth = 0, mapHeight = 0;
    private float tileSize;

    private SpriteBatch batch;
    private Skin skin;

    private Stage stage;

    private Table layout;

    private TextButton prevMapBtn, nextMapBtn;

    private TextButton selMapBtn;

    private TextButton backBtn;

    private Label selectMapLbl;

    private BitmapFont font;
    private Label.LabelStyle labelS;
    private FreeTypeFontGenerator generator;

    private InputProcessor inputProcessor;

    private NinePatchDrawable ninePatchDrawable;

    public MapSelectScreen(Skin skin, SpriteBatch batch) {

        this.batch = batch;
        this.skin = skin;

        loadMaps();

        this.camera = new OrthographicCamera();

        selectMapLbl = new Label("Select Map", skin);

        prevMapBtn = new TextButton("Prev", skin);
        prevMapBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changeMap(-1);
            }
        });

        nextMapBtn = new TextButton("Next", skin);
        nextMapBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changeMap(1);
            }
        });

        selMapBtn = new TextButton("Select", skin);
        selMapBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameScreen screen = (GameScreen) MyGdxGame.screens.get(MyGdxGame.GAME_SCREEN);
                screen.setMap(selectedMap);
                screen.startGame();
                ((Game) Gdx.app.getApplicationListener()).setScreen(MyGdxGame.screens.get(MyGdxGame.PRE_GAME_SCREEN));
            }
        });

        backBtn = new TextButton("Back", skin);
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(MyGdxGame.screens.get(MyGdxGame.MENU_SCREEN));
            }
        });

        stage = new Stage();

        layout = new Table();

        layout.setFillParent(true);
        //layout.debug();

        //layout.pad(30);
        layout.add(selectMapLbl).colspan(2).height(Gdx.graphics.getHeight() * 0.11f);
        layout.row().width(Gdx.graphics.getWidth() * 0.1f).expandY().fillY();
        layout.add(prevMapBtn).bottom().left();
        layout.add(nextMapBtn).bottom().right();
        layout.row();
        layout.add(backBtn).expandX().top().width(Gdx.graphics.getWidth() * 0.2f).height(Gdx.graphics.getHeight() * 0.11f);
        layout.add(selMapBtn).expandX().bottom().width(Gdx.graphics.getWidth() * 0.2f).height(Gdx.graphics.getHeight() * 0.11f);
        //layout.bottom();
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

    @Override
    public void show() {

        loadMaps();

        selectedMap = maps.get(selectedMapIndex);

        TiledMapTileLayer mainLayer = (TiledMapTileLayer) selectedMap.getLayers().get(0);
        tileSize = mainLayer.getTileWidth();
        mapWidth = mainLayer.getWidth() * tileSize;
        mapHeight = mainLayer.getHeight() * tileSize;


        camera.setToOrtho(false, mapWidth * 1.3f, mapHeight * 1.3f);
        camera.translate(-(mapWidth * 0.15f), -(mapHeight * 0.15f)
        );
        camera.update();

        renderer = new OrthogonalTiledMapRenderer(selectedMap);

        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/28 Days Later.ttf"));
        this.font = MyGdxGame.createFont(generator, 16);
        labelS = new Label.LabelStyle(font, Color.WHITE);

        selectMapLbl.setStyle(new Label.LabelStyle(MyGdxGame.createFont(generator, 36), Color.WHITE));

        prevMapBtn.getLabel().setStyle(labelS);
        nextMapBtn.getLabel().setStyle(labelS);
        selMapBtn.getLabel().setStyle(labelS);
        backBtn.getLabel().setStyle(labelS);

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
        renderer.setView(camera);


        stage.act();

        batch.begin();

        stage.draw();

        batch.end();

        renderer.render();

    }

    private void changeMap(int param) {
        if (selectedMapIndex + param >= maps.size()) {
            selectedMapIndex = 0;
        } else if (selectedMapIndex + param < 0) {
            selectedMapIndex = maps.size() - 1;
        } else {
            selectedMapIndex += param;
        }
        selectedMap = maps.get(selectedMapIndex);
        renderer.setMap(selectedMap);
    }

    private void loadMaps() {
        maps.clear();
        FileHandle dir = Gdx.files.internal("map"); // načte složku s mapami
        try {
            for (FileHandle map : dir.list()) {
                maps.add(new TmxMapLoader().load("map/" + map.nameWithoutExtension() + ".tmx"));
            }
        } catch (Exception e) {
            Gdx.app.log("Chyba", "něco se pokazilo", e);
        }
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
