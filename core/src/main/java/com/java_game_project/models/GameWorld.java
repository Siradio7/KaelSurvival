package com.java_game_project.models;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class GameWorld {
    private Player player;
    private final Array<Ork> orks = new Array<>();
    private final Array<Rectangle> obstacles = new Array<>();
    private final Array<Rectangle> targets = new Array<>();

    public void setPlayer(Player player) { this.player = player; }
    public Player getPlayer() { return player; }

    public Array<Ork> getOrks() { return orks; }
    public Array<Rectangle> getObstacles() { return obstacles; }
    public Array<Rectangle> getTargets() { return targets; }

    public void clear() {
        orks.clear();
        obstacles.clear();
        targets.clear();
    }
}
