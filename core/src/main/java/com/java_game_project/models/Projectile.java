package com.java_game_project.models;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Projectile {
    private Vector2 position;
    private Vector2 velocity;
    private float speed = 400.0f;
    private float rotation;
    private boolean active;
    private Rectangle bounds;

    private Vector2 startPos;
    private Vector2 targetPos;
    private float totalDistance;
    private float arcHeight = 50.0f;

    public Projectile(float startX, float startY, float targetX, float targetY) {
        this.position = new Vector2(startX, startY);
        this.startPos = new Vector2(startX, startY);
        this.targetPos = new Vector2(targetX, targetY);
        this.totalDistance = startPos.dst(targetPos);
        this.active = true;
        this.bounds = new Rectangle(startX, startY, 32, 10);

        Vector2 direction = new Vector2(targetX - startX, targetY - startY).nor();
        this.velocity = direction.scl(speed);

        this.rotation = direction.angleDeg();
    }

    public void update(float delta) {
        position.add(velocity.x * delta, velocity.y * delta);
        bounds.setPosition(position);
    }

    public Vector2 getVisualPosition() {
        float currentDist = startPos.dst(position);
        float progress = MathUtils.clamp(currentDist / totalDistance, 0, 1);
        float heightOffset = arcHeight * MathUtils.sin((float) (progress * Math.PI));

        return new Vector2(position.x, position.y + heightOffset);
    }

    public float getVisualRotation() {
        float currentDist = startPos.dst(position);
        float progress = MathUtils.clamp(currentDist / totalDistance, 0, 1);

        float baseAngle = new Vector2(targetPos.x - startPos.x, targetPos.y - startPos.y).angleDeg();

        float pitch = 45.0f * MathUtils.cos((float) (progress * Math.PI));

        return baseAngle + pitch;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getRotation() {
        return rotation;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
