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
import com.java_game_project.controllers.PlayerController;
import com.java_game_project.models.Ork;
import com.java_game_project.models.Player;
import com.java_game_project.utils.AudioManager;
import com.java_game_project.utils.Constants;
import com.java_game_project.utils.MapManager;

public class GameScreen extends AbstractScreen {

    private final MapManager mapManager = MapManager.getInstance();
    private final AudioManager audioManager = AudioManager.getInstance();

    private Texture playerTexture;
    private Texture treeTexture;
    private Texture orkTexture;

    private final Array<Sprite> trees = new Array<>();
    private final Array<Ork> orks = new Array<>();
    private Player player;
    private PlayerController playerController;

    public GameScreen(Main game) {
        super(game);
        audioManager.playMusic(Constants.MENU_MUSIC, true);
    }

    @Override
    public void show() {
        camera.setToOrtho(false, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        camera.zoom = 0.5f;
        mapManager.addMaps(Constants.MAPS_PATH);
        mapManager.loadMap(Constants.LEVEL_3_MAP);

        initTextures();
        loadMapObjects();
    }

    private void initTextures() {
        playerTexture = new Texture(Gdx.files.internal("images/player.png"));
        orkTexture    = new Texture(Gdx.files.internal("maps/ork.png"));
        treeTexture   = new Texture(Gdx.files.internal("maps/TilesTree.png"));
    }

    private void loadMapObjects() {
        MapLayer layer = mapManager.getCurrentMap().getLayers().get("Object Layer 1");
        if (layer == null) return;

        for (MapObject object : layer.getObjects()) {
            if (!(object instanceof RectangleMapObject)) continue;

            String name = object.getName();
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            if ("tree".equals(name)) {
                Sprite tree = new Sprite(treeTexture);

                tree.setSize(rect.width, rect.height);
                tree.setPosition(rect.x, rect.y);
                trees.add(tree);
            } else if ("kael_start".equals(name)) {
                player = new Player(rect.x, rect.y, 70, 70);
                playerController = new PlayerController(player);
            } else if ("ork".equals(name)) {
                orks.add(new Ork(rect.x, rect.y, 70, 70));
            }
        }
    }

    private void updateEntities(float delta) {
        if (playerController != null) {
            player = playerController.update();
        }

        if (player != null) {
            Array<Rectangle> obstacles = new Array<>();
            for (Sprite s : trees) obstacles.add(s.getBoundingRectangle());
            for (Ork o : orks) obstacles.add(o.getBounds());

            player.update(delta, obstacles);

            float targetX = player.getPosition().x + player.getBounds().width / 2;
            float targetY = player.getPosition().y + player.getBounds().height / 2;

            float halfViewWidth = (camera.viewportWidth * camera.zoom) / 2f;
            float halfViewHeight = (camera.viewportHeight * camera.zoom) / 2f;

            float camX = Math.max(halfViewWidth, Math.min(targetX, Constants.WINDOW_WIDTH - halfViewWidth));
            float camY = Math.max(halfViewHeight, Math.min(targetY, Constants.WINDOW_HEIGHT - halfViewHeight));

            for (Ork o : orks) {
                o.updateAI(delta, player, obstacles);
            }

            camera.position.set(camX, camY, 0);
            camera.update();
        }

        for (Ork o : orks) o.update(delta);
    }

    @Override
    public void render(float delta) {
        updateEntities(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mapManager.render(camera);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        if (player != null) {
            batch.draw(playerTexture, player.getPosition().x, player.getPosition().y, player.getBounds().width / 2, player.getBounds().height / 2, player.getBounds().width, player.getBounds().height, 1, 1, player.getRotation(), 0, 0, playerTexture.getWidth(), playerTexture.getHeight(), false, false);
        }

        for (Sprite tree : trees) tree.draw(batch);
        for (Ork o : orks) {
            Rectangle b = o.getBounds();

            batch.draw(orkTexture, o.getPosition().x, o.getPosition().y, b.width / 2, b.height / 2, b.width, b.height, 1, 1, o.getRotation(), 0, 0, orkTexture.getWidth(), orkTexture.getHeight(), false, false);
        }

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }

    @Override public void dispose() {
        batch.dispose();
        mapManager.dispose();
        if (playerTexture != null) playerTexture.dispose();
        if (treeTexture != null) treeTexture.dispose();
        if (orkTexture != null) orkTexture.dispose();
    }
}
