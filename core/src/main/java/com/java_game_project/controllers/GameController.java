package com.java_game_project.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.MathUtils;
import com.java_game_project.Main;
import com.java_game_project.models.Consumable;
import com.java_game_project.models.FloatingText;
import com.java_game_project.models.GameWorld;
import com.java_game_project.models.Ork;
import com.java_game_project.screens.GameOverScreen;
import com.java_game_project.utils.Constants;
import com.badlogic.gdx.graphics.Color;
import java.util.Iterator;

public class GameController {
    private final GameWorld world;
    private final String currentLevel;
    private final PlayerController playerController;

    private final float ZOOM_SPEED = 0.3f;
    private final float MIN_ZOOM = 0.3f;

    public GameController(GameWorld world, String currentLevel) {
        this.world = world;
        this.currentLevel = currentLevel;
        this.playerController = new PlayerController(world.getPlayer());
    }

    public void update(float delta, OrthographicCamera camera) {
        if (world.getPlayer() == null)
            return;

        handleZoom(delta, camera);

        playerController.update();
        world.getPlayer().update(delta, world.getObstacles(), world.getTarget());

        for (Ork ork : world.getOrks()) {
            ork.updateAI(delta, world.getPlayer(), world.getObstacles(), world.getTarget());
            ork.update(delta);

            if (world.getPlayer().getBounds().overlaps(ork.getBounds())) {
                if (!world.getPlayer().isInvincible()) {
                    world.getPlayer().takeDamage(10);
                    world.addFloatingText(new FloatingText("-10", world.getPlayer().getPosition().x,
                            world.getPlayer().getPosition().y + 50, Color.RED));
                }
            }
        }

        Iterator<FloatingText> iter = world.getFloatingTexts().iterator();
        while (iter.hasNext()) {
            FloatingText text = iter.next();
            text.update(delta);
            if (text.isFinished()) {
                iter.remove();
            }
        }

        world.setTime(world.getTime() + delta);

        if (world.getPlayer().getHealth() <= 0) {
            ((Main) Gdx.app.getApplicationListener())
                    .setScreen(new GameOverScreen((Main) Gdx.app.getApplicationListener()));
        }

        if (world.getExitZone() != null && world.getPlayer().getBounds().overlaps(world.getExitZone())) {
            int currentHealth = world.getPlayer().getHealth();
            float currentTime = world.getTime();

            if (Constants.LEVEL_2_MAP.equals(currentLevel)) {
                ((Main) Gdx.app.getApplicationListener()).setScreen(new com.java_game_project.screens.GameScreen(
                        (Main) Gdx.app.getApplicationListener(), Constants.LEVEL_3_MAP, currentHealth, currentTime));
            } else if (Constants.LEVEL_3_MAP.equals(currentLevel)) {
                ((Main) Gdx.app.getApplicationListener()).setScreen(new com.java_game_project.screens.GameScreen(
                        (Main) Gdx.app.getApplicationListener(), Constants.LEVEL_2_MAP, currentHealth, currentTime));
            }
        }

        updateCamera(camera);

        for (Consumable item : world.getConsumables()) {
            item.update(delta);
            if (!item.isActive())
                continue;

            Rectangle expandedBounds = new Rectangle(item.getBounds());
            expandedBounds.x -= 5;
            expandedBounds.y -= 5;
            expandedBounds.width += 10;
            expandedBounds.height += 10;

            boolean istouching = world.getPlayer().getBounds().overlaps(expandedBounds);

            if (istouching) {
                if (world.getPlayer().getHealth() < world.getPlayer().getMaxHealth()) {
                    if (MathUtils.random() < Consumable.getHealRate() * delta) {
                        world.getPlayer().setHealth(
                                Math.min(world.getPlayer().getHealth() + 1, world.getPlayer().getMaxHealth()));
                        world.addFloatingText(new FloatingText("+1", world.getPlayer().getPosition().x,
                                world.getPlayer().getPosition().y + 50, Color.GREEN));
                    }
                } else {
                    item.consume();
                }
            }
        }
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
