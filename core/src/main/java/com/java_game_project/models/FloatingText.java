package com.java_game_project.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class FloatingText {
    private String text;
    private Vector2 position;
    private Vector2 velocity;
    private float timer;
    private float maxTime;
    private Color color;
    private boolean finished;

    public FloatingText(String text, float x, float y, Color color) {
        this.text = text;
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(0, 50);
        this.color = color;
        this.maxTime = 1.0f;
        this.timer = 0;
        this.finished = false;
    }

    public void update(float delta) {
        position.add(velocity.x * delta, velocity.y * delta);
        timer += delta;
        if (timer >= maxTime) {
            finished = true;
        }
    }

    public boolean isFinished() {
        return finished;
    }

    public String getText() {
        return text;
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public Color getColor() {
        return color;
    }

    public float getAlpha() {
        return 1.0f - (timer / maxTime);
    }
}
