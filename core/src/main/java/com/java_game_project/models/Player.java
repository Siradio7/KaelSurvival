package com.java_game_project.models;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.java_game_project.utils.Constants;

public class Player extends EntityModel {
    public Player(float x, float y, float width, float height) {
        super(x, y, width, height);
        this.speed = Constants.PLAYER_SPEED;
    }

    public void update(float delta, Array<Rectangle> obstacles) {
        float oldX = position.x;
        float oldY = position.y;

        position.x += velocity.x * delta;
        bounds.setPosition(position);

        if (checkCollisions(obstacles)) {
            position.x = oldX;
        }

        position.y += velocity.y * delta;
        bounds.setPosition(position);

        if (checkCollisions(obstacles)) {
            position.y = oldY;
        }

        clampToMap();
        bounds.setPosition(position);
    }
}
