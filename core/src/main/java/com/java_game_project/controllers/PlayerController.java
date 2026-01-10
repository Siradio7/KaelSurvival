package com.java_game_project.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.java_game_project.models.Player;
import com.java_game_project.utils.EntityState;

public class PlayerController {
    private final Player player;

    public PlayerController(Player player) {
        this.player = player;
    }

    public Player update(boolean canUpdateState) {
        float vx = 0;
        float vy = 0;

        if (Gdx.input.isKeyPressed(Keys.Z) || Gdx.input.isKeyPressed(Keys.UP))
            vy = player.getSpeed();
        if (Gdx.input.isKeyPressed(Keys.S) || Gdx.input.isKeyPressed(Keys.DOWN))
            vy = -player.getSpeed();
        if (Gdx.input.isKeyPressed(Keys.Q) || Gdx.input.isKeyPressed(Keys.LEFT))
            vx = -player.getSpeed();
        if (Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.RIGHT))
            vx = player.getSpeed();

        player.getVelocity().set(vx, vy);

        if (vx != 0 || vy != 0) {
            if (canUpdateState)
                player.setState(EntityState.WALKING);
            float angle = (float) Math.toDegrees(Math.atan2(vx, -vy));
            player.setRotation(angle);
        } else {
            if (canUpdateState)
                player.setState(EntityState.IDLE);
        }

        return player;
    }
}
