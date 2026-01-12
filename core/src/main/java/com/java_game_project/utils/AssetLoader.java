package com.java_game_project.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ObjectMap;

public class AssetLoader {
    // Stockage des textures pour éviter les doublons
    private static final ObjectMap<String, Texture> textures = new ObjectMap<>();

    /**
     * Charge une texture ou la récupère si elle est déjà en mémoire.
     */
    public static Texture getTexture(String path) {
        if (!textures.containsKey(path)) {
            if (Gdx.files.internal(path).exists()) {
                textures.put(path, new Texture(Gdx.files.internal(path)));
            } else if (Gdx.files.internal("assets/" + path).exists()) {
                textures.put(path, new Texture(Gdx.files.internal("assets/" + path)));
            } else {
                textures.put(path, new Texture(Gdx.files.internal(path)));
            }
        }
        return textures.get(path);
    }

    /**
     * Libère toutes les ressources chargées.
     */
    public static void dispose() {
        for (Texture tex : textures.values()) {
            tex.dispose();
        }

        textures.clear();
    }
}
