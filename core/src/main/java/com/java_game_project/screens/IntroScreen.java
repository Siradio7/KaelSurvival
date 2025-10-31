package com.java_game_project.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Align;
import com.java_game_project.Main;
import com.java_game_project.utils.Constants;
import com.java_game_project.utils.NarrationConstants;

public class IntroScreen extends AbstractScreen {
    private BitmapFont font;
    private int currentLine = 0;
    private float lineTimer = 0f;
    private float displayDuration = 5f;

    public IntroScreen(Main game) {
        super(game);
        audioManager.playMusic(Constants.INTRO_MUSIC, false);
    }

    @Override
    public void show() {
        font = new BitmapFont(Gdx.files.internal(Constants.FONT));
        font.getData().setScale(1.1f);
    }

    @Override
    public void render(float delta) {
        lineTimer += delta;

        // Nettoyage de l’écran
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        font.setColor(1, 1, 1, Math.min(1, lineTimer / 2f));

        String text = NarrationConstants.INTRO_LINES[currentLine];
        font.draw(batch, text, 0, Gdx.graphics.getHeight() / 2f, Gdx.graphics.getWidth(), Align.center, true);
        batch.end();

        if (lineTimer > displayDuration) {
            lineTimer = 0f;
            currentLine++;

            if (currentLine >= NarrationConstants.INTRO_LINES.length) {
                Gdx.gl.glClearColor(0, 0, 0, 1);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                game.setScreen(new GameScreen(game));
            }
        }
    }

    @Override
    public void resize(int width, int height) {}

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
        font.dispose();
        audioManager.dispose();
    }
}

