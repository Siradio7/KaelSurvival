package com.java_game_project.models;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class GameWorld {
    private Player player;
    private final Array<Ork> orks = new Array<>();

    private Rectangle target;
    private Rectangle exitZone;
    private float time;

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
    private final Array<FloatingText> floatingTexts = new Array<>();
    private final Array<Projectile> projectiles = new Array<>();
    private final Array<Rectangle> allBlockers = new Array<>();
    private final Array<Poulet> poulets = new Array<>();
    private final Array<Mouton> moutons = new Array<>();

    public Array<Rectangle> getObstacles() {
        allBlockers.clear();
        allBlockers.addAll(trees);
        allBlockers.addAll(walls);

        for (Consumable c : consumables) {
            allBlockers.add(c.getBounds());
        }
        return allBlockers;
    }

    public Array<Poulet> getPoulets() { return poulets; }
    public Array<Mouton> getMoutons() { return moutons; }

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

    public Array<FloatingText> getFloatingTexts() {
        return floatingTexts;
    }

    public void addFloatingText(FloatingText text) {
        floatingTexts.add(text);
    }

    public Array<Projectile> getProjectiles() {
        return projectiles;
    }

    public void addProjectile(Projectile p) {
        projectiles.add(p);
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public void update(float delta) {
        for (Poulet p : poulets) {
            p.update(delta);
        }

        for (Mouton m : moutons) {
            m.update(delta);
        }
    }

    public void clear() {
        orks.clear();
        trees.clear();
        walls.clear();
        consumables.clear();
        floatingTexts.clear();
        projectiles.clear();
        target = null;
        exitZone = null;
    }
}
