package com.java_game_project.models;

import com.badlogic.gdx.math.Rectangle;

public class Consumable {
    private Rectangle bounds;
    private boolean active;
    private float cooldownTimer;
    private static final float COOLDOWN_DURATION = 5.0f;
    private static final float HEAL_RATE = 2.0f;

    public Consumable(float x, float y, float width, float height) {
        this.bounds = new Rectangle(x, y, width, height);
        this.active = true;
        this.cooldownTimer = 0;
    }

    public void update(float delta) {
        if (!active) {
            cooldownTimer -= delta;
            if (cooldownTimer <= 0) {
                active = true;
            }
        }
    }

    public void consume() {
        active = false;
        cooldownTimer = COOLDOWN_DURATION;
    }

    public boolean isActive() {
        return active;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public static float getHealRate() {
        return HEAL_RATE;
    }
}
