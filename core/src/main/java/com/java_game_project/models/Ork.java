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
    private Vector2 patrolDirection = new Vector2();

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

    private void chasePlayer(Player player) {
        Vector2 direction = new Vector2(player.getPosition())
            .sub(this.position)
            .nor();
        velocity.set(direction.x * speed, direction.y * speed);

        rotation = (float) Math.toDegrees(Math.atan2(velocity.x, -velocity.y));
    }

    private void patrolRandomly(float delta) {
        patrolTimer += delta;

        if (patrolTimer >= PATROL_CHANGE_TIME) {
            patrolTimer = 0;

            if (MathUtils.randomBoolean()) {
                float randomAngle = MathUtils.random(0f, 360f);
                patrolDirection.set(1, 0).setAngleDeg(randomAngle);
                velocity.set(patrolDirection.x * (speed / 2), patrolDirection.y * (speed / 2));
                rotation = randomAngle;
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
