package com.java_game_project.controllers;

import com.badlogic.gdx.Gdx;
import com.java_game_project.Main;
import com.java_game_project.models.GameWorld;
import com.java_game_project.screens.GameOverScreen;
import com.java_game_project.screens.GameScreen;
import com.java_game_project.screens.VictoryScreen;
import com.java_game_project.utils.Constants;

public class ProgressionController {
    private final GameWorld world;
    private final String currentLevel;

    public ProgressionController(GameWorld world, String currentLevel) {
        this.world = world;
        this.currentLevel = currentLevel;
    }

    public void updateProgression() {
        if (world.getPlayer().getHealth() <= 0) {
            ((Main) Gdx.app.getApplicationListener())
                    .setScreen(new GameOverScreen((Main) Gdx.app.getApplicationListener()));
            return;
        }

        if (world.getTarget() != null) {
            float dist = world.getPlayer().getPosition().dst(world.getTarget().x, world.getTarget().y);
            if (dist < 100 || world.getPlayer().getBounds().overlaps(world.getTarget())) {
                ((Main) Gdx.app.getApplicationListener())
                        .setScreen(new VictoryScreen((Main) Gdx.app.getApplicationListener()));
                return;
            }
        }

        if (world.getExitZone() != null && world.getPlayer().getBounds().overlaps(world.getExitZone())) {
            int currentHealth = world.getPlayer().getHealth();
            float currentTime = world.getTime();

            if (Constants.LEVEL_2_MAP.equals(currentLevel)) {
                ((Main) Gdx.app.getApplicationListener()).setScreen(new GameScreen(
                        (Main) Gdx.app.getApplicationListener(), Constants.LEVEL_3_MAP, currentHealth, currentTime));
            } else if (Constants.LEVEL_3_MAP.equals(currentLevel)) {
                ((Main) Gdx.app.getApplicationListener()).setScreen(new GameScreen(
                        (Main) Gdx.app.getApplicationListener(), Constants.LEVEL_2_MAP, currentHealth, currentTime));
            }
        }
    }
}
