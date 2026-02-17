package com.java_game_project.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.java_game_project.models.Consumable;
import com.java_game_project.models.FloatingText;
import com.java_game_project.models.GameWorld;
import com.java_game_project.models.Ork;
import com.java_game_project.models.Projectile;
import com.java_game_project.utils.Constants;
import com.java_game_project.utils.EntityState;

import java.util.Iterator;

public class GameController {
    private final GameWorld world;
    private final PlayerController playerController;
    private final ProgressionController progressionController;
    private final NarrationController narrationController;

    private static final float ZOOM_SPEED = 0.3f;
    private static final float MIN_ZOOM = 0.3f;

    private float attackCooldown = 0;
    private float attackAnimTimer = 0;
    private static final float ATTACK_RATE = 0.6f;
    private static final float ATTACK_ANIM_DURATION = 0.6f;
    private final Vector3 touchPoint = new Vector3();

    public GameController(GameWorld world, String currentLevel) {
        this.world = world;
        this.playerController = new PlayerController(world.getPlayer());
        this.progressionController = new ProgressionController(world, currentLevel);
        this.narrationController = new NarrationController(world);
    }

    public void update(float delta, OrthographicCamera camera) {
        if (world.getPlayer() == null)
            return;

        handleZoom(delta, camera);

        if (attackCooldown > 0)
            attackCooldown -= delta;
        if (attackAnimTimer > 0)
            attackAnimTimer -= delta;

        playerController.update(attackAnimTimer <= 0);

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && attackCooldown <= 0) {
            fireProjectile(camera);
        }

        if (attackAnimTimer > 0) {
            world.getPlayer().setState(EntityState.ATTACKING);
        }

        world.getPlayer().update(delta, world.getObstacles(), world.getTarget());
        updateProjectiles(delta);
        updateOrks(delta);
        updateFloatingTexts(delta);

        world.setTime(world.getTime() + delta);
        progressionController.updateProgression();

        updateCamera(camera);
        updateConsumables(delta);
        narrationController.update();
    }

    private void fireProjectile(OrthographicCamera camera) {
        camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

        float pX = world.getPlayer().getPosition().x + world.getPlayer().getBounds().width / 2;
        float pY = world.getPlayer().getPosition().y + world.getPlayer().getBounds().height / 2;

        Ork targetOrk = findNearestOrk(Constants.AUTO_AIM_RANGE);

        float tX = touchPoint.x;
        float tY = touchPoint.y;

        if (targetOrk != null) {
            tX = targetOrk.getPosition().x + targetOrk.getBounds().width / 2;
            tY = targetOrk.getPosition().y + targetOrk.getBounds().height / 2;
        } else if (Constants.AUTO_AIM_REQUIRE_TARGET) {
            return;
        }

        float dx = tX - pX;
        float dy = tY - pY;
        float angle = MathUtils.atan2(dy, dx) * MathUtils.radiansToDegrees;
        world.getPlayer().setRotation(angle);

        world.addProjectile(new Projectile(pX, pY, tX, tY));
        attackCooldown = ATTACK_RATE;
        attackAnimTimer = ATTACK_ANIM_DURATION;
    }

    private void updateProjectiles(float delta) {
        for (int i = world.getProjectiles().size - 1; i >= 0; i--) {
            Projectile p = world.getProjectiles().get(i);
            p.update(delta);

            boolean hit = false;
            for (Ork ork : world.getOrks()) {
                if (ork.getHealth() > 0 && p.getBounds().overlaps(ork.getBounds())) {
                    ork.damage(25);
                    world.addFloatingText(new FloatingText("-25", ork.getPosition().x, ork.getPosition().y + 50,
                            Color.RED, FloatingText.Type.DAMAGE));
                    hit = true;
                    break;
                }
            }

            if (hit || !p.isActive() || world.getPlayer().getPosition().dst(p.getPosition()) > 1000) {
                p.setActive(false);
                world.getProjectiles().removeIndex(i);
            }
        }
    }

    private void updateOrks(float delta) {
        for (int i = world.getOrks().size - 1; i >= 0; i--) {
            Ork ork = world.getOrks().get(i);
            if (ork.getHealth() <= 0) {
                world.addFloatingText(new FloatingText("Mort !", ork.getPosition().x, ork.getPosition().y, Color.GRAY,
                        FloatingText.Type.THOUGHT));
                world.getOrks().removeIndex(i);
            }
        }

        for (Ork ork : world.getOrks()) {
            ork.updateAI(delta, world.getPlayer(), world.getObstacles(), world.getTarget());
            ork.update(delta);

            if (world.getPlayer().getBounds().overlaps(ork.getBounds()) && MathUtils.random() < 5.0f * delta) {
                world.getPlayer().damage(1);
                world.addFloatingText(new FloatingText("-1", world.getPlayer().getPosition().x,
                        world.getPlayer().getPosition().y + 50, Color.RED, FloatingText.Type.DAMAGE));
            }
        }
    }

    private void updateFloatingTexts(float delta) {
        Iterator<FloatingText> iter = world.getFloatingTexts().iterator();
        while (iter.hasNext()) {
            FloatingText text = iter.next();
            text.update(delta);
            if (text.isFinished()) {
                iter.remove();
            }
        }
    }

    private void updateConsumables(float delta) {
        for (Consumable item : world.getConsumables()) {
            item.update(delta);
            if (!item.isActive())
                continue;

            Rectangle expandedBounds = new Rectangle(item.getBounds());
            expandedBounds.x -= 5;
            expandedBounds.y -= 5;
            expandedBounds.width += 10;
            expandedBounds.height += 10;

            boolean isTouching = world.getPlayer().getBounds().overlaps(expandedBounds);

            if (isTouching) {
                if (world.getPlayer().getHealth() < world.getPlayer().getMaxHealth()) {
                    if (MathUtils.random() < Consumable.getHealRate() * delta) {
                        world.getPlayer().setHealth(
                                Math.min(world.getPlayer().getHealth() + 1, world.getPlayer().getMaxHealth()));
                        world.addFloatingText(new FloatingText("+1", world.getPlayer().getPosition().x,
                                world.getPlayer().getPosition().y + 50, Color.GREEN, FloatingText.Type.HEAL));
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

    private Ork findNearestOrk(float range) {
        Ork nearest = null;
        float minDst = range;
        Vector2 playerPos = world.getPlayer().getPosition();

        for (Ork ork : world.getOrks()) {
            if (ork.getHealth() <= 0)
                continue;

            float dst = playerPos.dst(ork.getPosition());

            if (dst < minDst) {
                minDst = dst;
                nearest = ork;
            }
        }

        return nearest;
    }
}
