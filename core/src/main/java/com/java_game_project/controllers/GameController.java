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
import com.java_game_project.utils.NarrationConstants;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.java_game_project.models.Projectile;
import java.util.Iterator;
import com.java_game_project.utils.EntityState;

public class GameController {
    private final GameWorld world;
    private final String currentLevel;
    private final PlayerController playerController;

    private final float ZOOM_SPEED = 0.3f;
    private final float MIN_ZOOM = 0.3f;

    private float attackCooldown = 0;
    private float attackAnimTimer = 0;
    private final float ATTACK_RATE = 0.6f;
    private final float ATTACK_ANIM_DURATION = 0.6f;
    private final Vector3 touchPoint = new Vector3();

    private boolean startThoughtTriggered = false;
    private boolean exitThoughtTriggered = false;
    private boolean lowHealthTriggered = false;

    public GameController(GameWorld world, String currentLevel) {
        this.world = world;
        this.currentLevel = currentLevel;
        this.playerController = new PlayerController(world.getPlayer());
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
            camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

            float pX = world.getPlayer().getPosition().x + world.getPlayer().getBounds().width / 2;
            float pY = world.getPlayer().getPosition().y + world.getPlayer().getBounds().height / 2;

            Ork targetOrk = findNearestOrk(500.0f);
            float tX, tY;

            if (targetOrk != null) {
                tX = targetOrk.getPosition().x + targetOrk.getBounds().width / 2;
                tY = targetOrk.getPosition().y + targetOrk.getBounds().height / 2;
            } else {
                tX = touchPoint.x;
                tY = touchPoint.y;
            }

            float dx = tX - pX;
            float dy = tY - pY;
            float angle = MathUtils.atan2(dy, dx) * MathUtils.radiansToDegrees;
            world.getPlayer().setRotation(angle);

            world.addProjectile(new Projectile(pX, pY, tX, tY));

            attackCooldown = ATTACK_RATE;
            attackAnimTimer = ATTACK_ANIM_DURATION;
        }

        if (attackAnimTimer > 0) {
            world.getPlayer().setState(EntityState.ATTACKING);
        }

        world.getPlayer().update(delta, world.getObstacles(), world.getTarget());

        Iterator<Projectile> pIter = world.getProjectiles().iterator();
        while (pIter.hasNext()) {
            Projectile p = pIter.next();
            p.update(delta);
            boolean hit = false;
            for (Ork ork : world.getOrks()) {
                if (p.getBounds().overlaps(ork.getBounds())) {
                    ork.damage(25);
                    world.addFloatingText(new FloatingText("-25", ork.getPosition().x, ork.getPosition().y + 50,
                            Color.RED, FloatingText.Type.DAMAGE));
                    hit = true;
                    break;
                }
            }

            if (hit || world.getPlayer().getPosition().dst(p.getPosition()) > 1000) {
                p.setActive(false);
                pIter.remove();
            }

            if (hit || world.getPlayer().getPosition().dst(p.getPosition()) > 1000) {
                p.setActive(false);
                pIter.remove();
            }
        }

        Iterator<Ork> orkIter = world.getOrks().iterator();
        for (; orkIter.hasNext();) {
            Ork ork = orkIter.next();

            if (ork.getHealth() <= 0) {
                orkIter.remove();
                world.addFloatingText(new FloatingText("Mort !", ork.getPosition().x, ork.getPosition().y, Color.GRAY,
                        FloatingText.Type.THOUGHT));
            }
        }

        for (Ork ork : world.getOrks()) {
            ork.updateAI(delta, world.getPlayer(), world.getObstacles(), world.getTarget());
            ork.update(delta);

            if (world.getPlayer().getBounds().overlaps(ork.getBounds())) {
                if (MathUtils.random() < 5.0f * delta) {
                    world.getPlayer().damage(1);
                    world.addFloatingText(new FloatingText("-1", world.getPlayer().getPosition().x,
                            world.getPlayer().getPosition().y + 50, Color.RED, FloatingText.Type.DAMAGE));
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
                                world.getPlayer().getPosition().y + 50, Color.GREEN, FloatingText.Type.HEAL));
                    }
                } else {
                    item.consume();
                }
            }
        }

        handleNarration();
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

    private void handleNarration() {
        if (!startThoughtTriggered && world.getTime() > 1.0f) {
            world.addFloatingText(new FloatingText(com.java_game_project.utils.NarrationConstants.GAME_START_THOUGHT,
                    world.getPlayer().getPosition().x - 100, world.getPlayer().getPosition().y + 100, Color.CYAN,
                    FloatingText.Type.THOUGHT));
            startThoughtTriggered = true;
        }

        if (!exitThoughtTriggered && world.getExitZone() != null) {
            float dist = world.getPlayer().getPosition().dst(world.getExitZone().x, world.getExitZone().y);
            if (dist < 400) {
                world.addFloatingText(new FloatingText(com.java_game_project.utils.NarrationConstants.EXIT_NEAR_THOUGHT,
                        world.getPlayer().getPosition().x - 100, world.getPlayer().getPosition().y + 100, Color.CYAN,
                        FloatingText.Type.THOUGHT));
                exitThoughtTriggered = true;
            }
        }

        if (!lowHealthTriggered && world.getPlayer().getHealth() < 30) {
            world.addFloatingText(new FloatingText(NarrationConstants.LOW_HEALTH_THOUGHT,
                    world.getPlayer().getPosition().x, world.getPlayer().getPosition().y + 100, Color.ORANGE,
                    FloatingText.Type.THOUGHT));
            lowHealthTriggered = true;
        } else if (lowHealthTriggered && world.getPlayer().getHealth() > 50) {
            lowHealthTriggered = false;
        }
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
