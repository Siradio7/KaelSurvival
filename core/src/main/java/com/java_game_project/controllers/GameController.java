package com.java_game_project.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.java_game_project.models.GameWorld;
import com.java_game_project.models.Ork;
import com.java_game_project.utils.Constants;

public class GameController {
    private final GameWorld world;
    private final PlayerController playerController;

    private final float ZOOM_SPEED = 0.3f;
    private final float MIN_ZOOM = 0.3f;

    public GameController(GameWorld world) {
        this.world = world;
        this.playerController = new PlayerController(world.getPlayer());
    }

    public void update(float delta, OrthographicCamera camera) {
        if (world.getPlayer() == null) return;

        handleZoom(delta, camera);

        playerController.update();
        world.getPlayer().update(delta, world.getObstacles(), world.getTarget());

        for (Ork ork : world.getOrks()) {
            ork.updateAI(delta, world.getPlayer(), world.getObstacles(), world.getTarget());
            ork.update(delta);
        }

        updateCamera(camera);
    }

    private void handleZoom(float delta, OrthographicCamera camera) {
        float maxZoomX = Constants.WINDOW_WIDTH / camera.viewportWidth;
        float maxZoomY = Constants.WINDOW_HEIGHT / camera.viewportHeight;
        float maxZoomAllowed = Math.min(maxZoomX, maxZoomY);

        if (Gdx.input.isKeyPressed(Input.Keys.P)) {
            camera.zoom -= ZOOM_SPEED * delta;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.M)) {
            camera.zoom += ZOOM_SPEED * delta;
        }

        camera.zoom = MathUtils.clamp(camera.zoom, MIN_ZOOM, maxZoomAllowed);
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
