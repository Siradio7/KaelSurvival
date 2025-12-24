package com.java_game_project.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.java_game_project.Main;
import com.java_game_project.controllers.GameController;
import com.java_game_project.models.GameWorld;
import com.java_game_project.models.Ork;
import com.java_game_project.models.Player;
import com.java_game_project.utils.Constants;
import com.java_game_project.utils.MapManager;
import com.java_game_project.views.WorldRenderer;

public class GameScreen extends AbstractScreen {
    private GameWorld world;
    private GameController controller;
    private WorldRenderer renderer;

    public GameScreen(Main game) {
        super(game);
    }

    @Override
    public void show() {
        camera.setToOrtho(false, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        camera.zoom = 0.5f;

        world = new GameWorld();
        loadMapData();

        controller = new GameController(world);
        renderer = new WorldRenderer(world);
    }

    private void loadMapData() {
        MapManager.getInstance().addMaps(Constants.MAPS_PATH);
        MapManager.getInstance().loadMap(Constants.LEVEL_3_MAP);

        MapLayer layer = MapManager.getInstance().getCurrentMap().getLayers().get("Object Layer 1");
        for (MapObject object : layer.getObjects()) {
            if (!(object instanceof RectangleMapObject)) continue;
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            String name = object.getName();

            if ("tree".equals(name)) world.getObstacles().add(new Rectangle(rect));
            else if ("kael_start".equals(name)) world.setPlayer(new Player(rect.x, rect.y, 70, 70));
            else if ("ork".equals(name)) world.getOrks().add(new Ork(rect.x, rect.y, 70, 70));
            else if ("target".equals(name)) world.setTarget(new Rectangle(rect));
        }
    }

    @Override
    public void render(float delta) {
        controller.update(delta, camera);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render(batch, camera);
    }

    @Override
    public void dispose() {
        renderer.dispose();
        MapManager.getInstance().dispose();
    }
}
