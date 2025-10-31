package com.java_game_project.models;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class MapModel {
    private final TiledMap map;
    private final String mapName;
    public static final float MAP_WIDTH = 50 * 32f;
    public static final float MAP_HEIGHT = 30 * 32f;

    public MapModel(String mapName) {
        this.mapName = mapName;
        this.map = new TmxMapLoader().load("maps/" + mapName + ".tmx");
    }

    public TiledMap getMap() {
        return map;
    }

    public String getMapName() {
        return mapName;
    }

    public MapObject getMapObjectByName(String layerName, String objectName) {
        MapObjects objects = getObjectLayerObjects(layerName);

        if (objects != null) {
            return objects.get(objectName);
        }

        return null;
    }

    public MapObjects getObjectLayerObjects(String layerName) {
        MapLayer layer = map.getLayers().get(layerName);

        if (layer != null) {
            return layer.getObjects();
        }

        return null;
    }

    public void dispose() {
        map.dispose();
    }
}
