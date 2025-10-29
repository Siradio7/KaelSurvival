package com.java_game_project;

import com.badlogic.gdx.Game;
import com.java_game_project.screens.MenuScreen;
import com.java_game_project.utils.AudioManager;
import com.java_game_project.utils.Constants;

public class Main extends Game {
    AudioManager audioManager = AudioManager.getInstance();

    @Override
    public void create() {
        audioManager.loadMusics(Constants.MUSICS_PATH);
        audioManager.loadSounds(Constants.SOUNDS_PATH);
        setScreen(new MenuScreen(this));
    }

    @Override
    public void dispose() {
        audioManager.dispose();
    }
}
