package com.java_game_project.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.java_game_project.Main;
import com.java_game_project.utils.AudioManager;
import com.java_game_project.utils.Constants;
import com.java_game_project.utils.MapManager;

public class GameScreen extends AbstractScreen {
    private final MapManager mapManager = MapManager.getInstance();
    private final AudioManager audioManager = AudioManager.getInstance();

    public GameScreen(Main game) {
        super(game);
        audioManager.playMusic(Constants.MENU_MUSIC, true);
    }

    @Override
    public void show() {
        camera.setToOrtho(false, 800, 480);
        mapManager.addMaps(Constants.MAPS_PATH);
        mapManager.loadMap(Constants.LEVEL_3_MAP);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mapManager.render(camera);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        System.out.println(width + " ### " + height);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        mapManager.dispose();
    }
}
