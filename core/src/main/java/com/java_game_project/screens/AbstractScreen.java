package com.java_game_project.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.java_game_project.Main;
import com.java_game_project.utils.AudioManager;

public abstract class AbstractScreen implements Screen {
    protected final Main game;
    protected final SpriteBatch batch;
    protected final AudioManager audioManager;
    protected final OrthographicCamera camera;

    public AbstractScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();
        audioManager = AudioManager.getInstance();
        camera = new OrthographicCamera();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

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
