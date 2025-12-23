package com.java_game_project.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.java_game_project.models.GameWorld;
import com.java_game_project.models.Ork;
import com.java_game_project.models.Player;
import com.java_game_project.utils.MapManager;

public class WorldRenderer {
    private final GameWorld world;
    private final Texture playerTex, orkTex, treeTex, caveTex;

    public WorldRenderer(GameWorld world) {
        this.world = world;
        this.playerTex = new Texture(Gdx.files.internal("images/player.png"));
        this.orkTex = new Texture(Gdx.files.internal("maps/ork.png"));
        this.treeTex = new Texture(Gdx.files.internal("maps/TilesTree.png"));
        this.caveTex = new Texture(Gdx.files.internal("maps/mystic_cave.png"));
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        MapManager.getInstance().render(camera);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        Player p = world.getPlayer();
        if (p != null) {
            batch.draw(playerTex, p.getPosition().x, p.getPosition().y, p.getBounds().width / 2, p.getBounds().height / 2,
                p.getBounds().width, p.getBounds().height, 1, 1, p.getRotation(), 0, 0,
                playerTex.getWidth(), playerTex.getHeight(), false, false);
        }

        for (Rectangle r : world.getObstacles()) {
            batch.draw(treeTex, r.x, r.y, r.width, r.height);
        }

        for (Ork o : world.getOrks()) {
            batch.draw(orkTex, o.getPosition().x, o.getPosition().y, o.getBounds().width / 2, o.getBounds().height / 2,
                o.getBounds().width, o.getBounds().height, 1, 1, o.getRotation(), 0, 0,
                orkTex.getWidth(), orkTex.getHeight(), false, false);
        }

        batch.end();
    }

    public void dispose() {
        playerTex.dispose();
        orkTex.dispose();
        treeTex.dispose();
        caveTex.dispose();
    }
}
