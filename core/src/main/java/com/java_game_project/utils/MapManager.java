package com.java_game_project.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.java_game_project.models.MapModel;

import java.util.HashMap;
import java.util.Map;

public class MapManager {
    private static MapManager instance;
    private final Map<String, MapModel> maps = new HashMap<>();
    private OrthogonalTiledMapRenderer renderer;
    private TiledMap currentMap;
    private String currentMapName;

    public static MapManager getInstance() {
        if (instance == null) {
            instance = new MapManager();
        }

        return instance;
    }

    private MapManager() {

    }

    public void addMaps(String path) {
        FileHandle folder = Gdx.files.internal(path);

        if (!folder.exists() || !folder.isDirectory()) {
            Gdx.app.error("MapManager", "Le dossier " + path + " est introuvable ou invalide !");
            return;
        }

        for (FileHandle file : folder.list()) {
            if (file.extension().equalsIgnoreCase("tmx")) {
                String mapName = file.nameWithoutExtension();
                MapModel mapModel = new MapModel(mapName);
                
                maps.put(mapName, mapModel);
            }
        }
    }

    public void loadMap(String mapName) {
        if (currentMap != null) {
            currentMap.dispose();
        }

        currentMap = maps.get(mapName).getMap();
        renderer = new OrthogonalTiledMapRenderer(currentMap);
        currentMapName = mapName;
    }

    public String getCurrentMap() {
        return currentMapName;
    }

    public void render(OrthographicCamera camera) {
        renderer.setView(camera);
        renderer.render();
    }

    public void dispose() {
        currentMap.dispose();
        renderer.dispose();
    }
}
