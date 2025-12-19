package com.java_game_project.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.java_game_project.Main;
import com.java_game_project.utils.AudioManager;
import com.java_game_project.utils.Constants;
import com.java_game_project.utils.MapManager;

public class GameScreen extends AbstractScreen {

    private final MapManager mapManager = MapManager.getInstance();
    private final AudioManager audioManager = AudioManager.getInstance();

    // ===== OBJETS DE LA MAP =====
    private Texture treeTexture;
    private Texture orkTexture;

    private final Array<Sprite> trees = new Array<>();
    private final Array<Sprite> orks = new Array<>();

    public GameScreen(Main game) {
        super(game);
        audioManager.playMusic(Constants.MENU_MUSIC, true);
    }

    @Override
    public void show() {
        camera.setToOrtho(false, 800, 480);

        mapManager.addMaps(Constants.MAPS_PATH);
        mapManager.loadMap(Constants.LEVEL_3_MAP);

        loadMapObjects();
    }

    private void loadMapObjects() {

        treeTexture = new Texture(Gdx.files.internal(Constants.MAPS_PATH + "TilesTree.png"));
        orkTexture  = new Texture(Gdx.files.internal(Constants.MAPS_PATH + "ork.png"));

        MapLayer layer = mapManager.getCurrentMap().getLayers().get("Object Layer 1");
        if (layer == null) return;

        for (MapObject object : layer.getObjects()) {

            if (!(object instanceof RectangleMapObject)) continue;

            String name = object.getName();
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            // 🔥 CORRECTION COORDONNÉES TILED → LIBGDX
            float fixedY = rect.y - rect.height;

            if ("tree".equals(name)) {

                Sprite tree = new Sprite(treeTexture);
                tree.setPosition(rect.x, fixedY);
                tree.setScale(0.5f);

                trees.add(tree);

            } else if ("ork".equals(name)) {

                Sprite ork = new Sprite(orkTexture);
                ork.setPosition(rect.x, fixedY);
                ork.setScale(0.1f);

                orks.add(ork);
            }
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Map
        mapManager.render(camera);

        // Objets
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        for (Sprite tree : trees) {
            tree.draw(batch);
        }

        for (Sprite ork : orks) {
            ork.draw(batch);
        }

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        mapManager.dispose();

        if (treeTexture != null) treeTexture.dispose();
        if (orkTexture != null) orkTexture.dispose();
    }
}
