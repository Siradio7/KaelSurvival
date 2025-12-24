package com.java_game_project.models;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.java_game_project.utils.Constants;
import com.java_game_project.utils.EntityState;

public abstract class EntityModel {
    protected Vector2 position;
    protected Vector2 velocity;
    protected Rectangle bounds;
    protected float speed;
    protected float rotation;

    protected EntityState state = EntityState.IDLE;
    protected float stateTime = 0;

    public EntityModel(float x, float y, float width, float height) {
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(0, 0);
        this.bounds = new Rectangle(x, y, width, height);
    }

    public void update(float delta) {
        position.add(velocity.x * delta, velocity.y * delta);
        bounds.setPosition(position);
        stateTime += delta;
    }

    public void setState(EntityState newState) {
        if (this.state != newState) {
            this.state = newState;
            this.stateTime = 0;
        }
    }

    public EntityState getState() { return state; }
    public float getStateTime() { return stateTime; }
    public Vector2 getPosition() { return position; }
    public Rectangle getBounds() { return bounds; }
    public Vector2 getVelocity() { return velocity; }
    public float getSpeed() { return speed; }
    public float getRotation() { return rotation; }
    public void setRotation(float rotation) { this.rotation = rotation; }

    protected void clampToMap() {
        if (position.x < 0) position.x = 0;
        if (position.y < 0) position.y = 0;
        if (position.x > Constants.WINDOW_WIDTH - bounds.width) position.x = Constants.WINDOW_WIDTH - bounds.width;
        if (position.y > Constants.WINDOW_HEIGHT - bounds.height) position.y = Constants.WINDOW_HEIGHT - bounds.height;
    }

    protected boolean checkCollisions(Array<Rectangle> obstacles, Rectangle target) {
        for (Rectangle rect : obstacles) {
            if (bounds.overlaps(rect)) return true;
        }

        return target != null && bounds.overlaps(target);
    }

    public void updateAnimation(float delta) {
        this.stateTime += delta;
    }
}
