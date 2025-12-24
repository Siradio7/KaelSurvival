package com.java_game_project.models;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class GameWorld {
    private Player player;
    private final Array<Ork> orks = new Array<>();
    private final Array<Rectangle> obstacles = new Array<>();
    private Rectangle target;

    public void setPlayer(Player player) { this.player = player; }
    public Player getPlayer() { return player; }

    public Array<Ork> getOrks() { return orks; }
    public Array<Rectangle> getObstacles() { return obstacles; }
    public Rectangle getTarget() { return target; }
    public void setTarget(Rectangle target) { this.target = target; }

    public void addObstacle(Rectangle obstacle) {
        obstacles.add(obstacle);
    }

    public void clear() {
        orks.clear();
        obstacles.clear();
        target = null;
    }
}
