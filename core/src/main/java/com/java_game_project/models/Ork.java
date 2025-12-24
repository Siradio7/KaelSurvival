package com.java_game_project.models;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.java_game_project.utils.Constants;

public class Ork extends EntityModel {
    private final float DETECTION_RANGE = 100f;

    private float patrolTimer = 0;
    private final float PATROL_CHANGE_TIME = 2.0f;
    private final float ROTATION_ANGLES[] = { 0f, 45f, 90f, 135f, 180f, 225f, 270f, 315f };

    public Ork(float x, float y, float width, float height) {
        super(x, y, width, height);
        this.speed = Constants.ORK_SPEED;
    }

    public void updateAI(float delta, Player player, Array<Rectangle> obstacles, Rectangle target) {
        float distance = position.dst(player.getPosition());

        if (distance < DETECTION_RANGE) {
            chasePlayer(player);
            patrolTimer = 0;
        } else {
            patrolRandomly(delta);
        }

        applyMovement(delta, obstacles, target);
    }

    private float getNearestAngle(float angle) {
        angle = (angle % 360 + 360) % 360;
        float closest = ROTATION_ANGLES[0];
        float minDiff = Float.MAX_VALUE;

        for (float a : ROTATION_ANGLES) {
            float diff = Math.abs(a - angle);
            if (diff > 180) diff = 360 - diff;

            if (diff < minDiff) {
                minDiff = diff;
                closest = a;
            }
        }
        return closest;
    }

    private void chasePlayer(Player player) {
        Vector2 direction = new Vector2(player.getPosition()).sub(this.position).nor();
        float rawAngle = (float) Math.toDegrees(Math.atan2(direction.x, -direction.y));

        rotation = getNearestAngle(rawAngle);
        velocity.set((float) Math.sin(Math.toRadians(rotation)) * speed, -(float) Math.cos(Math.toRadians(rotation)) * speed);
    }

    private void patrolRandomly(float delta) {
        patrolTimer += delta;

        if (patrolTimer >= PATROL_CHANGE_TIME) {
            patrolTimer = 0;

            if (MathUtils.randomBoolean()) {
                rotation = ROTATION_ANGLES[MathUtils.random(ROTATION_ANGLES.length - 1)];
                velocity.set((float) Math.sin(Math.toRadians(rotation)) * (speed / 2), -(float) Math.cos(Math.toRadians(rotation)) * (speed / 2));
            } else {
                velocity.set(0, 0);
            }
        }
    }

    private void applyMovement(float delta, Array<Rectangle> obstacles, Rectangle target) {
        float oldX = position.x;
        float oldY = position.y;

        position.x += velocity.x * delta;
        bounds.setPosition(position);
        if (checkCollisions(obstacles, target)) {
            position.x = oldX;
        }

        position.y += velocity.y * delta;
        bounds.setPosition(position);
        if (checkCollisions(obstacles, target)) {
            position.y = oldY;
        }

        clampToMap();
        bounds.setPosition(position);
    }
}
