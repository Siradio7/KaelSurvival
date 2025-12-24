package com.java_game_project.controllers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.java_game_project.models.GameWorld;
import com.java_game_project.models.Ork;
import com.java_game_project.utils.Constants;

public class GameController {
    private final GameWorld world;
    private final PlayerController playerController;

    public GameController(GameWorld world) {
        this.world = world;
        this.playerController = new PlayerController(world.getPlayer());
    }

    public void update(float delta, OrthographicCamera camera) {
        if (world.getPlayer() == null) return;

        playerController.update();
        world.getPlayer().update(delta, world.getObstacles(), world.getTarget());

        for (Ork ork : world.getOrks()) {
            ork.updateAI(delta, world.getPlayer(), world.getObstacles(), world.getTarget());
            ork.update(delta);
        }

        updateCamera(camera);
    }

    private void updateCamera(OrthographicCamera camera) {
        float targetX = world.getPlayer().getPosition().x + world.getPlayer().getBounds().width / 2;
        float targetY = world.getPlayer().getPosition().y + world.getPlayer().getBounds().height / 2;

        float halfViewWidth = (camera.viewportWidth * camera.zoom) / 2f;
        float halfViewHeight = (camera.viewportHeight * camera.zoom) / 2f;

        float camX = Math.max(halfViewWidth, Math.min(targetX, Constants.WINDOW_WIDTH - halfViewWidth));
        float camY = Math.max(halfViewHeight, Math.min(targetY, Constants.WINDOW_HEIGHT - halfViewHeight));

        camera.position.set(camX, camY, 0);
        camera.update();
    }
}
