package com.java_game_project.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationUtils {
    public static Animation<TextureRegion> loadAnimation(String path, int frameWidth, int frameHeight,
            float frameDuration) {
        Texture sheet = AssetLoader.getTexture(path);

        if (sheet.getWidth() < frameWidth || sheet.getHeight() < frameHeight) {
            com.badlogic.gdx.Gdx.app.error("AnimationUtils", "Texture " + path + " trop petite (" + sheet.getWidth()
                    + "x" + sheet.getHeight() + ") pour frame " + frameWidth + "x" + frameHeight);
            // Retourne une animation valide avec la texture complète pour éviter le crash
            return new Animation<>(frameDuration, new TextureRegion(sheet));
        }

        TextureRegion[][] tmp = TextureRegion.split(sheet, frameWidth, frameHeight);
        int cols = sheet.getWidth() / frameWidth;
        int rows = sheet.getHeight() / frameHeight;

        if (cols * rows == 0) {
            com.badlogic.gdx.Gdx.app.error("AnimationUtils", "Aucune frame extraite pour " + path);
            return new Animation<>(frameDuration, new TextureRegion(sheet));
        }

        TextureRegion[] frames = new TextureRegion[cols * rows];

        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                frames[index++] = tmp[i][j];
            }
        }

        return new Animation<>(frameDuration, frames);
    }
}
