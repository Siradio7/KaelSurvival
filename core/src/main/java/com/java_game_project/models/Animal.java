package com.java_game_project.models;

import com.badlogic.gdx.math.MathUtils;

public abstract class Animal extends EntityModel implements Edible {

    protected float lifeValue;
    protected boolean active = true;
    protected float respawnTimer = 0f;

    public Animal(float x, float y, float w, float h, float lifeValue) {
        super(x, y, w, h);
        this.lifeValue = lifeValue;
        this.speed = 30f;

        velocity.x = MathUtils.random(-1f, 1f) * speed;
        velocity.y = MathUtils.random(-1f, 1f) * speed;
    }

    public void update(float delta) {

        if (!active) {
            respawnTimer -= delta;
            if (respawnTimer <= 0) active = true;
            return;
        }

        position.x += velocity.x * delta;
        position.y += velocity.y * delta;
        bounds.setPosition(position);
    }

    @Override
    public float getLifeValue() {
        return lifeValue;
    }

    @Override
    public void eat() {
        active = false;
        respawnTimer = 5f;
    }

    @Override
    public boolean isActive() {
        return active;
    }
}
