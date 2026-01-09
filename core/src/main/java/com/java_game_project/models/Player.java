package com.java_game_project.models;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.java_game_project.utils.Constants;

public class Player extends EntityModel {
    private float invincibleTimer = 0;
    private static final float INVINCIBILITY_DURATION = 1.0f;

    public Player(float x, float y, float width, float height) {
        super(x, y, width, height);
        this.speed = Constants.PLAYER_SPEED;
    }

    public void update(float delta, Array<Rectangle> obstacles, Rectangle target) {
        if (invincibleTimer > 0)
            invincibleTimer -= delta;
        updateAnimation(delta);
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

    public void takeDamage(int amount) {
        if (invincibleTimer <= 0) {
            damage(amount);
            invincibleTimer = INVINCIBILITY_DURATION;
        }
    }

    public boolean isInvincible() {
        return invincibleTimer > 0;
    }
}
