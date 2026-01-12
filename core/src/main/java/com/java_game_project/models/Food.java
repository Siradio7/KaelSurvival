package com.java_game_project.models;
public class Food extends EntityModel implements Edible {

    protected float lifeValue;
    protected boolean active = true;
    protected float respawnTimer = 0f;

    public Food(float x, float y, float w, float h, float lifeValue) {
        super(x, y, w, h);
        this.lifeValue = lifeValue;
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

    public void update(float delta) {
        if (!active) {
            respawnTimer -= delta;
            if (respawnTimer <= 0) active = true;
        }
    }
}
