package com.java_game_project.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import com.java_game_project.Main;
import com.java_game_project.utils.AssetLoader;
import com.java_game_project.utils.Constants;

public class MenuScreen extends AbstractScreen {
    private Stage stage;
    private Skin skin;
    private Texture background;
    private BitmapFont titleFont;
    private float backgroundScroll = 0f;

    public MenuScreen(Main game) {
        super(game);
        audioManager.playMusic(Constants.MENU_MUSIC, true);
    }

    @Override
    public void show() {
        camera.setToOrtho(false, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        background = AssetLoader.getTexture(Constants.BACKGROUND);
        background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal(Constants.SKIN));
        titleFont = new BitmapFont(Gdx.files.internal(Constants.FONT));

        TextButton playButton = new TextButton("JOUER", skin);
        TextButton quitButton = new TextButton("QUITTER", skin);
        playButton.getLabel().setFontScale(0.5f);
        quitButton.getLabel().setFontScale(0.5f);

        Table mainTable = new Table();
        mainTable.setFillParent(true);

        Table buttonTable = new Table();
        buttonTable.add(playButton).size(220, 60).padBottom(20).row();
        buttonTable.add(quitButton).size(220, 60);

        mainTable.add(buttonTable).expand().center();
        mainTable.row();
        mainTable.add(new com.badlogic.gdx.scenes.scene2d.ui.Label(
                "v1.0 - Created by Mamadou Siradiou, Sara and Cesaire", skin))
                .bottom().padBottom(10);

        stage.addActor(mainTable);

        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                audioManager.playSound(Constants.MOUSE_CLICK_SOUND);
                Gdx.app.postRunnable(() -> {
                    game.setScreen(new IntroScreen(game));
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
        batch.draw(background, 0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, (int) backgroundScroll, 0,
                background.getWidth(), background.getHeight(), false, false);
        batch.end();
        stage.act(delta);
        stage.draw();

        if (Gdx.input.isKeyPressed(Input.Keys.P)) {
            camera.zoom -= 0.2f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.M)) {
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
        skin.dispose();
        titleFont.dispose();
        audioManager.dispose();
    }
}
