package com.java_game_project.models;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class GameWorld {
    private Player player;
    private final Array<Ork> orks = new Array<>();

    private Rectangle target;
    private Rectangle exitZone;

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public Array<Ork> getOrks() {
        return orks;
    }

    private final Array<Rectangle> trees = new Array<>();
    private final Array<Rectangle> walls = new Array<>();
    private final Array<Consumable> consumables = new Array<>();
    private final Array<Rectangle> allBlockers = new Array<>();

    public Array<Rectangle> getObstacles() {
        allBlockers.clear();
        allBlockers.addAll(trees);
        allBlockers.addAll(walls);

        for (Consumable c : consumables) {
            allBlockers.add(c.getBounds());
        }
        return allBlockers;
    }

    public Array<Rectangle> getTrees() {
        return trees;
    }

    public Array<Rectangle> getWalls() {
        return walls;
    }

    public Array<Consumable> getConsumables() {
        return consumables;
    }

    public void addTree(Rectangle tree) {
        trees.add(tree);
    }

    public void addWall(Rectangle wall) {
        walls.add(wall);
    }

    public void addConsumable(Consumable item) {
        consumables.add(item);
    }

    public Rectangle getTarget() {
        return target;
    }

    public void setTarget(Rectangle target) {
        this.target = target;
    }

    public Rectangle getExitZone() {
        return exitZone;
    }

    public void setExitZone(Rectangle exitZone) {
        this.exitZone = exitZone;
    }

    public void clear() {
        orks.clear();
        trees.clear();
        walls.clear();
        consumables.clear();
        consumables.clear();
        target = null;
        exitZone = null;
    }
}
