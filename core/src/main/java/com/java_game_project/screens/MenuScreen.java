package com.java_game_project.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import com.java_game_project.Main;
import com.java_game_project.utils.AudioManager;
import com.java_game_project.utils.Constants;

public class MenuScreen implements Screen {
    private final Main game;
    private Stage stage;
    private Skin skin;
    private SpriteBatch batch;
    private Texture background;
    private OrthographicCamera camera;
    private Label titleLabel;
    private BitmapFont titleFont;
    private float backgroundScroll = 0f;
    private final AudioManager audioManager = AudioManager.getInstance();

    public MenuScreen(Main game) {
        audioManager.stopMusic();
        audioManager.playMusic(Constants.MENU_MUSIC, true);
        this.game = game;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        batch = new SpriteBatch();
        background = new Texture(Constants.BACKGROUND);
        background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal(Constants.SKIN));
        titleFont = new BitmapFont(Gdx.files.internal(Constants.FONT));

        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = titleFont;
        titleStyle.fontColor = Color.GOLD;
        titleLabel = new Label(Constants.GAME_TITLE, titleStyle);
        titleLabel.setSize(400, 100);
        titleLabel.setFontScale(3.0F);

        TextButton playButton = new TextButton("JOUER", skin);
        TextButton quitButton = new TextButton("QUITTER", skin);
        playButton.getLabel().setFontScale(0.5f);
        quitButton.getLabel().setFontScale(0.5f);

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        table.add(titleLabel).padBottom(50).row();
        table.add(playButton).size(220, 60).padBottom(20).row();
        table.add(quitButton).size(220, 60);

        stage.addActor(table);

        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                audioManager.playSound(Constants.MOUSE_CLICK_SOUND);
                Gdx.app.postRunnable(() -> {

                });
            }
        });

        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                audioManager.playSound(Constants.MOUSE_CLICK_SOUND);
                Gdx.app.exit();
            }
        });
    }

    @Override
    public void render(float delta) {
        backgroundScroll += delta * 0.1f;
        ScreenUtils.clear(0, 0, 0, 1);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(background, 0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, (int)(backgroundScroll * 100), 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, false, false);
        batch.end();
        stage.act(delta);
        stage.draw();

        if(Gdx.input.isKeyPressed(Input.Keys.P)) {
            camera.zoom -= 0.2f;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.M)) {
            camera.zoom += 0.2f;
        }

        camera.update();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        stage.dispose();
        batch.dispose();
        background.dispose();
        skin.dispose();
        titleFont.dispose();
        audioManager.dispose();
    }
}
