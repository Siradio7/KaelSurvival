package com.java_game_project.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.java_game_project.Main;
import com.java_game_project.controllers.GameController;
import com.java_game_project.models.*;
import com.java_game_project.utils.Constants;
import com.java_game_project.utils.MapManager;
import com.java_game_project.views.WorldRenderer;
import com.java_game_project.views.Hud;

public class GameScreen extends AbstractScreen {
    private GameWorld world;
    private GameController controller;
    private WorldRenderer renderer;
    private Hud hud;
    private String currentMapName;
    private int initialHealth = -1;
    private float initialTime = 0;

    public GameScreen(Main game) {
        this(game, Constants.LEVEL_2_MAP);
    }

    public GameScreen(Main game, String mapName) {
        super(game);
        this.currentMapName = mapName;
    }

    public GameScreen(Main game, String mapName, int health, float time) {
        super(game);
        this.currentMapName = mapName;
        this.initialHealth = health;
        this.initialTime = time;
    }

    @Override
    public void show() {
        camera.setToOrtho(false, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        camera.zoom = 0.7f;

        world = new GameWorld();
        world = new GameWorld();
        world.setTime(initialTime);
        loadMapData();

        if (initialHealth != -1 && world.getPlayer() != null) {
            world.getPlayer().setHealth(initialHealth);
        }

        controller = new GameController(world, currentMapName);
        renderer = new WorldRenderer(world);
        hud = new Hud(batch, world);
    }

    private void loadMapData() {
        MapManager.getInstance().addMaps(Constants.MAPS_PATH);
        MapManager.getInstance().loadMap(currentMapName);

        MapLayer layer = MapManager.getInstance().getCurrentMap().getLayers().get("Object Layer 1");
        for (MapObject object : layer.getObjects()) {
            if (!(object instanceof RectangleMapObject))
                continue;
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            String name = object.getName();

            if ("tree".equals(name)) {
                world.addTree(new Rectangle(rect));
            } else if ("obstacle".equals(name)) {
                world.addWall(new Rectangle(rect));
            } else if ("nourriture".equals(name)) {
                world.addConsumable(new Consumable(rect.x, rect.y, rect.width, rect.height));
            } else if ("kael_start".equals(name)) {
                world.setPlayer(new Player(rect.x, rect.y, 70, 70));
            } else if ("ork".equals(name)) {
                world.getOrks().add(new Ork(rect.x, rect.y, 70, 70));
            } else if ("target".equals(name)) {
                world.setTarget(new Rectangle(rect));
            } if ("poulet".equals(name)) {
                if (Constants.LEVEL_2_MAP.equals(currentMapName)) {
                    world.getPoulets().add(new Poulet(rect.x, rect.y));
                }
            } else if ("mouton".equals(name)) {
                if (Constants.LEVEL_2_MAP.equals(currentMapName)) {
                    world.getMoutons().add(new Mouton(rect.x, rect.y));
                }
            }else if ("exit_zone".equals(name)) {
                world.setExitZone(new Rectangle(rect));
            }
        }
    }

    @Override
    public void render(float delta) {
        for (Poulet p : world.getPoulets()) {
            p.updateAI(delta, world.getObstacles(), world.getTarget());
        }

        for (Mouton m : world.getMoutons()) {
            m.updateAI(delta, world.getObstacles(), world.getTarget());
        }

        controller.update(delta, camera);

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render(batch, camera);

        batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
        hud.update(delta);
    }

    @Override
    public void resize(int width, int height) {
        hud.stage.getViewport().update(width, height);
    }

    @Override
    public void dispose() {
        renderer.dispose();
        MapManager.getInstance().dispose();
        hud.dispose();
    }
}
