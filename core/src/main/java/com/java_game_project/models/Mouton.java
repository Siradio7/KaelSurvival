package com.java_game_project.models;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.java_game_project.utils.Constants;
import com.java_game_project.utils.EntityState;

public class Mouton extends EntityModel {

    private float patrolTimer = 0f;
    private static final float PATROL_CHANGE_TIME = 2.5f;
    private static final float[] ROTATION_ANGLES =
        {0f, 45f, 90f, 135f, 180f, 225f, 270f, 315f};

    public Mouton(float x, float y) {
        super(x, y, 25, 25);
        this.speed = Constants.MOUTON_SPEED;
    }

    public void updateAI(float delta, Array<Rectangle> obstacles, Rectangle target) {
        patrolTimer += delta;

        if (patrolTimer >= PATROL_CHANGE_TIME) {
            patrolTimer = 0;

            if (MathUtils.randomBoolean()) {
                rotation = ROTATION_ANGLES[MathUtils.random(ROTATION_ANGLES.length - 1)];
                velocity.set(
                    MathUtils.sinDeg(rotation) * speed,
                    -MathUtils.cosDeg(rotation) * speed
                );
            } else {
                velocity.set(0, 0);
            }
        }

        applyMovement(delta, obstacles, target);
    }

    private void applyMovement(float delta, Array<Rectangle> obstacles, Rectangle target) {
        updateAnimation(delta);

        float oldX = position.x;
        float oldY = position.y;

        position.x += velocity.x * delta;
        bounds.setPosition(position);
        if (checkCollisions(obstacles, target)) position.x = oldX;

        position.y += velocity.y * delta;
        bounds.setPosition(position);
        if (checkCollisions(obstacles, target)) position.y = oldY;

        setState((velocity.x != 0 || velocity.y != 0)
            ? EntityState.WALKING
            : EntityState.IDLE);

        clampToMap();
        bounds.setPosition(position);
    }
}
